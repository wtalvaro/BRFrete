package br.com.wta.frete.core.service;

import static org.mockito.Mockito.lenient;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
// Import org.mockito.InjectMocks; <--- Removido!
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.StringRedisTemplate;

@ExtendWith(MockitoExtension.class)
public class TokenAtivacaoServiceTest {

    // Simula a injeção do TokenAtivacaoService. Removendo @InjectMocks.
    private TokenAtivacaoService tokenAtivacaoService;

    // Mock da dependência principal (StringRedisTemplate)
    @Mock
    private StringRedisTemplate redisTemplate;

    // Mock das operações de valor do Redis
    @Mock
    private ValueOperations<String, String> valueOperations;

    // --- VARIÁVEIS DE TESTE ---
    private final Long PESSOA_ID = 123L;
    private final String CHAVE_PREFIXO_LOOKUP = "ativacao:";
    private final String CHAVE_PREFIXO_ACTIVATE = "activate:";
    private final Long EXPIRACAO_HORAS = 24L;

    @BeforeEach
    void setUp() {
        // PASSO 1: Configura o Mockito

        // ESSENCIAL: Configura o redisTemplate mockado para retornar o nosso mock
        // 'valueOperations'. Isso DEVE ser feito ANTES de criarmos o serviço.
        doReturn(valueOperations).when(redisTemplate).opsForValue();

        // Configura o 'valueOperations' mockado para retornar o 'redisTemplate' mockado
        // quando o método 'getOperations()' é chamado (usado nos métodos de remoção).
        lenient().doReturn(redisTemplate).when(valueOperations).getOperations();

        // PASSO 2: Inicializa o Serviço MANUALMENTE.
        // Isso garante que ele use o 'redisTemplate' JÁ CONFIGURADO,
        // evitando o NullPointerException no construtor.
        this.tokenAtivacaoService = new TokenAtivacaoService(redisTemplate);
    }

    // =================================================================
    // TESTE 1: criarToken()
    // =================================================================

    @Test
    void deveCriarTokenEArmazenarCorretamenteNoRedis() {
        // ARRANGE: Nada especial, apenas o setup já basta.

        // ACT: Cria o token (o service usa UUID.randomUUID(), então o valor será
        // sempre diferente)
        String tokenGerado = tokenAtivacaoService.criarToken(PESSOA_ID);

        // ASSERT: Verifica se o token foi gerado (não é vazio ou nulo)
        assertNotNull(tokenGerado, "O token não deve ser nulo.");
        assertEquals(32, tokenGerado.length(), "O token deve ter 32 caracteres (UUID sem hífens).");

        // ASSERT: Verifica se as duas chaves foram salvas no Redis com o TTL correto

        // 1. Verifica a chave principal (activate:ID -> Token)
        verify(valueOperations, times(1)).set(eq(CHAVE_PREFIXO_ACTIVATE + PESSOA_ID), eq(tokenGerado),
                eq(EXPIRACAO_HORAS), eq(TimeUnit.HOURS));

        // 2. Verifica a chave de lookup (ativacao:Token -> ID)
        verify(valueOperations, times(1)).set(eq(CHAVE_PREFIXO_LOOKUP + tokenGerado), eq(PESSOA_ID.toString()),
                eq(EXPIRACAO_HORAS), eq(TimeUnit.HOURS));
    }

    // =================================================================
    // TESTE 2: obterIdPessoaPorToken(String token) - Caminho Feliz
    // =================================================================

    @Test
    void deveObterIdPessoaPorTokenERemoverAsChaves() {
        // ARRANGE: Token que vamos simular que existe no Redis
        String tokenValido = UUID.randomUUID().toString().replace("-", "");
        String chaveLookup = CHAVE_PREFIXO_LOOKUP + tokenValido;
        String chaveActivate = CHAVE_PREFIXO_ACTIVATE + PESSOA_ID;

        // Simula que o token está no Redis (chave: ativacao:TOKEN -> valor: "123")
        when(valueOperations.get(chaveLookup)).thenReturn(PESSOA_ID.toString());

        // Simula que o Token (o valor da chave activate:ID) existe para ser removido
        // depois (linha 82 do Service)
        when(valueOperations.get(chaveActivate)).thenReturn(tokenValido);

        // ACT: Tenta obter o ID
        Long idPessoa = tokenAtivacaoService.obterIdPessoaPorToken(tokenValido);

        // ASSERT: O ID deve ser retornado e as chaves devem ser removidas

        // 1. Verifica se o ID correto foi retornado
        assertNotNull(idPessoa, "O ID da pessoa não deve ser nulo.");
        assertEquals(PESSOA_ID, idPessoa, "O ID retornado deve ser o correto.");

        // 2. Verifica se a chave de lookup foi deletada (remoção da primeira chave no
        // removerToken)
        verify(redisTemplate, times(1)).delete(eq(chaveLookup));

        // 3. Verifica se a chave principal foi deletada (remoção da segunda chave no
        // removerTokenPorId)
        verify(redisTemplate, times(1)).delete(eq(chaveActivate));
    }

    // =================================================================
    // TESTE 3: obterIdPessoaPorToken(String token) - Token Inválido/Expirado
    // =================================================================

    @Test
    void deveRetornarNullSeTokenNaoExistirOuEstiverExpirado() {
        // ARRANGE: Simula que o token NÃO está no Redis
        String tokenInvalido = "token-que-nao-existe";
        String chaveLookup = CHAVE_PREFIXO_LOOKUP + tokenInvalido;

        // Simula que o Redis retorna NULL
        when(valueOperations.get(chaveLookup)).thenReturn(null);

        // ACT: Tenta obter o ID
        Long idPessoa = tokenAtivacaoService.obterIdPessoaPorToken(tokenInvalido);

        // ASSERT: Deve retornar null e NÃO deve tentar deletar nada
        assertNull(idPessoa, "Deve retornar null para token inexistente.");

        // Verifica que NENHUM método delete foi chamado no redisTemplate
        verify(redisTemplate, never()).delete(anyString());
    }
}
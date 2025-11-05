package br.com.wta.frete.core.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance; // Importação Adicionada
import org.junit.jupiter.api.TestInstance.Lifecycle; // Importação Adicionada
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import br.com.wta.frete.core.controller.dto.CadastroSimplificadoRequest;
import br.com.wta.frete.core.entity.Perfil; // <--- Importação Adicionada
import br.com.wta.frete.core.entity.Pessoa;
import br.com.wta.frete.core.repository.PerfilRepository; // <--- Importação Adicionada
import br.com.wta.frete.core.repository.PessoaRepository; // <--- Importação Adicionada

/**
 * Teste de integração para CadastroPessoaService.
 * Nota: Usa o perfil 'test' com um banco de dados em memória ou de teste.
 */
@SpringBootTest
@ActiveProfiles("test")
@TestInstance(Lifecycle.PER_CLASS)
public class CadastroPessoaServiceTest {

    @Autowired
    private CadastroPessoaService cadastroPessoaService;

    @Autowired
    private PessoaRepository pessoaRepository;

    @Autowired
    private PerfilRepository perfilRepository; // <--- Campo Injetado

    @Autowired
    private PasswordEncoder passwordEncoder;

    @SuppressWarnings("removal")
    @MockBean // Mock para evitar o envio real de e-mails em testes
    private EmailService emailService;

    @Autowired
    private StringRedisTemplate redisTemplate; // Para limpar/verificar o Redis

    /**
     * Documentação: Limpa o banco e o Redis antes de CADA teste para garantir
     * isolamento.
     */
    @BeforeEach
    void setup() {
        // Limpeza de dados (garante que os testes são independentes)
        pessoaRepository.deleteAll();
        perfilRepository.deleteAll(); // Limpa os perfis existentes

        // Limpa todas as chaves 'activate:*' no Redis
        // Isso é necessário se o `CadastroPessoaService` estiver sendo testado.
        redisTemplate.execute((RedisCallback<Object>) connection -> {
            connection.serverCommands().flushDb();
            return null;
        });

        // -----------------------------------------------------------
        // 🛠️ NOVO CÓDIGO: INSERÇÃO DOS PERFIS ESSENCIAIS PARA O TESTE
        // -----------------------------------------------------------

        // Insere o perfil LEAD
        Perfil lead = new Perfil(null, "LEAD", "Utilizador em fase de engajamento inicial.");
        perfilRepository.save(lead);

        // Insere o perfil CLIENTE (O perfil que estava faltando no setup de teste)
        Perfil cliente = new Perfil(null, "CLIENTE", "Pessoa que utiliza os serviços (solicita frete/compra).");
        perfilRepository.save(cliente);

        // O perfil de teste agora está garantido no banco de teste.
        // -----------------------------------------------------------

        // Mock do serviço de e-mail para não enviar e-mails reais
        doNothing().when(emailService).enviarEmailAtivacao(anyString(), anyString());
    }

    // --- Teste 1: Caminho Feliz (Happy Path) ---

    @Test
    void deveCadastrarPessoaSimplificadoComSucesso() {
        // 1. ARRANGE
        CadastroSimplificadoRequest request = new CadastroSimplificadoRequest("teste@email.com", "senha123");

        // 2. ACT
        Pessoa pessoaSalva = cadastroPessoaService.cadastrarPessoaSimplificado(request);

        // 3. ASSERT
        assertNotNull(pessoaSalva.getId(), "O ID da pessoa não deve ser nulo após salvar.");
        assertTrue(passwordEncoder.matches("senha123", pessoaSalva.getSenha()),
                "A senha deve estar criptografada.");
        assertEquals(false, pessoaSalva.isAtivo(), "A pessoa deve começar como INATIVA.");

        // ATUALIZADO: Agora deve ter 2 perfis (LEAD e CLIENTE)
        assertEquals(2, pessoaSalva.getPerfis().size(), "A pessoa deve ter 2 perfis (LEAD e CLIENTE).");
        assertTrue(pessoaSalva.getPerfis().stream().anyMatch(pp -> "LEAD".equals(pp.getPerfil().getNomePerfil())),
                "Deve ter o perfil LEAD.");
        assertTrue(pessoaSalva.getPerfis().stream().anyMatch(pp -> "CLIENTE".equals(pp.getPerfil().getNomePerfil())),
                "Deve ter o perfil CLIENTE.");

        // Validação do campo isCliente (nova regra)
        assertTrue(pessoaSalva.isCliente(), "O campo isCliente deve ser TRUE.");

        String tokenKey = "activate:" + pessoaSalva.getId();
        String tokenNoRedis = redisTemplate.opsForValue().get(tokenKey);
        assertNotNull(tokenNoRedis, "Um token de ativação deve existir no Redis.");

        verify(emailService).enviarEmailAtivacao(eq("teste@email.com"), eq(tokenNoRedis));
    }

    // --- Teste 2: Caminho Triste (Sad Path) ---

    @Test
    void deveLancarExcecaoParaEmailDuplicado() {
        // 1. ARRANGE
        // (O @BeforeEach já rodou e limpou o banco)
        CadastroSimplificadoRequest request1 = new CadastroSimplificadoRequest("teste@email.com", "senha123");
        cadastroPessoaService.cadastrarPessoaSimplificado(request1); // Cria a primeira pessoa

        // Agora, criamos uma *segunda* requisição com o MESMO e-mail
        CadastroSimplificadoRequest requestDuplicada = new CadastroSimplificadoRequest("teste@email.com", "outraSenha");

        // 2. ACT & 3. ASSERT
        IllegalArgumentException excecao = assertThrows(IllegalArgumentException.class, () -> {
            cadastroPessoaService.cadastrarPessoaSimplificado(requestDuplicada); // Tenta criar a segunda
        });

        assertEquals("Erro de Cadastro: O e-mail já está em uso.", excecao.getMessage());
    }
}
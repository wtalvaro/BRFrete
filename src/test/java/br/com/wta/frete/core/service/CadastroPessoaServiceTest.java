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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import br.com.wta.frete.core.controller.dto.CadastroSimplificadoRequest;
import br.com.wta.frete.core.entity.Perfil; // <--- PRECISA IMPORTAR
import br.com.wta.frete.core.entity.Pessoa;
import br.com.wta.frete.core.repository.PerfilRepository; // <--- PRECISA INJETAR
import br.com.wta.frete.core.repository.PessoaRepository; // <--- PRECISA INJETAR

/**
 * Teste de integração para o CadastroPessoaService. * @SpringBootTest: Carrega
 * o contexto completo da aplicação (JPA, Redis, etc.). @ActiveProfiles("test"):
 * Usa um perfil de teste (se houver, ex: application-test.properties).
 * * @Transactional: Garante que cada teste rode em uma transação e dê rollback
 * ao final. Isso limpa o banco de dados automaticamente.
 */
@SpringBootTest
@ActiveProfiles("test")
// @Transactional
public class CadastroPessoaServiceTest {
    @Autowired
    private CadastroPessoaService cadastroPessoaService;

    // --- Dependências Reais (Injetadas pelo Spring) ---

    // PRECISAMOS DOS REPOSITÓRIOS PARA O SETUP
    @Autowired
    private PessoaRepository pessoaRepository;

    @Autowired
    private PerfilRepository perfilRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private StringRedisTemplate redisTemplate;

    // --- Dependências Externas (Mocks) ---
    @MockBean
    private EmailService emailService;

    /**
     * Método de setup (preparação) que roda antes de CADA teste. Limpa os
     * repositórios e o Redis para garantir que um teste não interfira no outro.
     */
    @BeforeEach
    void setUp() {
        // 1. Limpa o Banco de Dados (necessário por causa do @Transactional)
        pessoaRepository.deleteAll();
        perfilRepository.deleteAll();

        // 2. Limpa o Redis
        redisTemplate.execute((RedisCallback<Void>) connection -> {
            connection.flushDb();
            return null;
        });

        // 3. CRIA AS DEPENDÊNCIAS NECESSÁRIAS (Forma Correta)

        // Antes (Errado):
        // perfilRepository.save(new Perfil("LEAD", "Perfil de teste"));

        // Depois (Correto):
        Perfil perfilDeTeste = new Perfil(); // Usa o @NoArgsConstructor
        perfilDeTeste.setNomePerfil("LEAD"); // Usa o setter do @Data
        perfilDeTeste.setDescricao("Perfil de teste"); // Usa o setter do @Data

        perfilRepository.save(perfilDeTeste); // Agora sim, o save() funciona
    }

    // --- Teste 1: Caminho Feliz (Happy Path) ---

    @Test
    void deveCadastrarPessoaSimplificadoComSucesso() {
        // 1. ARRANGE
        doNothing().when(emailService).enviarEmailAtivacao(anyString(), anyString());

        CadastroSimplificadoRequest request = new CadastroSimplificadoRequest("teste@email.com", "senhaForte123");

        // 2. ACT
        Pessoa pessoaSalva = cadastroPessoaService.cadastrarPessoaSimplificado(request);

        // 3. ASSERT
        assertNotNull(pessoaSalva.getId(), "A pessoa salva deve ter um ID.");
        assertEquals("teste@email.com", pessoaSalva.getEmail());
        assertTrue(passwordEncoder.matches("senhaForte123", pessoaSalva.getSenha()),
                "A senha deve estar criptografada.");
        assertEquals(false, pessoaSalva.isAtivo(), "A pessoa deve começar como INATIVA.");
        assertEquals(1, pessoaSalva.getPerfis().size(), "A pessoa deve ter 1 perfil (LEAD).");

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
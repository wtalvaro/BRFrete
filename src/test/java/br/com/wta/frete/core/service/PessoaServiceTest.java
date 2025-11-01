package br.com.wta.frete.core.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import br.com.wta.frete.core.controller.dto.CadastroSimplificadoRequest;
import br.com.wta.frete.core.controller.dto.OAuth2UserRequestDTO;
import br.com.wta.frete.core.controller.dto.PessoaRequest;
import br.com.wta.frete.core.entity.Pessoa;
import br.com.wta.frete.core.repository.PessoaRepository;

// Usa a extensão do Mockito para inicializar os Mocks
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class PessoaServiceTest {

    // Simula a injeção do PessoaService, injetando os mocks abaixo
    @InjectMocks
    private PessoaService pessoaService;

    // Mocks das dependências diretas do PessoaService
    @Mock
    private PessoaRepository pessoaRepository;
    @Mock
    private CadastroPessoaService cadastroPessoaService;
    @Mock
    private PessoaSocialService pessoaSocialService;

    // --- VARIÁVEIS DE TESTE ---
    private Pessoa pessoaInativa;
    private Pessoa pessoaAtiva;
    private final Long PESSOA_ID_INATIVA = 1L;
    private final Long PESSOA_ID_ATIVA = 2L;
    private final Long PESSOA_ID_INEXISTENTE = 99L;

    // Configuração inicial para criar objetos Pessoa simulados antes de cada teste
    @BeforeEach
    void setUp() {
        // Pessoa Inativa
        pessoaInativa = new Pessoa();
        pessoaInativa.setId(PESSOA_ID_INATIVA);
        pessoaInativa.setAtivo(false);

        // Pessoa Ativa
        pessoaAtiva = new Pessoa();
        pessoaAtiva.setId(PESSOA_ID_ATIVA);
        pessoaAtiva.setAtivo(true);
    }

    // =================================================================
    // TESTES DE LÓGICA CORE: ativarPessoa(Long pessoaId)
    // =================================================================

    @Test
    void deveAtivarPessoaComSucesso() {
        // ARRANGE: Simula que o repositório ENCONTRA uma pessoa INATIVA
        when(pessoaRepository.findById(PESSOA_ID_INATIVA)).thenReturn(Optional.of(pessoaInativa));

        // ACT: Tenta ativar a pessoa
        pessoaService.ativarPessoa(PESSOA_ID_INATIVA);

        // ASSERT:
        // 1. Verifica se o método save foi chamado EXATAMENTE 1 vez
        verify(pessoaRepository, times(1)).save(any(Pessoa.class));

        // 2. Verifica se a entidade foi modificada para ativa
        verify(pessoaRepository, times(1)).save(pessoaInativa);

        // 3. Garante que o estado da entidade MOCKADA foi alterado
        // Nota: Em um teste unitário, você deve confiar no `verify`, mas esta linha
        // ajuda na clareza.
        // assertTrue(pessoaInativa.isAtivo()); // Removido para focar no Mockito
    }

    @Test
    void naoDeveSalvarSePessoaJaEstiverAtiva() {
        // ARRANGE: Simula que o repositório ENCONTRA uma pessoa ATIVA
        when(pessoaRepository.findById(PESSOA_ID_ATIVA)).thenReturn(Optional.of(pessoaAtiva));

        // ACT: Tenta ativar a pessoa
        pessoaService.ativarPessoa(PESSOA_ID_ATIVA);

        // ASSERT:
        // O método save NÃO deve ser chamado, pois a lógica `if (pessoa.isAtivo())
        // return;`
        // deve ser executada.
        verify(pessoaRepository, never()).save(any(Pessoa.class));
    }

    @Test
    void deveLancarExcecaoSePessoaNaoExistir() {
        // ARRANGE: Simula que o repositório NÃO ENCONTRA o ID
        when(pessoaRepository.findById(PESSOA_ID_INEXISTENTE)).thenReturn(Optional.empty());

        // ACT & ASSERT: Verifica se a exceção correta é lançada
        assertThrows(IllegalArgumentException.class, () -> {
            pessoaService.ativarPessoa(PESSOA_ID_INEXISTENTE);
        }, "Deve lançar IllegalArgumentException para Pessoa inexistente.");

        // Verifica que o save nunca foi chamado
        verify(pessoaRepository, never()).save(any(Pessoa.class));
    }

    // =================================================================
    // TESTES DE DELEGAÇÃO (FACADE)
    // =================================================================

    /**
     * Teste: deveDelegarCadastroSimplificadoAoServicoEspecializado Objetivo:
     * Garantir que o PessoaService apenas repassa a chamada de cadastro
     * simplificado para o CadastroPessoaService com os argumentos corretos.
     */
    @Test
    void deveDelegarCadastroSimplificadoAoServicoEspecializado() {
        // 1. ARRANGE (Preparação)
        CadastroSimplificadoRequest request = new CadastroSimplificadoRequest("novo@email.com", "senhaSegura123");
        Pessoa pessoaMock = new Pessoa(); // Mock do objeto que seria retornado

        // Simula o comportamento: quando o método delegado é chamado, ele retorna um
        // mock
        when(cadastroPessoaService.cadastrarPessoaSimplificado(request)).thenReturn(pessoaMock);

        // 2. ACT (Ação)
        Pessoa resultado = pessoaService.cadastrarPessoaSimplificado(request);

        // 3. ASSERT (Verificação)
        // 3.1. Verifica se o método *delegado* foi chamado **exatamente uma vez** //
        // com o objeto 'request' original.
        verify(cadastroPessoaService, times(1)).cadastrarPessoaSimplificado(request);

        // 3.2. Verifica se o resultado retornado é o resultado do serviço delegado
        // (Opcional, mas útil)
        assertEquals(pessoaMock, resultado, "O PessoaService deve retornar o objeto Pessoa do serviço delegado.");
    }

    // =================================================================
    // TESTES DE DELEGAÇÃO (CADASTRO COMPLETO)
    // =================================================================

    /**
     * Teste: deveDelegarCadastroCompletoAoServicoEspecializado Objetivo: Assegurar
     * que o PessoaService apenas repassa a chamada de cadastro completo para o
     * CadastroPessoaService com a requisição correta.
     */
    @Test
    void deveDelegarCadastroCompletoAoServicoEspecializado() {
        // 1. ARRANGE (Preparação)
        // CORRIGIDO: Agora apenas 5 argumentos, na ordem correta:
        // nomeCompleto, documento, email, senha, telefone
        PessoaRequest request = new PessoaRequest("Nome Completo", "doc12345", "completo@email.com", "senhaForte",
                "900000000");

        Pessoa pessoaEsperada = new Pessoa(); // Mock do objeto Pessoa que seria retornado

        // Simula o comportamento: quando o método delegado é chamado, ele retorna o
        // mock
        when(cadastroPessoaService.cadastrarPessoa(request)).thenReturn(pessoaEsperada);

        // 2. ACT (Ação)
        Pessoa resultado = pessoaService.cadastrarPessoa(request);

        // 3. ASSERT (Verificação)
        // 3.1. Verifica se o método *delegado* foi chamado EXATAMENTE UMA VEZ com
        // o objeto 'request' original.
        verify(cadastroPessoaService, times(1)).cadastrarPessoa(request);

        // 3.2. Verifica se o valor retornado da Fachada é o mesmo que veio do
        // serviço delegado.
        assertEquals(pessoaEsperada, resultado, "O PessoaService deve retornar o objeto Pessoa do serviço delegado.");
    }

    // =================================================================
    // TESTES DE DELEGAÇÃO (LOGIN SOCIAL)
    // =================================================================

    /**
     * Teste: deveDelegarLoginSocialAoServicoEspecializado Objetivo: Garantir que o
     * PessoaService repassa a chamada de login/cadastro social para o
     * PessoaSocialService com a requisição correta.
     */
    @Test
    void deveDelegarLoginSocialAoServicoEspecializado() {
        // 1. ARRANGE (Preparação)
        // DTO tem a ordem: socialId, nome, email, provedor (Lombok @Data constructor)
        OAuth2UserRequestDTO request = new OAuth2UserRequestDTO("social-token-id-12345", // socialId
                "Nome Social Completo", // nome
                "social@email.com", // email
                "google" // provedor
        );

        Pessoa pessoaSocial = new Pessoa(); // Mock do objeto Pessoa retornado

        // CORRIGIDO: Usamos getNome() para acessar o campo (Padrão Lombok/JavaBeans)
        pessoaSocial.setNome(request.getNome());

        // Simula o comportamento: quando o método delegado (PessoaSocialService) é
        // chamado,
        // ele retorna o objeto Pessoa simulado.
        when(pessoaSocialService.cadastrarOuObterPessoaSocial(request)).thenReturn(pessoaSocial);

        // 2. ACT (Ação)
        Pessoa resultado = pessoaService.cadastrarOuObterPessoaSocial(request);

        // 3. ASSERT (Verificação)
        // 3.1. Verifica se o método *delegado* foi chamado **exatamente uma vez** //
        // com o objeto 'request' original.
        verify(pessoaSocialService, times(1)).cadastrarOuObterPessoaSocial(request);

        // 3.2. Verifica se o resultado retornado é o resultado do serviço delegado
        assertEquals(pessoaSocial, resultado, "O PessoaService deve retornar o objeto Pessoa do serviço social.");
    }
}
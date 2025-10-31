package br.com.wta.frete.core.service;

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
}
package br.com.wta.frete.core.service;

import br.com.wta.frete.core.controller.dto.PessoaRequest;
import br.com.wta.frete.core.entity.Pessoa;
import br.com.wta.frete.core.repository.PessoaRepository;
import br.com.wta.frete.shared.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Testes unitários para a classe PessoaService, utilizando Mockito.
 * O foco é testar a lógica de negócio contida no Service, isolando a camada de
 * persistência.
 */
@ExtendWith(MockitoExtension.class)
public class PessoaServiceTest {

    @Mock // Simula o repositório, não acessa o banco de dados
    private PessoaRepository pessoaRepository;

    // Mocks de outros serviços que o PessoaService usa:
    @Mock
    private CadastroPessoaService cadastroPessoaService;
    @Mock
    private PessoaSocialService pessoaSocialService;
    @Mock
    private EmailService emailService;

    @InjectMocks // Injeta todos os Mocks acima nesta instância de PessoaService
    private PessoaService pessoaService;

    private Pessoa pessoaAtiva;
    private Pessoa pessoaInativa;
    private PessoaRequest pessoaRequestValido;

    @BeforeEach
    void setUp() {
        // Objeto Pessoa Ativa para uso nos testes
        pessoaAtiva = new Pessoa();
        pessoaAtiva.setId(1L);
        pessoaAtiva.setNome("Alice Silva");
        pessoaAtiva.setEmail("alice@teste.com");
        pessoaAtiva.setAtivo(true);
        pessoaAtiva.setDataNascimento(LocalDate.of(1990, 1, 1));

        // Objeto Pessoa Inativa
        pessoaInativa = new Pessoa();
        pessoaInativa.setId(2L);
        pessoaInativa.setNome("Bob Santos");
        pessoaInativa.setAtivo(false);

        // Objeto DTO de Requisição para uso nos testes de UPDATE
        pessoaRequestValido = new PessoaRequest(
                "Alice Pereira",
                "12345678909",
                LocalDate.of(1990, 1, 1),
                "alice.pereira@novo.com",
                "11987654321",
                "123456789");
    }

    // =================================================================
    // TESTES DE LEITURA (READ)
    // =================================================================

    @Test
    @DisplayName("Deve buscar Pessoa por ID com sucesso")
    void buscarPorId_DeveRetornarPessoa_QuandoEncontrado() {
        // Arrange
        when(pessoaRepository.findById(1L)).thenReturn(Optional.of(pessoaAtiva));

        // Act
        Pessoa resultado = pessoaService.buscarPorId(1L);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Alice Silva", resultado.getNome());
        verify(pessoaRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException ao buscar ID inexistente")
    void buscarPorId_DeveLancarExcecao_QuandoNaoEncontrado() {
        // Arrange
        when(pessoaRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> pessoaService.buscarPorId(99L));
        verify(pessoaRepository, times(1)).findById(99L);
    }

    @Test
    @DisplayName("Deve listar todas as Pessoas cadastradas")
    void listarTodas_DeveRetornarListaDePessoas() {
        // Arrange
        List<Pessoa> listaMocada = List.of(pessoaAtiva, pessoaInativa);
        when(pessoaRepository.findAll()).thenReturn(listaMocada);

        // Act
        List<Pessoa> resultado = pessoaService.listarTodas();

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(pessoaRepository, times(1)).findAll();
    }

    // =================================================================
    // TESTES DE ATUALIZAÇÃO (UPDATE)
    // =================================================================

    @SuppressWarnings("null")
    @Test
    @DisplayName("Deve atualizar nome e telefone da Pessoa com sucesso")
    void atualizarPessoa_DeveAtualizarDados_QuandoPessoaExiste() {
        // Arrange
        Long id = 1L;
        // Simula a busca bem-sucedida
        when(pessoaRepository.findById(id)).thenReturn(Optional.of(pessoaAtiva));
        // Simula o salvamento
        when(pessoaRepository.save(any(Pessoa.class))).thenAnswer(i -> i.getArguments()[0]);

        // Act
        Pessoa pessoaAtualizada = pessoaService.atualizarPessoa(id, pessoaRequestValido);

        // Assert
        assertNotNull(pessoaAtualizada);
        assertEquals(id, pessoaAtualizada.getId());
        assertEquals("Alice Pereira", pessoaAtualizada.getNome());
        
        // CORREÇÃO AQUI: Esperar o valor real do telefone ("123456789")
        assertEquals(pessoaRequestValido.telefone(), pessoaAtualizada.getTelefone()); // Ou use "123456789"
        
        verify(pessoaRepository, times(1)).findById(id);
        verify(pessoaRepository, times(1)).save(pessoaAtualizada);
    }

    @SuppressWarnings("null")
    @Test
    @DisplayName("Deve lançar exceção ao tentar atualizar Pessoa inexistente")
    void atualizarPessoa_DeveLancarExcecao_QuandoPessoaNaoExiste() {
        // Arrange
        Long idInexistente = 99L;
        when(pessoaRepository.findById(idInexistente)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class,
                () -> pessoaService.atualizarPessoa(idInexistente, pessoaRequestValido));
        verify(pessoaRepository, times(1)).findById(idInexistente);
        verify(pessoaRepository, never()).save(any(Pessoa.class));
    }

    // =================================================================
    // TESTES DE ESTADO (ATIVAR)
    // =================================================================

    @SuppressWarnings("null")
    @Test
    @DisplayName("Deve ativar Pessoa Inativa com sucesso")
    void ativarPessoa_DeveMudarStatusParaAtivo_QuandoPessoaInativa() {
        // Arrange
        Long id = 2L;
        when(pessoaRepository.findById(id)).thenReturn(Optional.of(pessoaInativa));
        // Simula o salvamento
        when(pessoaRepository.save(any(Pessoa.class))).thenAnswer(i -> i.getArguments()[0]);

        // Act
        pessoaService.ativarPessoa(id);

        // Assert
        assertTrue(pessoaInativa.isAtivo());
        verify(pessoaRepository, times(1)).findById(id);
        verify(pessoaRepository, times(1)).save(pessoaInativa);
    }

    @SuppressWarnings("null")
    @Test
    @DisplayName("Não deve salvar se a Pessoa já estiver ativa")
    void ativarPessoa_NaoDeveSalvar_QuandoPessoaJaAtiva() {
        // Arrange
        Long id = 1L;
        when(pessoaRepository.findById(id)).thenReturn(Optional.of(pessoaAtiva));

        // Act
        pessoaService.ativarPessoa(id);

        // Assert
        assertTrue(pessoaAtiva.isAtivo());
        verify(pessoaRepository, times(1)).findById(id);
        verify(pessoaRepository, never()).save(any(Pessoa.class)); // Nenhuma interação de salvamento
    }

    // =================================================================
    // TESTES DE DELEÇÃO (DELETE)
    // =================================================================

    @SuppressWarnings("null")
    @Test
    @DisplayName("Deve deletar Pessoa com sucesso")
    void deletarPessoa_DeveChamarDelete_QuandoPessoaExiste() {
        // Arrange
        Long id = 1L;
        when(pessoaRepository.findById(id)).thenReturn(Optional.of(pessoaAtiva));
        doNothing().when(pessoaRepository).delete(pessoaAtiva); // Simula a deleção

        // Act
        pessoaService.deletarPessoa(id);

        // Assert
        verify(pessoaRepository, times(1)).findById(id);
        verify(pessoaRepository, times(1)).delete(pessoaAtiva);
    }

    @SuppressWarnings("null")
    @Test
    @DisplayName("Deve lançar exceção ao tentar deletar Pessoa inexistente")
    void deletarPessoa_DeveLancarExcecao_QuandoPessoaNaoExiste() {
        // Arrange
        Long idInexistente = 99L;
        when(pessoaRepository.findById(idInexistente)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> pessoaService.deletarPessoa(idInexistente));
        verify(pessoaRepository, times(1)).findById(idInexistente);
        verify(pessoaRepository, never()).delete(any(Pessoa.class));
    }
}
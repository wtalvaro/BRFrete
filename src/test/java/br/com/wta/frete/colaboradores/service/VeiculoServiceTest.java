package br.com.wta.frete.colaboradores.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
// Inclui spy() e verify()
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.wta.frete.colaboradores.controller.dto.VeiculoRequest;
import br.com.wta.frete.colaboradores.controller.dto.VeiculoResponse;
import br.com.wta.frete.colaboradores.entity.Transportador;
import br.com.wta.frete.colaboradores.entity.Veiculo;
import br.com.wta.frete.colaboradores.entity.enums.StatusVeiculo;
import br.com.wta.frete.colaboradores.entity.enums.TipoVeiculo;
import br.com.wta.frete.colaboradores.repository.TransportadorRepository;
import br.com.wta.frete.colaboradores.repository.VeiculoRepository;
import br.com.wta.frete.colaboradores.service.mapper.VeiculoMapper;
import br.com.wta.frete.shared.exception.InvalidDataException;
import br.com.wta.frete.shared.exception.ResourceNotFoundException;

/**
 * Testes Unitários para a classe de serviço VeiculoService.
 * Objetivo: Validar todas as regras de negócio do cadastro de veículos.
 */
@ExtendWith(MockitoExtension.class)
class VeiculoServiceTest {

    @InjectMocks
    private VeiculoService veiculoService;

    @Mock
    private VeiculoRepository veiculoRepository;

    @Mock
    private TransportadorRepository transportadorRepository;

    @Mock
    private VeiculoMapper veiculoMapper;

    // Dados de teste comuns
    private VeiculoRequest veiculoRequest;
    private Transportador transportador;
    private Veiculo veiculo; // Agora será um Spy
    private Veiculo veiculoSalvo;
    private VeiculoResponse veiculoResponse;

    /**
     * Configuração inicial (Setup) antes de cada teste.
     * Define os objetos de mock comuns para evitar repetição.
     */
    @BeforeEach
    void setUp() {
        // Mock de entrada (Request DTO)
        veiculoRequest = new VeiculoRequest(
                1L, // 1. transportadorPessoaId
                "ABC1234", // 2. placa
                "12345678901", // 3. renavam
                TipoVeiculo.VUC, // 4. tipoVeiculo
                2020, // 5. anoFabricacao
                new BigDecimal("5000.00"), // 6. capacidadeKg
                new BigDecimal("30.00"), // 7. capacidadeM3
                true, // 8. possuiRastreador
                StatusVeiculo.DISPONIVEL // 9. statusVeiculo
        );

        // Mock de Entidade e Transportador
        transportador = new Transportador();
        transportador.setPessoaId(10L);

        // CORREÇÃO ESSENCIAL: Cria o Veiculo como um SPY.
        veiculo = spy(new Veiculo());
        veiculo.setPlaca(veiculoRequest.placa());
        veiculo.setRenavam(veiculoRequest.renavam());
        // REMOVIDO: veiculo.setTransportador(transportador); -> Quem faz isso é o
        // Serviço, e é o que queremos verificar.

        veiculoSalvo = new Veiculo();
        veiculoSalvo.setId(1);
        veiculoSalvo.setPlaca(veiculoRequest.placa());
        veiculoSalvo.setRenavam(veiculoRequest.renavam());
        veiculoSalvo.setTransportador(transportador);

        // Mock de Saída (Response DTO)
        veiculoResponse = new VeiculoResponse(
                1, // veiculoId
                transportador.getPessoaId(),
                veiculoRequest.placa(),
                veiculoRequest.renavam(),
                veiculoRequest.tipoVeiculo(),
                veiculoRequest.anoFabricacao(),
                veiculoRequest.capacidadeKg(),
                veiculoRequest.capacidadeM3(),
                veiculoRequest.possuiRastreador(),
                StatusVeiculo.DISPONIVEL);
    }

    // =======================================================================
    // Teste de Sucesso
    // =======================================================================

    @SuppressWarnings("null")
    @Test
    @DisplayName("Deve cadastrar um veículo com sucesso quando dados válidos forem fornecidos")
    void cadastrarVeiculo_Success() {
        // CENÁRIO (GIVEN)
        when(transportadorRepository.findByPessoaId(veiculoRequest.transportadorPessoaId()))
                .thenReturn(Optional.of(transportador));
        when(veiculoRepository.findByPlaca(veiculoRequest.placa())).thenReturn(Optional.empty());
        when(veiculoRepository.findByRenavam(veiculoRequest.renavam())).thenReturn(Optional.empty());
        // O Mapper retorna o objeto veiculo (que agora é um SPY)
        when(veiculoMapper.toEntity(veiculoRequest)).thenReturn(veiculo);
        when(veiculoRepository.save(any(Veiculo.class))).thenReturn(veiculoSalvo);
        when(veiculoMapper.toResponse(veiculoSalvo)).thenReturn(veiculoResponse);

        // AÇÃO (WHEN)
        VeiculoResponse resultado = veiculoService.cadastrarVeiculo(veiculoRequest);

        // VERIFICAÇÃO (THEN)
        assertNotNull(resultado, "A resposta do veículo não deve ser nula.");
        assertEquals(veiculoResponse.veiculoId(), resultado.veiculoId(),
                "O ID do veículo retornado deve ser o ID salvo.");

        // VERIFICADO NO SPY: Garante que o serviço associou o Transportador à entidade
        verify(veiculo, times(1)).setTransportador(transportador); // Agora funciona porque 'veiculo' é um SPY

        // Garante que o repositório salvou
        verify(veiculoRepository, times(1)).save(veiculo);

        // Garante que as validações ocorreram
        verify(veiculoRepository, times(1)).findByPlaca(veiculoRequest.placa());
        verify(veiculoRepository, times(1)).findByRenavam(veiculoRequest.renavam());
    }

    // =======================================================================
    // Testes de Falha - Regras de Negócio
    // =======================================================================

    @SuppressWarnings("null")
    @Test
    @DisplayName("Deve lançar ResourceNotFoundException quando o Transportador não for encontrado")
    void cadastrarVeiculo_TransportadorNotFound_ThrowsException() {
        // CENÁRIO (GIVEN)
        when(transportadorRepository.findByPessoaId(veiculoRequest.transportadorPessoaId()))
                .thenReturn(Optional.empty());

        // AÇÃO & VERIFICAÇÃO (WHEN & THEN)
        ResourceNotFoundException excecao = assertThrows(
                ResourceNotFoundException.class,
                () -> veiculoService.cadastrarVeiculo(veiculoRequest),
                "Deveria lançar ResourceNotFoundException quando o Transportador não existir.");

        // VERIFICAÇÃO ADICIONAL
        assertTrue(excecao.getMessage().contains("Transportador não encontrado"),
                "A mensagem da exceção deve indicar Transportador não encontrado.");

        verify(veiculoRepository, never()).findByPlaca(anyString());
        verify(veiculoRepository, never()).save(any(Veiculo.class));
    }

    @SuppressWarnings("null")
    @Test
    @DisplayName("Deve lançar InvalidDataException quando a Placa já estiver cadastrada")
    void cadastrarVeiculo_PlacaAlreadyExists_ThrowsException() {
        // CENÁRIO (GIVEN)
        when(transportadorRepository.findByPessoaId(veiculoRequest.transportadorPessoaId()))
                .thenReturn(Optional.of(transportador));
        when(veiculoRepository.findByPlaca(veiculoRequest.placa())).thenReturn(Optional.of(veiculo));

        // AÇÃO & VERIFICAÇÃO (WHEN & THEN)
        InvalidDataException excecao = assertThrows(
                InvalidDataException.class,
                () -> veiculoService.cadastrarVeiculo(veiculoRequest),
                "Deveria lançar InvalidDataException quando a Placa já estiver em uso.");

        // VERIFICAÇÃO ADICIONAL
        // CORRIGIDO: usa getReasonCode() para obter a mensagem amigável
        assertEquals("A placa informada já está em uso.", excecao.getReasonCode(),
                "A mensagem amigável deve ser 'A placa informada já está em uso.'.");

        verify(veiculoRepository, never()).findByRenavam(anyString());
        verify(veiculoRepository, never()).save(any(Veiculo.class));
    }

    @SuppressWarnings("null")
    @Test
    @DisplayName("Deve lançar InvalidDataException quando o Renavam já estiver cadastrado")
    void cadastrarVeiculo_RenavamAlreadyExists_ThrowsException() {
        // CENÁRIO (GIVEN)
        when(transportadorRepository.findByPessoaId(veiculoRequest.transportadorPessoaId()))
                .thenReturn(Optional.of(transportador));
        when(veiculoRepository.findByPlaca(veiculoRequest.placa())).thenReturn(Optional.empty());
        when(veiculoRepository.findByRenavam(veiculoRequest.renavam())).thenReturn(Optional.of(veiculo));

        // AÇÃO & VERIFICAÇÃO (WHEN & THEN)
        InvalidDataException excecao = assertThrows(
                InvalidDataException.class,
                () -> veiculoService.cadastrarVeiculo(veiculoRequest),
                "Deveria lançar InvalidDataException quando o Renavam já estiver em uso.");

        // VERIFICAÇÃO ADICIONAL
        // CORRIGIDO: usa getReasonCode() para obter a mensagem amigável
        assertEquals("O Renavam informado já está em uso.", excecao.getReasonCode(),
                "A mensagem amigável deve ser 'O Renavam informado já está em uso.'.");

        verify(veiculoRepository, times(1)).findByPlaca(veiculoRequest.placa());
        verify(veiculoRepository, times(1)).findByRenavam(veiculoRequest.renavam());
        verify(veiculoRepository, never()).save(any(Veiculo.class));
    }
}
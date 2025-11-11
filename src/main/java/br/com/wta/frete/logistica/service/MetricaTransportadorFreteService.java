// Caminho: src/main/java/br/com/wta/frete/logistica/service/MetricaTransportadorFreteService.java
package br.com.wta.frete.logistica.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.wta.frete.colaboradores.controller.dto.MetricaTransportadorFreteRequest;
import br.com.wta.frete.colaboradores.controller.dto.MetricaTransportadorFreteResponse;
import br.com.wta.frete.colaboradores.entity.MetricaTransportadorFrete;
import br.com.wta.frete.colaboradores.entity.Transportador;
import br.com.wta.frete.colaboradores.repository.MetricaTransportadorFreteRepository;
import br.com.wta.frete.colaboradores.repository.TransportadorRepository;
import br.com.wta.frete.colaboradores.service.mapper.MetricaTransportadorFreteMapper;
import br.com.wta.frete.logistica.entity.ModalidadeFrete;
import br.com.wta.frete.logistica.repository.ModalidadeFreteRepository;
import br.com.wta.frete.shared.exception.InvalidDataException;
import br.com.wta.frete.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;

/**
 * Serviço responsável pela regra de negócio e gestão do ciclo de vida das
 * Métricas de Precificação Customizadas. Inclui a lógica de cálculo de custo e
 * os métodos CRUD para o Controller.
 */
@Service
@RequiredArgsConstructor
public class MetricaTransportadorFreteService {

    private final MetricaTransportadorFreteRepository metricaRepository;
    private final ModalidadeFreteRepository modalidadeRepository;
    private final TransportadorRepository transportadorRepository;
    private final MetricaTransportadorFreteMapper metricaMapper;

    private static final int CASAS_DECIMAIS = 2;
    private static final RoundingMode MODO_ARREDONDAMENTO = RoundingMode.HALF_UP;

    // --------------------------------------------------------------------------
    // MÉTODOS DE CÁLCULO (Lógica de Custo)
    // --------------------------------------------------------------------------

    @Transactional(readOnly = true)
    public BigDecimal calcularCustoPersonalizado(Long transportadorId, String nomeModalidade, BigDecimal distanciaKm) {

        // 1. OBTÉM A MODALIDADE ID
        ModalidadeFrete modalidade = modalidadeRepository.findByNomeModalidade(nomeModalidade)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Modalidade de Frete não encontrada: " + nomeModalidade));

        // CORREÇÃO 1: Usar getId() para a entidade ModalidadeFrete
        Integer modalidadeId = modalidade.getId();

        // 2. BUSCA A MÉTRICA CUSTOMIZADA
        // CORREÇÃO 2: Mudar a convenção de nome do método JPA
        MetricaTransportadorFrete metrica = metricaRepository
                .findByTransportadorPessoaIdAndModalidadeFreteId(transportadorId, modalidadeId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "O transportador " + transportadorId
                                + " não possui parâmetros de precificação customizados (Métrica) para a modalidade "
                                + nomeModalidade));

        // 3. APLICA A FÓRMULA DE CUSTO PERSONALIZADO
        BigDecimal custoFixo = metrica.getCustoFixoViagem();
        BigDecimal custoPorKm = metrica.getCustoPorKm();
        BigDecimal margemLucro = metrica.getMargemLucro();

        BigDecimal custoVariavel = custoPorKm.multiply(distanciaKm);
        BigDecimal custoBasico = custoFixo.add(custoVariavel);
        BigDecimal fatorMargem = BigDecimal.ONE.add(margemLucro);
        BigDecimal custoFinal = custoBasico.multiply(fatorMargem);

        // 4. RETORNO (Arredondado)
        return custoFinal.setScale(CASAS_DECIMAIS, MODO_ARREDONDAMENTO);
    }

    // --------------------------------------------------------------------------
    // MÉTODOS CRUD (SOLUÇÃO PARA O ERRO DE COMPILAÇÃO)
    // --------------------------------------------------------------------------

    @Transactional
    public MetricaTransportadorFreteResponse criarMetrica(MetricaTransportadorFreteRequest request) {

        validarTransportadorExiste(request.transportadorPessoaId());
        validarUnicidade(request.transportadorPessoaId(), request.nomeMetrica(), null);

        Transportador transportador = buscarTransportador(request.transportadorPessoaId());
        ModalidadeFrete modalidade = buscarModalidade(request.modalidadeFreteId());

        MetricaTransportadorFrete novaMetrica = metricaMapper.toEntity(request);
        novaMetrica.setTransportador(transportador);
        novaMetrica.setModalidadeFrete(modalidade);

        MetricaTransportadorFrete metricaSalva = metricaRepository.save(novaMetrica);

        return metricaMapper.toResponse(metricaSalva);
    }

    @Transactional(readOnly = true)
    public MetricaTransportadorFreteResponse buscarPorId(Long id) {
        MetricaTransportadorFrete metrica = buscarMetricaPorId(id);
        return metricaMapper.toResponse(metrica);
    }

    @Transactional(readOnly = true)
    public List<MetricaTransportadorFreteResponse> buscarPorTransportador(Long transportadorId) {
        validarTransportadorExiste(transportadorId);

        // CORREÇÃO 3: Mudar a convenção de nome do método JPA
        List<MetricaTransportadorFrete> metricas = metricaRepository.findByTransportadorPessoaId(transportadorId);

        return metricas.stream()
                .map(metricaMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public MetricaTransportadorFreteResponse atualizarMetrica(Long id, MetricaTransportadorFreteRequest request) {

        MetricaTransportadorFrete metricaExistente = buscarMetricaPorId(id);

        validarUnicidade(request.transportadorPessoaId(), request.nomeMetrica(), id);

        ModalidadeFrete novaModalidade = buscarModalidade(request.modalidadeFreteId());
        metricaExistente.setModalidadeFrete(novaModalidade);

        // CORREÇÃO 4: Mudar o nome do método do Mapper
        metricaMapper.updateEntityFromRequest(request, metricaExistente);

        MetricaTransportadorFrete metricaAtualizada = metricaRepository.save(metricaExistente);

        return metricaMapper.toResponse(metricaAtualizada);
    }

    @SuppressWarnings("null")
    @Transactional
    public void deletarMetrica(Long id) {
        MetricaTransportadorFrete metrica = buscarMetricaPorId(id);
        metricaRepository.delete(metrica);
    }

    // --------------------------------------------------------------------------
    // MÉTODOS AUXILIARES PRIVADOS
    // --------------------------------------------------------------------------

    private MetricaTransportadorFrete buscarMetricaPorId(Long id) {
        return metricaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Métrica de Frete não encontrada com ID: " + id));
    }

    @SuppressWarnings("null")
    private Transportador buscarTransportador(Long id) {
        // Assume que existe um TransportadorRepository injetado
        return transportadorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transportador não encontrado com ID: " + id));
    }

    private ModalidadeFrete buscarModalidade(Long id) {
        // Assume que ModalidadeFrete tem um ID Integer na PK
        return modalidadeRepository.findById(id.intValue())
                .orElseThrow(() -> new ResourceNotFoundException("Modalidade de Frete não encontrada com ID: " + id));
    }

    @SuppressWarnings("null")
    private void validarTransportadorExiste(Long transportadorId) {
        if (!transportadorRepository.existsById(transportadorId)) {
            throw new ResourceNotFoundException("Transportador não encontrado com ID: " + transportadorId);
        }
    }

    private void validarUnicidade(Long transportadorId, String nomeMetrica, Long idAtual) {
        // CORREÇÃO 5: Mudar a convenção de nome do método JPA
        Optional<MetricaTransportadorFrete> existente = metricaRepository
                .findByTransportadorPessoaIdAndNomeMetrica(transportadorId, nomeMetrica);

        if (existente.isPresent() && (idAtual == null || !existente.get().getMetricaId().equals(idAtual))) {
            // CORREÇÃO 6: Adicionar o código de erro à exceção
            throw new InvalidDataException(
                    "Já existe uma métrica de precificação com o nome '" + nomeMetrica + "' para este transportador.",
                    "METRICA_DUPLICADA");
        }
    }
}
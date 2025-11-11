// Caminho: src/main/java/br/com/wta/frete/logistica/service/LanceService.java
package br.com.wta.frete.logistica.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.wta.frete.colaboradores.entity.Transportador;
import br.com.wta.frete.colaboradores.repository.TransportadorRepository;
import br.com.wta.frete.logistica.controller.dto.LanceRequest;
import br.com.wta.frete.logistica.controller.dto.LanceResponse;
import br.com.wta.frete.logistica.entity.Frete;
import br.com.wta.frete.logistica.entity.Lance;
import br.com.wta.frete.logistica.repository.FreteRepository;
import br.com.wta.frete.logistica.repository.LanceRepository;
import br.com.wta.frete.logistica.service.mapper.LanceMapper;
import br.com.wta.frete.shared.exception.InvalidDataException;
import br.com.wta.frete.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;

/**
 * Serviço responsável por gerenciar Lances (Leilão Reverso Dinâmico Avançado).
 * Regras: Bloqueio/Desbloqueio por Frete, Decremento Mínimo de R$ 5,00.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class LanceService {

    // --- REGRA DE NEGÓCIO: Decremento Mínimo Competitivo ---
    // Diferença mínima exigida para que um novo lance seja válido em relação ao
    // melhor lance atual.
    private static final BigDecimal DECREMENTO_MINIMO = new BigDecimal("5.00");
    // Código de erro genérico para a exceção de dados inválidos (CORREÇÃO)
    private static final String CODIGO_ERRO_VALIDACAO = "VALIDACAO_LANCE";

    private final LanceRepository lanceRepository;
    private final FreteRepository freteRepository;
    private final TransportadorRepository transportadorRepository;
    private final LanceMapper lanceMapper;

    /**
     * Processa a submissão de um novo lance, com validações avançadas de
     * competição.
     */
    @SuppressWarnings("null")
    public LanceResponse criarLance(Long freteId, LanceRequest request) {

        // ResourceNotFoundException, assumimos que o construtor de 1 String é válido
        Frete frete = freteRepository.findById(freteId)
                .orElseThrow(() -> new ResourceNotFoundException("Frete não encontrado com ID: " + freteId));

        // Validações de Pré-requisito
        validarStatusFreteParaLance(frete);
        validarTransportadorExistente(request.transportadorId());

        Transportador transportador = transportadorRepository.findById(request.transportadorId())
                .orElseThrow(() -> new ResourceNotFoundException("Transportador não encontrado."));

        // Busca o lance anterior do transportador neste frete
        Optional<Lance> optionalLanceExistente = lanceRepository
                .findByFreteFreteIdAndTransportadorPessoaId(freteId, request.transportadorId());

        Lance lanceSalvo;

        if (optionalLanceExistente.isPresent()) {
            // Se já tem lance: verifica se o transportador foi superado e se o novo lance é
            // válido (atualização).
            lanceSalvo = atualizarLanceExistente(frete, optionalLanceExistente.get(), request.valorLance());
        } else {
            // Se for o primeiro lance do transportador: valida e cria.
            lanceSalvo = criarPrimeiroLance(frete, transportador, request.valorLance());
        }

        return lanceMapper.toResponse(lanceSalvo);
    }

    // --- LÓGICA DE CRIAÇÃO E RE-BIDDING ---

    @SuppressWarnings("null")
    private Lance criarPrimeiroLance(Frete frete, Transportador transportador, BigDecimal valorProposto) {

        // Valida o valor proposto contra o melhor lance atual (se houver).
        // Para o primeiro lance, o 'lanceAnterior' é nulo.
        validarValorDoLance(frete, null, valorProposto);

        Lance novoLance = Lance.builder()
                .frete(frete)
                .transportador(transportador)
                .valorLance(valorProposto)
                .dataLance(LocalDateTime.now())
                .vencedor(false)
                .build();

        return lanceRepository.save(novoLance);
    }

    private Lance atualizarLanceExistente(Frete frete, Lance lanceExistente, BigDecimal novoValorProposto) {

        // 1. REGRAS DE VALIDAÇÃO UNIFICADAS
        // O método agora recebe o lance anterior do próprio transportador para aplicar
        // todas as regras.
        validarValorDoLance(frete, lanceExistente, novoValorProposto);

        // 4. ATUALIZAÇÃO
        lanceExistente.setValorLance(novoValorProposto);
        lanceExistente.setDataLance(LocalDateTime.now());

        return lanceRepository.save(lanceExistente);
    }

    // --- MÉTODOS AUXILIARES DE VALIDAÇÃO ---

    private void validarStatusFreteParaLance(Frete frete) {
        // Validação de Status
        if (frete.getStatusLeilao() == null || !frete.getStatusLeilao().getNomeStatus().equals("AGUARDANDO_LANCES")) {
            // CORREÇÃO: Usando construtor com 2 Strings
            throw new InvalidDataException(
                    "Não é permitido enviar lances. O frete está no status: " + frete.getStatusLeilao().getNomeStatus(),
                    CODIGO_ERRO_VALIDACAO);
        }

        // Validação de Tempo
        if (frete.getDataExpiracaoNegociacao() != null
                && frete.getDataExpiracaoNegociacao().isBefore(LocalDateTime.now())) {
            // CORREÇÃO: Usando construtor com 2 Strings
            throw new InvalidDataException("O período de negociação para este Frete expirou.", CODIGO_ERRO_VALIDACAO);
        }
    }

    @SuppressWarnings("null")
    private void validarTransportadorExistente(Long transportadorId) {
        // ResourceNotFoundException, assumimos que o construtor de 1 String é válido
        if (!transportadorRepository.existsById(transportadorId)) {
            throw new ResourceNotFoundException("Transportador não encontrado com ID: " + transportadorId);
        }
    }

    /**
     * Unifica todas as regras de validação de valor para um novo lance ou um
     * re-bid.
     */
    private void validarValorDoLance(Frete frete, Lance lanceAnterior, BigDecimal novoValor) {
        // Busca o melhor lance atual no leilão para comparar.
        Optional<Lance> melhorLanceAtualOpt = lanceRepository
                .findTopByFrete_OrdemServico_IdOrderByValorLanceAsc(frete.getOrdemServicoId());

        // Regra 1: O valor do lance deve ser positivo.
        if (novoValor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidDataException("O valor do lance deve ser positivo.", CODIGO_ERRO_VALIDACAO);
        }

        // Regra 2: Se o transportador já tem um lance, o novo deve ser menor.
        if (lanceAnterior != null && novoValor.compareTo(lanceAnterior.getValorLance()) >= 0) {
            throw new InvalidDataException(
                    "O novo lance (R$ " + novoValor.toPlainString()
                            + ") deve ser estritamente menor que o seu lance anterior (R$ "
                            + lanceAnterior.getValorLance().toPlainString() + ").",
                    CODIGO_ERRO_VALIDACAO);
        }

        if (melhorLanceAtualOpt.isPresent()) {
            Lance melhorLanceAtual = melhorLanceAtualOpt.get();

            // Regra 3: O transportador não pode dar um novo lance se já possui o melhor.
            if (lanceAnterior != null && melhorLanceAtual.getId().equals(lanceAnterior.getId())) {
                throw new InvalidDataException(
                        "Você já possui o melhor lance (R$ " + melhorLanceAtual.getValorLance().toPlainString()
                                + ") neste leilão. Aguarde a competição para poder superá-lo.",
                        CODIGO_ERRO_VALIDACAO);
            }

            // Regra 4: O novo lance deve ser competitivamente menor que o melhor lance
            // atual.
            BigDecimal valorMaximoPermitido = melhorLanceAtual.getValorLance().subtract(DECREMENTO_MINIMO);
            if (novoValor.compareTo(valorMaximoPermitido) > 0) {
                throw new InvalidDataException(
                        "O lance deve ser de pelo menos R$ " + DECREMENTO_MINIMO.toPlainString()
                                + " inferior ao melhor lance atual (R$ "
                                + melhorLanceAtual.getValorLance().toPlainString()
                                + "). Valor máximo permitido: R$ " + valorMaximoPermitido.toPlainString(),
                        CODIGO_ERRO_VALIDACAO);
            }
        } else {
            // Se não há melhor lance, este é o primeiro lance do leilão.
            // A validação de valor positivo (Regra 1) já é suficiente.
            if (lanceAnterior != null) {
                // Cenário de erro de consistência de dados.
                throw new IllegalStateException("Erro de lógica do leilão: Lance anterior encontrado, mas nenhum melhor lance geral existe.");
            }
        }
    }

}
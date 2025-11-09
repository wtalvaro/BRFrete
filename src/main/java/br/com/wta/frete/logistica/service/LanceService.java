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
                .findByFreteOrdemServico_IdAndTransportadorPessoaId(freteId, request.transportadorId());

        Lance lanceSalvo;

        if (optionalLanceExistente.isPresent()) {
            // Se já tem lance: verifica se o transportador foi superado e se o novo lance é
            // válido.
            lanceSalvo = rebidLance(frete, optionalLanceExistente.get(), request.valorLance());
        } else {
            // Se for o primeiro lance: verifica se o novo lance é o melhor.
            lanceSalvo = criarNovoLance(frete, transportador, request.valorLance());
        }

        return lanceMapper.toResponse(lanceSalvo);
    }

    // --- LÓGICA DE CRIAÇÃO E RE-BIDDING ---

    @SuppressWarnings("null")
    private Lance criarNovoLance(Frete frete, Transportador transportador, BigDecimal valorProposto) {

        Optional<Lance> melhorLanceAtual = lanceRepository
                .findTopByFreteOrdemServico_IdOrderByValorPropostoAsc(frete.getOrdemServicoId());

        if (melhorLanceAtual.isPresent()) {
            // Se já houver um lance, o novo lance deve obedecer à regra de decremento
            // competitivo
            validarDecrementoCompetitivo(melhorLanceAtual.get().getValorLance(), valorProposto,
                    "para iniciar a disputa");
        } else {
            // Caso não haja lances, não há decremento competitivo a ser validado (apenas
            // valor positivo)
            if (valorProposto.compareTo(BigDecimal.ZERO) <= 0) {
                // CORREÇÃO: Usando construtor com 2 Strings
                throw new InvalidDataException("O valor do primeiro lance deve ser positivo.", CODIGO_ERRO_VALIDACAO);
            }
        }

        Lance novoLance = Lance.builder()
                .frete(frete)
                .transportador(transportador)
                .valorLance(valorProposto)
                .dataLance(LocalDateTime.now())
                .vencedor(false)
                .build();

        return lanceRepository.save(novoLance);
    }

    private Lance rebidLance(Frete frete, Lance lanceExistente, BigDecimal novoValorProposto) {

        // 1. RE-BIDDING PERMISSION (Regra: O transportador só pode dar um novo lance se
        // seu lance não for o melhor)
        Optional<Lance> melhorLanceAtualOptional = lanceRepository
                .findTopByFreteOrdemServico_IdOrderByValorPropostoAsc(frete.getOrdemServicoId());

        if (melhorLanceAtualOptional.isPresent()) {
            Lance melhorLanceAtual = melhorLanceAtualOptional.get();

            if (melhorLanceAtual.getId().equals(lanceExistente.getId())) {
                // Se o ID do melhor lance atual for o mesmo do transportador, ele está
                // BLOQUEADO.
                // CORREÇÃO: Usando construtor com 2 Strings
                throw new InvalidDataException(
                        "Você já possui o melhor lance (R$ " + melhorLanceAtual.getValorLance().toPlainString()
                                + ") neste leilão. Aguarde a competição para poder superá-lo.",
                        CODIGO_ERRO_VALIDACAO);
            }

            // 2. DECREMENTO COMPETITIVO (Regra: R$ 5,00 inferior ao melhor lance atual - Y)
            validarDecrementoCompetitivo(melhorLanceAtual.getValorLance(), novoValorProposto,
                    "para superar o lance atual (R$ " + melhorLanceAtual.getValorLance().toPlainString() + ")");
        } else {
            // Caso inesperado: se o lance existe, o melhor lance deveria existir
            // CORREÇÃO: Usando construtor com 2 Strings
            throw new InvalidDataException("Erro de lógica do leilão: Lance existente sem melhor lance geral.",
                    CODIGO_ERRO_VALIDACAO);
        }

        // 3. O lance não pode ser pior do que o próprio lance anterior
        if (novoValorProposto.compareTo(lanceExistente.getValorLance()) >= 0) {
            // CORREÇÃO: Usando construtor com 2 Strings
            throw new InvalidDataException(
                    "O novo lance (" + novoValorProposto + ") deve ser ESTRITAMENTE MENOR que o seu lance anterior de: "
                            + lanceExistente.getValorLance(),
                    CODIGO_ERRO_VALIDACAO);
        }

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
     * Regra Anti-Spam (R$ 5,00) aplicada ao lance a ser batido.
     */
    private void validarDecrementoCompetitivo(BigDecimal valorCompetitivo, BigDecimal novoValor, String contexto) {
        // Calcula o valor MÁXIMO que o novo lance pode ter: (Valor a Bater - Decremento
        // Mínimo)
        BigDecimal valorMaximoPermitido = valorCompetitivo.subtract(DECREMENTO_MINIMO);

        // Se o novo valor for MAIOR que o valor máximo permitido, ele é rejeitado.
        if (novoValor.compareTo(valorMaximoPermitido) > 0) {
            // CORREÇÃO: Usando construtor com 2 Strings
            throw new InvalidDataException(
                    "O lance deve ser de pelo menos R$ " + DECREMENTO_MINIMO.toPlainString() +
                            " inferior ao lance a ser batido. O valor máximo permitido " + contexto + " é de R$ "
                            + valorMaximoPermitido.toPlainString() + ".",
                    CODIGO_ERRO_VALIDACAO);
        }
    }
}
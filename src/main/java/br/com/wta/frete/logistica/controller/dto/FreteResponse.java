package br.com.wta.frete.logistica.controller.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO de Resposta para a entidade Frete.
 */
public record FreteResponse(
		// [NOVO] ID Próprio do Frete
		Long freteId,

		// Campos herdados da OrdemServico (para referência)
		Long ordemServicoId,

		// Campos de Relacionamento
		Integer modalidadeId,
		String nomeModalidade,
		Integer statusLeilaoId,
		String nomeStatusLeilao,

		// [NOVO] Transportador Selecionado (Pode ser null)
		Long transportadorSelecionadoId,

		// Campos de Negociação
		LocalDateTime dataExpiracaoNegociacao,
		BigDecimal precoSugerido,
		BigDecimal anttPisoMinimo,
		BigDecimal custoBaseMercado,
		BigDecimal distanciaKm,
		BigDecimal valorFinalAceito,

		// Campos de Logística Adicionais
		BigDecimal pesoTotalKg,
		BigDecimal valorInicialProposto,
		String tipoEmbalagem

// O campo 'prazoEncerramento' deve ser removido do DTO se existia antes,
// sendo substituído por 'dataExpiracaoNegociacao'
) {
}
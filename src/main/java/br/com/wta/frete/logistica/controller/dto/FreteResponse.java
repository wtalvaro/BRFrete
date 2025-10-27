package br.com.wta.frete.logistica.controller.dto;

import java.time.ZonedDateTime;
import java.math.BigDecimal;

/**
 * DTO de Resposta para a entidade Frete (logistica.fretes). Retorna os detalhes
 * do processo de leilão/proposta de um frete.
 */
public record FreteResponse(
		// FK para a Ordem de Serviço (Chave Primária e Estrangeira)
		Long ordemServicoId,

		// FK para a Modalidade de Frete (Lookup)
		Integer modalidadeId,

		// FK para o Status do Leilão (Lookup)
		Integer statusLeilaoId,

		// Data e hora limite para receber propostas (TIMESTAMP WITH TIME ZONE)
		ZonedDateTime prazoEncerramento,

		// Valor base sugerido para o frete (NUMERIC)
		BigDecimal valorInicialProposto,

		// Valor final aceito, se o leilão foi fechado (NUMERIC)
		BigDecimal valorFinalAceito,

		// Tipo de embalagem requerida/utilizada
		String tipoEmbalagem) {
}
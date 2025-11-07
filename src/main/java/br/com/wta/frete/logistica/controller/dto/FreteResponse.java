package br.com.wta.frete.logistica.controller.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime; // Importado para compatibilidade com a Entidade Frete
import java.time.ZonedDateTime;

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
		String tipoEmbalagem,

		// --- NOVOS CAMPOS ADICIONADOS (Para resolver os Warnings) ---

		// Distância rodoviária em Km (NUMERIC)
		BigDecimal distanciaKm,

		// Piso mínimo de frete sugerido pela ANTT (NUMERIC)
		BigDecimal anttPisoMinimo,

		// Preço sugerido de mercado para o frete (NUMERIC)
		BigDecimal precoSugerido,

		// Custo base de mercado calculado (NUMERIC)
		BigDecimal custoBaseMercado,

		// Data e hora de expiração da negociação
		LocalDateTime dataExpiracaoNegociacao) { // Usando LocalDateTime conforme a Entidade
}
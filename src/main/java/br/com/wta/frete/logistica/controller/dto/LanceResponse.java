package br.com.wta.frete.logistica.controller.dto;

import java.time.ZonedDateTime;
import java.math.BigDecimal;

/**
 * DTO de Resposta para a entidade Lance (logistica.lances). Retorna os detalhes
 * da proposta de valor de um transportador para um frete.
 */
public record LanceResponse(
		// Chave primária do Lance (renomeado de 'id' para clareza)
		Long lanceId,

		// FK para a Ordem de Serviço (Frete) a que o lance se refere
		Long ordemServicoId,

		// FK para o Transportador que fez a proposta
		Long transportadorPessoaId,

		// O valor proposto (NUMERIC)
		BigDecimal valorProposto,

		// Data e hora em que o lance foi registrado (TIMESTAMP WITH TIME ZONE)
		ZonedDateTime dataLance,

		// Indica se este lance foi o vencedor (BOOLEAN)
		Boolean vencedor) {
}
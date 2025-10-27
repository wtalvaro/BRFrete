package br.com.wta.frete.logistica.controller.dto;

/**
 * DTO de Resposta para a entidade StatusLeilao (logistica.status_leilao).
 * Representa os estados possíveis de um leilão (Lookup).
 */
public record StatusLeilaoResponse(
		// Chave primária do status (renomeado de 'id' para clareza)
		Integer statusLeilaoId,

		// Nome do status (ex: "ABERTO", "FINALIZADO")
		String nomeStatus) {
}
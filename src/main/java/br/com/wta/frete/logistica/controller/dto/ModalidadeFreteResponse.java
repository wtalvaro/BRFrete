package br.com.wta.frete.logistica.controller.dto;

/**
 * DTO de Resposta para a entidade ModalidadeFrete
 * (logistica.modalidades_frete). Representa os tipos de frete disponíveis
 * (Lookup).
 */
public record ModalidadeFreteResponse(
		// Chave primária da modalidade (renomeado de 'id' para clareza)
		Integer modalidadeFreteId,

		// Nome da modalidade (ex: "RODOVIARIO", "EXPRESSO")
		String nomeModalidade) {
}
package br.com.wta.frete.core.controller.dto;

import java.time.LocalDateTime;

/**
 * DTO de Resposta para a entidade ParticipanteConversa
 * (core.participantes_conversa). Representa um usuário dentro de uma conversa.
 */
public record ParticipanteConversaResponse(
		// O ID da conversa a que a pessoa pertence
		Long conversaId,

		// O ID da pessoa (usuário)
		Long pessoaId,

		// Data/Hora em que a pessoa entrou na conversa
		LocalDateTime dataEntrada) {
}
package br.com.wta.frete.core.controller.dto;

import java.time.LocalDateTime;

/**
 * DTO de Resposta para a entidade Conversa (core.conversas). Representa os
 * dados básicos de um chat.
 */
public record ConversaResponse(
		// Chave primária da conversa
		Long conversaId,

		// Tipo da conversa (ex: PRIVADA, GRUPO)
		String tipoConversa,

		// Data de criação da conversa (TIMESTAMP WITHOUT TIME ZONE)
		LocalDateTime dataCriacao,

		// Data/Hora da última mensagem (útil para listagem de chats)
		LocalDateTime ultimaMensagemEm) {
}
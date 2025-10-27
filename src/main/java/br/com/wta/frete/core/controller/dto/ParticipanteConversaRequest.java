package br.com.wta.frete.core.controller.dto;

import jakarta.validation.constraints.NotNull;

/**
 * DTO de Requisição para gerenciar a participação em uma Conversa
 * (core.participantes_conversa). Usado para adicionar ou remover um usuário de
 * um chat.
 */
public record ParticipanteConversaRequest(
		// O ID da conversa a ser modificada
		@NotNull(message = "O ID da conversa é obrigatório") Long conversaId,

		// O ID da pessoa a ser adicionada ou removida
		@NotNull(message = "O ID da pessoa é obrigatório") Long pessoaId) {
}
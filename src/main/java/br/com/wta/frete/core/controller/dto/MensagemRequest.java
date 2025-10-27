package br.com.wta.frete.core.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * DTO de Requisição para enviar uma nova Mensagem (core.mensagens).
 */
public record MensagemRequest(
		// O ID da conversa para onde a mensagem será enviada
		@NotNull(message = "O ID da conversa é obrigatório") Long conversaId,

		// O ID do autor que está enviando a mensagem
		@NotNull(message = "O ID do autor é obrigatório") Long autorId,

		// O conteúdo de texto da mensagem
		@NotBlank(message = "O conteúdo da mensagem não pode estar vazio") String conteudo) {
}
package br.com.wta.frete.core.controller.dto;

import java.time.LocalDateTime;

public record MensagemResponse(
		// Identificador único da mensagem (chave primária)
		Long mensagemId,

		// ID da conversa a que pertence
		Long conversaId,

		// ID da pessoa que enviou
		Long autorId,

		// O conteúdo da mensagem
		String conteudo,

		// Data e hora de envio (TIMESTAMP WITHOUT TIME ZONE)
		LocalDateTime dataEnvio,

		// Status de leitura
		Boolean isLida) {
}
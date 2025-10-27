package br.com.wta.frete.social.controller.dto;

import jakarta.validation.constraints.NotNull;

/**
 * DTO de Requisição para iniciar ou remover uma relação de Seguidor
 * (social.seguidores).
 */
public record SeguidorRequest(@NotNull(message = "O ID da pessoa que segue é obrigatório") Long seguidorId,

		@NotNull(message = "O ID da pessoa a ser seguida é obrigatório") Long seguidoId) {
	// NOTA: Em um sistema real, o 'seguidorId' viria tipicamente do token de
	// autenticação,
	// garantindo que o usuário só possa seguir em seu próprio nome.
}
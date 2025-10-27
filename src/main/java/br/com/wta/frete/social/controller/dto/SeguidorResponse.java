package br.com.wta.frete.social.controller.dto;

/**
 * DTO de Resposta para a entidade Seguidor (social.seguidores). Confirma a
 * criação de um relacionamento de Seguidor/Seguido.
 */
public record SeguidorResponse(
		// ID da Pessoa que está seguindo
		Long seguidorId,

		// ID da Pessoa que está sendo seguida
		Long seguidoId,

		// Status da operação ou do relacionamento
		String status // Ex: "Relacionamento estabelecido com sucesso"
) {
}
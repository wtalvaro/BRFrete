package br.com.wta.frete.social.controller.dto;

import java.time.ZonedDateTime;

/**
 * DTO de Resposta para a entidade Avaliação (social.avaliacoes). Retorna os
 * detalhes de uma avaliação, que pode ser para uma Ordem de Serviço ou um
 * Produto.
 */
public record AvaliacaoResponse(
		// Chave primária da Avaliação (renomeado de 'id' para clareza)
		Long avaliacaoId,

		// FK para a Pessoa que fez a avaliação
		Long avaliadorId,

		// FK para a Pessoa que foi avaliada
		Long avaliadoId,

		// ID do Frete/Ordem de Serviço avaliado (Polimórfico - pode ser null)
		Long ordemServicoId,

		// ID do Produto avaliado (Polimórfico - pode ser null)
		Integer produtoId,

		// Nota da avaliação (ex: 1 a 5)
		Integer nota,

		// Comentário ou texto da avaliação
		String comentario,

		// Data e hora em que a avaliação foi registrada
		ZonedDateTime dataAvaliacao) {
}
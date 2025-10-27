package br.com.wta.frete.social.controller.dto;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * DTO de Resposta para a entidade Comentário (social.comentarios). Retorna os
 * detalhes de um comentário, incluindo suas respostas aninhadas.
 */
public record ComentarioResponse(
		// Chave primária do Comentário (renomeado de 'id' para clareza)
		Long comentarioId,

		// FK para a Pessoa que escreveu o comentário
		Long autorId,

		// FK para o Produto ao qual o comentário se refere
		Integer produtoId,

		// FK para o comentário pai (null se for um comentário de nível superior)
		Long comentarioPaiId,

		// Conteúdo do comentário
		String textoComentario,

		// Data e hora em que o comentário foi feito
		ZonedDateTime dataComentario,

		// Lista das respostas (filhos) do comentário (Recursividade)
		List<ComentarioResponse> respostas) {
}
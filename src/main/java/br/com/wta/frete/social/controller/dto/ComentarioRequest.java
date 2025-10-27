package br.com.wta.frete.social.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * DTO de Requisição para postar um novo Comentário ou Resposta
 * (social.comentarios).
 */
public record ComentarioRequest(
		// Quem comenta
		@NotNull(message = "O ID do autor é obrigatório") Long autorId,

		// Onde o comentário é postado
		@NotNull(message = "O ID do produto é obrigatório") Integer produtoId,

		@NotBlank(message = "O texto do comentário é obrigatório") @Size(max = 1000, message = "O texto do comentário deve ter no máximo 1000 caracteres") String textoComentario,

		// Se for uma resposta, este campo deve ser preenchido (comentario_pai_id)
		Long comentarioPaiId) {
	// NOTA: Em uma API com segurança, o 'autorId' viria tipicamente do token do
	// usuário logado.
}
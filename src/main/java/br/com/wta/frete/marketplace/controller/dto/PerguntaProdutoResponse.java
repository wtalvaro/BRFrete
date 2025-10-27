package br.com.wta.frete.marketplace.controller.dto;

import java.time.LocalDateTime;

/**
 * DTO de Resposta para a entidade PerguntaProduto
 * (marketplace.perguntas_produtos). Exibição dos dados de uma pergunta feita a
 * um produto e sua resposta.
 */
public record PerguntaProdutoResponse(
		// Chave primária da Pergunta (renomeado de 'id' para clareza)
		Long perguntaId,

		// FK para o Produto a que a pergunta se refere
		Integer produtoId,

		// FK para a Pessoa (Cliente) que fez a pergunta
		Long autorId,

		// Nome do autor da pergunta (Campo otimizado para exibição)
		String nomeAutor,

		// Conteúdo da pergunta
		String textoPergunta,

		// Conteúdo da resposta (pode ser null se ainda não respondida)
		String resposta,

		// Data e hora em que a pergunta foi feita
		LocalDateTime dataPergunta,

		// Data e hora em que a pergunta foi respondida (pode ser null)
		LocalDateTime dataResposta) {
}
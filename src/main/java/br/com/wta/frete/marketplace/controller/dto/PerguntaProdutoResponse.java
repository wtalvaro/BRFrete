package br.com.wta.frete.marketplace.controller.dto;

import java.time.ZonedDateTime; // Tipo de dado correto para TIMESTAMP WITH TIME ZONE
import java.util.List;

/**
 * DTO de Resposta para a entidade PerguntaProduto
 * (marketplace.perguntas_produtos).
 * * ATUALIZAÇÃO:
 * - Removido 'resposta' e 'dataResposta'.
 * - Adicionados 'perguntaPaiId', 'isPublica' e a lista aninhada 'respostas'
 * (para threading).
 */
public record PerguntaProdutoResponse(
		// Chave primária da Pergunta
		Long perguntaId,

		// FK para o Produto
		Integer produtoId,

		// FK para a Pessoa (Cliente) que fez a pergunta/resposta
		Long autorId,

		// ID da pergunta pai (para threading)
		Long perguntaPaiId,

		// Nome do autor da pergunta (Campo otimizado para exibição)
		String nomeAutor,

		// Conteúdo da pergunta/resposta (texto_conteudo)
		String textoConteudo,

		// Data e hora em que a pergunta/resposta foi feita
		ZonedDateTime dataPublicacao, // Tipo de dado e nome de campo corrigidos

		// Status de visibilidade
		Boolean isPublica,

		// Respostas aninhadas para esta pergunta
		List<PerguntaProdutoResponse> respostas) {
}
package br.com.wta.frete.marketplace.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * DTO de Requisição para criação de uma Pergunta ou Resposta para um produto.
 * * ATUALIZAÇÃO:
 * - Campo 'resposta' removido.
 * - 'textoPergunta' renomeado para 'textoConteudo' e 'perguntaPaiId' adicionado
 * para respostas.
 */
public record PerguntaProdutoRequest(
		/**
		 * ID do Produto ao qual a pergunta/resposta se refere. Obrigatório.
		 */
		@NotNull(message = "O ID do produto é obrigatório.") Integer produtoId,

		/**
		 * ID do Autor. Opcional no Request, deve ser resolvido pelo token/serviço.
		 */
		Long autorId,

		/**
		 * Conteúdo da Pergunta ou Resposta (texto_conteudo). Obrigatório.
		 */
		@NotBlank(message = "O conteúdo da pergunta/resposta é obrigatório.") @Size(max = 2000) String textoConteudo,

		/**
		 * ID da pergunta pai (pergunta_pai_id). Necessário se for uma resposta.
		 * NULO se for uma pergunta principal.
		 */
		Long perguntaPaiId,

		/**
		 * Status de visibilidade. Usado para moderação.
		 */
		Boolean isPublica) {
}
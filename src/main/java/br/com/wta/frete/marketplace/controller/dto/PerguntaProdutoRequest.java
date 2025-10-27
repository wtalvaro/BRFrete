package br.com.wta.frete.marketplace.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * DTO de Requisição para recebimento de dados de uma Pergunta (criação) ou de
 * uma Resposta (atualização) para um produto no marketplace.
 */
public record PerguntaProdutoRequest(
		/**
		 * ID do Produto ao qual a pergunta se refere. Obrigatório.
		 */
		@NotNull(message = "O ID do produto é obrigatório.") Integer produtoId,

		/**
		 * ID do Autor da pergunta. Opcional no Request, pode ser resolvido pelo token
		 * de autenticação.
		 */
		Long autorId,

		/**
		 * Conteúdo da Pergunta (Texto). Obrigatório ao criar.
		 */
		@NotBlank(message = "O texto da pergunta é obrigatório.") String textoPergunta,

		/**
		 * Conteúdo da Resposta (Texto). Opcional, usado para responder uma pergunta
		 * existente.
		 */
		String resposta) {
	// NOTA: Para operações de resposta, o ID da Pergunta seria geralmente passado
	// no Path.
	// Para criação, este DTO é o suficiente.
}
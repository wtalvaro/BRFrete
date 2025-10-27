package br.com.wta.frete.inventario.controller.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

/**
 * DTO de Requisição para atualizar o Estoque de um Produto do Marketplace
 * (inventario.estoque_produto).
 */
public record EstoqueProdutoRequest(
		// O ID do Produto, que é a chave do EstoqueProduto
		@NotNull(message = "O ID do Produto é obrigatório") Integer produtoId,

		@NotNull(message = "A quantidade em estoque é obrigatória") @Min(value = 0, message = "A quantidade não pode ser negativa") Integer quantidade,

		@Min(value = 0, message = "O ponto de reposição não pode ser negativo") Integer pontoReposicao,

		@Size(max = 100) String localizacao) {
}
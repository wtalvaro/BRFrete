package br.com.wta.frete.marketplace.controller.dto;

import java.math.BigDecimal;

import br.com.wta.frete.marketplace.entity.enums.UnidadeMedidaEnum; // Assumindo este ENUM existe
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min; // Novo import
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * DTO de Requisição para criar ou atualizar um Produto (marketplace.produtos).
 * Inclui campos de Estoque (quantidade, pontoReposicao, localizacao) para
 * inicialização do EstoqueProduto.
 */
public record ProdutoRequest(
		// FK para o Lojista
		@NotNull(message = "O ID do Lojista (vendedor) é obrigatório") Long lojistaPessoaId, // Nome Corrigido

		// FK para a Categoria
		@NotNull(message = "O ID da Categoria é obrigatório") Integer categoriaId,

		@NotBlank(message = "O título do produto é obrigatório") @Size(max = 255) String titulo,

		// Descrição é opcional
		String descricao,

		@NotBlank(message = "O SKU é obrigatório") @Size(max = 50) String sku,

		@NotNull(message = "O preço é obrigatório") @DecimalMin(value = "0.00", inclusive = true, message = "O preço não pode ser negativo") BigDecimal preco,

		@NotNull(message = "A unidade de medida é obrigatória") UnidadeMedidaEnum unidadeMedida, // Usando o ENUM

		@NotNull(message = "O status de doação é obrigatório") Boolean isDoacao,

		@NotNull(message = "O status de disponibilidade é obrigatório") Boolean isDisponivel,

		// =========================================================================
		// CAMPOS DE ESTOQUE (NECESSÁRIOS PARA O ProdutoService)
		// =========================================================================

		@NotNull(message = "A quantidade inicial em estoque é obrigatória") @Min(value = 0, message = "A quantidade não pode ser negativa") Integer quantidade,

		@Min(value = 0, message = "O ponto de reposição não pode ser negativo") Integer pontoReposicao,

		@Size(max = 100) String localizacao

) {
}
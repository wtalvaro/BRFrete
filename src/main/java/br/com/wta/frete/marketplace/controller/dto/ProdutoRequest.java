package br.com.wta.frete.marketplace.controller.dto;

import java.math.BigDecimal;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;

/**
 * DTO de Requisição para criar ou atualizar um Produto (marketplace.produtos).
 */
public record ProdutoRequest(
		// FK para o Lojista
		@NotNull(message = "O ID do Lojista é obrigatório") Long lojistaPessoaId,

		// FK para a Categoria
		@NotNull(message = "O ID da Categoria é obrigatório") Integer categoriaId,

		@NotBlank(message = "O nome do produto é obrigatório") @Size(max = 255) String nomeProduto,

		// Descrição é opcional, mas deve ser um campo de estado
		String descricao,

		@NotBlank(message = "O SKU é obrigatório") @Size(max = 50) String sku,

		@NotNull(message = "O preço é obrigatório") @DecimalMin(value = "0.01", message = "O preço deve ser positivo") BigDecimal preco,

		// Peso é opcional, mas se for relevante para o frete, poderia ser @NotNull
		BigDecimal pesoKg,

		// Status de disponibilidade é opcional (pode ter valor padrão no BD)
		String statusDisponibilidade) {
}
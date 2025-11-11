package br.com.wta.frete.marketplace.controller.dto;

import java.math.BigDecimal;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

/**
 * DTO de Requisição para criar ou atualizar um Produto (marketplace.produtos).
 * * ATUALIZAÇÃO: Alinhamento com a entidade 'Produto' (titulo, quantidade,
 * unidadeMedida, isDisponivel, isDoacao).
 */
public record ProdutoRequest(
		// FK para o Lojista
		@NotNull(message = "O ID do Lojista (vendedor) é obrigatório") Long lojistaPessoaId,

		// FK para a Categoria
		@NotNull(message = "O ID da Categoria é obrigatório") Integer categoriaId,

		@NotBlank(message = "O título do produto é obrigatório") @Size(max = 255) String titulo, // Renomeado

		// Descrição é opcional
		String descricao,

		@NotBlank(message = "O SKU é obrigatório") @Size(max = 50) String sku,

		@NotNull(message = "O preço é obrigatório") @DecimalMin(value = "0.00", inclusive = true, message = "O preço não pode ser negativo") BigDecimal preco,

		@NotNull(message = "A quantidade é obrigatória") @Min(value = 1, message = "A quantidade deve ser de pelo menos 1") Integer quantidade, // Novo
																																				// campo

		@NotBlank(message = "A unidade de medida é obrigatória") @Size(max = 10) String unidadeMedida, // Novo campo

		@NotNull(message = "O status de doação é obrigatório") Boolean isDoacao, // Novo campo

		@NotNull(message = "O status de disponibilidade é obrigatório") Boolean isDisponivel, // Novo campo

		// O campo pesoKg foi removido pois não existe no SQL.

		// Status de disponibilidade anterior foi substituído por isDisponivel (boolean)
		String statusDisponibilidade // Mantido o campo statusDisponibilidade original, mas o ideal seria usar
										// isDisponivel para aderir ao SQL
) {
}
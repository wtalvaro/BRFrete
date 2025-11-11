package br.com.wta.frete.marketplace.controller.dto;

import br.com.wta.frete.marketplace.entity.enums.TipoGeralEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * DTO de Requisição para criar ou atualizar uma Categoria
 * (marketplace.categorias).
 */
public record CategoriaRequest(
		@NotBlank(message = "O nome da categoria é obrigatório") @Size(max = 100) String nomeCategoria,
		// Tipo geral da categoria. É obrigatório (NOT NULL) no DB.
		@NotNull(message = "O tipo geral da categoria é obrigatório") TipoGeralEnum tipoGeral) {
	// NOTA: Para operações de UPDATE (PUT/PATCH), o ID da Categoria
	// geralmente viria pelo Path, e não no corpo deste DTO.
}
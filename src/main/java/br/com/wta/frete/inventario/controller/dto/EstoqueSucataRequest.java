package br.com.wta.frete.inventario.controller.dto;

import java.math.BigDecimal;

import br.com.wta.frete.inventario.entity.enums.TipoMaterialEnum;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * DTO de Requisição para criar ou atualizar um item no Estoque de Sucata
 * (inventario.estoque).
 */
public record EstoqueSucataRequest(
		Long id, // ID é opcional (para atualização)

		@NotNull(message = "O ID do Sucateiro é obrigatório") Long sucateiroPessoaId,

		@NotNull(message = "O tipo de material é obrigatório") TipoMaterialEnum tipoMaterial, // ALTERADO: Usa o novo
																								// Enum

		@NotNull(message = "A quantidade em peso é obrigatória") @DecimalMin(value = "0.01", message = "A quantidade deve ser positiva") BigDecimal quantidadePesoKg,

		@Size(max = 50, message = "O status da qualidade deve ter no máximo 50 caracteres") String statusQualidade,

		@Size(max = 100, message = "A localização deve ter no máximo 100 caracteres") String localizacao) {
}
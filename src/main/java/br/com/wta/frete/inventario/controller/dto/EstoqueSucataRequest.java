package br.com.wta.frete.inventario.controller.dto;

import java.math.BigDecimal;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;

/**
 * DTO de Requisição para registrar ou atualizar o Estoque de Sucata
 * (inventario.estoque_sucata).
 */
public record EstoqueSucataRequest(
		// FK para o Sucateiro proprietário
		@NotNull(message = "O ID do Sucateiro é obrigatório") Long sucateiroPessoaId,

		@NotBlank(message = "O nome do material é obrigatório") @Size(max = 100) String nomeMaterial,

		@NotNull(message = "A quantidade (peso) é obrigatória") @DecimalMin(value = "0.001", message = "A quantidade deve ser positiva") BigDecimal quantidadePesoKg,

		@Size(max = 50) String statusQualidade) {
}
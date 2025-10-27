package br.com.wta.frete.colaboradores.controller.dto;

import java.math.BigDecimal;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;

/**
 * DTO de Requisição para receber dados de um novo Veículo
 * (colaboradores.veiculos).
 */
public record VeiculoRequest(
		// FK para o Transportador (Quem está cadastrando)
		@NotNull(message = "O ID do Transportador é obrigatório") Long transportadorPessoaId,

		@NotBlank(message = "A matrícula é obrigatória") @Size(max = 20) String matricula,

		@NotBlank(message = "O tipo de veículo é obrigatório") @Size(max = 50) String tipoVeiculo,

		@NotNull(message = "A capacidade de peso é obrigatória") @DecimalMin(value = "0.01", message = "A capacidade deve ser positiva") BigDecimal capacidadePesoKg,

		// Capacidade de volume é opcional na validação, mas é um campo de estado.
		BigDecimal capacidadeVolumeM3,

		@Size(max = 20) String statusVeiculo) {
}
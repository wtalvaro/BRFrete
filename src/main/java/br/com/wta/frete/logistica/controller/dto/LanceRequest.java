package br.com.wta.frete.logistica.controller.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

/**
 * DTO de requisição para submeter um novo lance ou atualizar um lance
 * existente.
 * CORREÇÃO: 'valorProposto' renomeado para 'valorLance'.
 */
public record LanceRequest(
		@NotNull(message = "O ID do Transportador é obrigatório.") Long transportadorId,

		@NotNull(message = "O valor do lance é obrigatório.") @DecimalMin(value = "0.01", message = "O valor deve ser positivo.") BigDecimal valorLance) {
}
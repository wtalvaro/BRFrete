package br.com.wta.frete.logistica.controller.dto;

import java.math.BigDecimal;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMin;

/**
 * DTO de Requisição para registrar uma proposta de Lance de frete
 * (logistica.lances).
 */
public record LanceRequest(
		// ID do Frete (ID da Ordem de Serviço)
		@NotNull(message = "O ID da Ordem de Serviço (Frete) é obrigatório") Long ordemServicoId,

		// ID do Transportador que está fazendo o lance
		@NotNull(message = "O ID do Transportador é obrigatório") Long transportadorPessoaId,

		@NotNull(message = "O valor proposto é obrigatório")
		// O valor deve ser maior que zero (inclusive = false)
		@DecimalMin(value = "0.0", inclusive = false, message = "O valor deve ser positivo") BigDecimal valorProposto) {
}
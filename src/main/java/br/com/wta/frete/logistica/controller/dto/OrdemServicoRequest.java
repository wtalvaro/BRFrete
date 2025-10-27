package br.com.wta.frete.logistica.controller.dto;

import java.time.ZonedDateTime;
import java.math.BigDecimal;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMin;

/**
 * DTO de Requisição para criar ou atualizar uma Ordem de Serviço
 * (logistica.ordens_servico).
 */
public record OrdemServicoRequest(
		// FK para o Cliente que está solicitando
		@NotNull(message = "O ID do Cliente é obrigatório") Long clientePessoaId,

		// FK opcional para o Transportador (se já atribuído, pode ser null)
		Long transportadorPessoaId,

		@NotBlank(message = "O local de origem é obrigatório") String localOrigem,

		@NotBlank(message = "O local de destino é obrigatório") String localDestino,

		@DecimalMin(value = "0.0", message = "A distância não pode ser negativa") BigDecimal distanciaKm,

		@NotNull(message = "O prazo para coleta é obrigatório") ZonedDateTime prazoColeta,

		// Status (pode ser usado para transições de status em uma API de UPDATE)
		String status) {
}
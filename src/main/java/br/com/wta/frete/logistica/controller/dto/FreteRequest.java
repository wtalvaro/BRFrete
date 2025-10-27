package br.com.wta.frete.logistica.controller.dto;

import java.time.ZonedDateTime;
import java.math.BigDecimal;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;

/**
 * DTO de Requisição para configurar o Frete (Leilão/Proposta)
 * (logistica.fretes).
 */
public record FreteRequest(
		// O ID da OrdemServico, que será a chave do Frete
		@NotNull(message = "O ID da Ordem de Serviço é obrigatório") Long ordemServicoId,

		// FK para ModalidadeFrete (Lookup)
		@NotNull(message = "O ID da Modalidade de Frete é obrigatório") Integer modalidadeId,

		// FK para StatusLeilao (Lookup) - Usado para iniciar o leilão
		@NotNull(message = "O ID do Status do Leilão é obrigatório") Integer statusLeilaoId,

		@NotNull(message = "O prazo de encerramento do leilão é obrigatório") ZonedDateTime prazoEncerramento,

		@DecimalMin(value = "0.0", message = "O valor inicial proposto não pode ser negativo") BigDecimal valorInicialProposto,

		@Size(max = 50) String tipoEmbalagem) {
}
package br.com.wta.frete.logistica.controller.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * DTO de Requisição para receber dados de um ItemFrete (logistica.itens_frete).
 */
public record ItemFreteRequest(
		// FK para a OrdemServico. Marcado como NOT NULL, pois é essencial para a
		// persistência.
		@NotNull(message = "O ID da Ordem de Serviço (Frete) é obrigatório") Long ordemServicoId,

		@NotBlank(message = "A descrição do item é obrigatória") @Size(max = 255) String descricaoItem,

		@NotNull(message = "O peso é obrigatório") @DecimalMin(value = "0.01", message = "O peso deve ser positivo") BigDecimal quantidadePesoKg,

		@DecimalMin(value = "0.0", message = "O volume não pode ser negativo") BigDecimal volumeM3,

		@DecimalMin(value = "0.0", message = "O valor estimado não pode ser negativo") BigDecimal valorEstimadoUnitario) {

	// CORREÇÃO: Método adicionado para resolver o erro 'pesoEstimadoKg' no
	// FreteService.
	public BigDecimal pesoEstimadoKg() {
		return quantidadePesoKg;
	}
}
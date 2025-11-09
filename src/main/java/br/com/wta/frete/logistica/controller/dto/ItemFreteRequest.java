package br.com.wta.frete.logistica.controller.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * DTO de Requisição para receber dados de um ItemFrete (logistica.itens_frete).
 * * CORREÇÕES: Nomes dos campos alinhados ao novo padrão.
 */
public record ItemFreteRequest(
		// FK para o Frete. Renomeado para 'freteId'
		@NotNull(message = "O ID do Frete é obrigatório") Long freteId,

		// NOME CORRIGIDO: 'descricaoItem' para 'descricao'
		// Removemos @Size pois o tipo SQL é TEXT.
		@NotBlank(message = "A descrição do item é obrigatória") String descricao,

		// NOVO CAMPO: Adicionamos o 'tipoMaterial'
		@Size(max = 100) String tipoMaterial,

		// NOME CORRIGIDO: 'quantidadePesoKg' para 'pesoEstimadoKg'
		@NotNull(message = "O peso é obrigatório") @DecimalMin(value = "0.01", message = "O peso deve ser positivo") BigDecimal pesoEstimadoKg,

		// NOME CORRIGIDO: 'volumeM3' para 'volumeEstimadoM3'
		@DecimalMin(value = "0.0", message = "O volume não pode ser negativo") BigDecimal volumeEstimadoM3) {

	/**
	 * MÉTODO REMOVIDO: O método 'pesoEstimadoKg()' que estava aqui foi removido,
	 * pois agora o nome do campo já é 'pesoEstimadoKg', eliminando a necessidade
	 * de getters customizados.
	 */
}
// Caminho: src/main/java/br/com/wta/frete/logistica/controller/dto/ItemFreteResponse.java
package br.com.wta.frete.logistica.controller.dto;

import java.math.BigDecimal;

/**
 * DTO de Resposta para a entidade ItemFrete.
 * * CORREÇÕES: Alinhamento de nomes de campos com a Entidade.
 */
public record ItemFreteResponse(
		// ID do item (chave primária de ItemFrete)
		Long itemFreteId,

		// Chave Externa para o Frete
		Long freteId,

		// NOME CORRIGIDO: 'nomeItem' para 'descricao'
		String descricao,

		// NOVO CAMPO: 'tipoMaterial'
		String tipoMaterial,

		// Peso Estimado
		BigDecimal pesoEstimadoKg) {

	/**
	 * CAMPO REMOVIDO: 'observacao' não existia no SQL nem na Entidade.
	 * O volume ('volumeEstimadoM3') também não foi incluído aqui para manter o DTO
	 * de resposta mais limpo, focando nos campos essenciais.
	 */
}
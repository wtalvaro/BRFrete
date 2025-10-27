package br.com.wta.frete.logistica.controller.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO de Resposta para a entidade AnttParametro (logistica.antt_parametros).
 * Retorna detalhes de um parâmetro regulatório (tabela de frete, impostos,
 * etc.).
 */
public record AnttParametroResponse(
		// Chave primária do parâmetro (renomeado de 'id' para clareza)
		Integer anttParametroId,

		// Chave do parâmetro (ex: 'FATOR_CUSTO_RODOVIARIO')
		String chave,

		// Valor numérico do parâmetro (NUMERIC)
		BigDecimal valor,

		// Descrição detalhada do parâmetro
		String descricao,

		// Data de início da vigência do parâmetro (sem informação de hora)
		LocalDate dataVigencia) {
}
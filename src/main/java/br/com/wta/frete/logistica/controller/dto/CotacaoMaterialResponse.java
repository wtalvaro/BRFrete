package br.com.wta.frete.logistica.controller.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO de Resposta para a entidade CotacaoMaterial
 * (logistica.cotacoes_materiais). Retorna os dados de cotação de um material
 * específico (Lookup).
 */
public record CotacaoMaterialResponse(
		// Chave primária da cotação (renomeado de 'id' para clareza)
		Integer cotacaoId,

		// Nome do material cotado (ex: "PLASTICO_PET", "ALUMINIO")
		String materialNome,

		// Preço médio por quilograma na data da cotação (NUMERIC)
		BigDecimal precoMedioKg,

		// Data em que a cotação foi registrada (sem informação de hora)
		LocalDate dataCotacao) {
}
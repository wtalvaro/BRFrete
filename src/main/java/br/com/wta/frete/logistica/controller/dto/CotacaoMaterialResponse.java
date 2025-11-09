package br.com.wta.frete.logistica.controller.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime; // Novo Import

/**
 * DTO de Resposta para a entidade CotacaoMaterial
 * (logistica.cotacoes_materiais). Retorna os dados de cotação de um material
 * específico (Lookup).
 * CORREÇÃO: Adicionados 'unidadeMedida' e 'dataAtualizacao'.
 */
public record CotacaoMaterialResponse(
		// Chave primária da cotação (renomeado de 'id' para clareza)
		Integer cotacaoId,

		// Nome do material cotado (ex: "PLASTICO_PET", "ALUMINIO")
		String materialNome,

		// Preço médio por quilograma na data da cotação (NUMERIC)
		BigDecimal precoMedioKg,

		// Unidade de medida (VARCHAR(10)) - NOVO CAMPO
		String unidadeMedida,

		// Data e hora da última atualização (TIMESTAMP WITHOUT TIME ZONE) - NOVO CAMPO
		LocalDateTime dataAtualizacao,

		// Data de vigência (mapeado de dataVigencia da Entity)
		LocalDate dataCotacao) { // Mapearemos para dataVigencia
}
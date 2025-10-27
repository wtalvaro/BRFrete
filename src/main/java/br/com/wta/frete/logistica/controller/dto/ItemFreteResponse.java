package br.com.wta.frete.logistica.controller.dto;

import java.math.BigDecimal;

/**
 * DTO de Resposta para a entidade ItemFrete (logistica.itens_frete). Retorna os
 * detalhes de um item específico que faz parte de uma Ordem de Serviço.
 */
public record ItemFreteResponse(
		// Chave primária do item de frete (renomeado de 'id' para clareza)
		Long itemFreteId,

		// FK para a Ordem de Serviço a que este item pertence
		Long ordemServicoId,

		// Descrição do material ou produto
		String descricaoItem,

		// Quantidade em peso (KG) do item
		BigDecimal quantidadePesoKg,

		// Volume em metros cúbicos (M3) do item
		BigDecimal volumeM3,

		// Valor unitário estimado do item
		BigDecimal valorEstimadoUnitario) {
}
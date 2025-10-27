package br.com.wta.frete.inventario.controller.dto;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

/**
 * DTO de Resposta para a entidade EstoqueSucata (inventario.estoque_sucata).
 * Retorna os detalhes de um item no estoque do sucateiro.
 */
public record EstoqueSucataResponse(
		// Chave primária do item de estoque (renomeado de 'id' para clareza)
		Long estoqueId,

		// FK para o Sucateiro que possui o estoque
		Long sucateiroPessoaId,

		// Nome ou tipo do material (ex: Ferro, Cobre, Plástico PET)
		String nomeMaterial,

		// Quantidade em peso (KG) do material em estoque
		BigDecimal quantidadePesoKg,

		// Status da qualidade do material (ex: ALTA, MEDIA, BAIXA)
		String statusQualidade,

		// Data/hora da última atualização de quantidade ou qualidade (TIMESTAMP WITH
		// TIME ZONE)
		ZonedDateTime dataAtualizacao) {
}
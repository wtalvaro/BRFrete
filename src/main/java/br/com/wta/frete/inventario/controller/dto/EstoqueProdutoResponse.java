package br.com.wta.frete.inventario.controller.dto;

import java.time.ZonedDateTime;

/**
 * DTO de Resposta para a entidade EstoqueProduto (inventario.estoque_produto).
 */
public record EstoqueProdutoResponse(
		// Chave primária/estrangeira do Produto (ID do Produto)
		Integer produtoId,

		// Quantidade atual do produto em estoque
		Integer quantidade,

		// Ponto mínimo de estoque para acionar a reposição
		Integer pontoReposicao,

		// Localização física do produto no armazém (ex: Corredor A, Prateleira 3)
		String localizacao,

		// Data/hora da última atualização de quantidade (TIMESTAMP WITH TIME ZONE)
		ZonedDateTime ultimaAtualizacao) {
}
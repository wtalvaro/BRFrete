package br.com.wta.frete.marketplace.controller.dto;

import br.com.wta.frete.marketplace.entity.enums.TipoGeralEnum;

/**
 * DTO de Resposta para a entidade Categoria (marketplace.categorias). Retorna
 * os detalhes de uma categoria de produto (Lookup).
 */
public record CategoriaResponse(
		// Chave primária da categoria (renomeado de 'id' para clareza)
		Integer categoriaId,

		// Nome da categoria (ex: "ELETRONICOS", "MATERIAIS_RECICLAVEIS")
		String nomeCategoria,

		// NOVO CAMPO: Tipo geral da categoria
		TipoGeralEnum tipoGeral) {
}
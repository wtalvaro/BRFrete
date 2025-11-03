package br.com.wta.frete.colaboradores.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * DTO de Requisição para receber dados de um Catador (colaboradores.catadores).
 * Usado para complementar o cadastro de uma Pessoa existente como catador.
 */
public record CatadorRequest(
		// INCLUSÃO NECESSÁRIA: O ID da Pessoa é a Chave Primária/Estrangeira da tabela.
		@NotNull(message = "O ID da pessoa é obrigatório para o Catador") Long pessoaId,

		// ID da associação (opcional, mas deve ser um Integer se fornecido)
		Integer associacaoId,

		@NotBlank(message = "A área de atuação geográfica é obrigatória") String areaAtuacaoGeografica) {
	// NOTA: Caso o pessoaId venha a ser injetado pelo Service/Controller, você
	// pode remover este campo do DTO, mas ele é crucial para o mapeamento da
	// entidade.
}
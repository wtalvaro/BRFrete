package br.com.wta.frete.colaboradores.controller.dto;

import java.time.LocalDate;

import br.com.wta.frete.core.controller.dto.PessoaResponse;

/**
 * DTO de Resposta para a entidade Catador (colaboradores.catadores). Retorna os
 * detalhes específicos de um catador, incluindo seus dados básicos de Pessoa.
 */
public record CatadorResponse(
		// ID da Pessoa, que é a Chave Primária/Estrangeira da tabela
		Long pessoaId,

		// Data de nascimento do catador (para validação de idade)
		LocalDate dataNascimento,

		// ID da associação ou cooperativa a que pertence
		Integer associacaoId,

		// Descrição da área geográfica onde o catador atua
		String areaAtuacaoGeografica,

		// Dados básicos da Pessoa associada (Composição)
		PessoaResponse dadosPessoa) {
}
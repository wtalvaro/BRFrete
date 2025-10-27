package br.com.wta.frete.colaboradores.controller.dto;

import br.com.wta.frete.core.controller.dto.PessoaResponse;

/**
 * DTO de Resposta para a entidade Transportador
 * (colaboradores.transportadores). Retorna os detalhes específicos de um
 * transportador, incluindo seus dados básicos de Pessoa.
 */
public record TransportadorResponse(
		// ID da Pessoa, que é a Chave Primária/Estrangeira da tabela
		Long pessoaId,

		// Licença ou certificação de transporte
		String licencaTransporte,

		// Dados básicos da Pessoa associada (Composição com o outro DTO)
		PessoaResponse dadosPessoa) {
}
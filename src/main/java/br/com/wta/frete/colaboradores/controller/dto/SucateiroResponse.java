package br.com.wta.frete.colaboradores.controller.dto;

import br.com.wta.frete.core.controller.dto.PessoaResponse;

/**
 * DTO de Resposta para a entidade Sucateiro (colaboradores.sucateiros). Retorna
 * os detalhes específicos de um sucateiro (geralmente PJ), incluindo seus dados
 * básicos de Pessoa.
 */
public record SucateiroResponse(
		// ID da Pessoa, que é a Chave Primária/Estrangeira da tabela
		Long pessoaId,

		// Razão Social da empresa sucateira
		String razaoSocial,

		// CNPJ secundário ou complementar, se houver
		String cnpjSecundario,

		// Número da licença ambiental
		String licencaAmbiental,

		// Endereço principal do pátio de sucata
		String enderecoPatio,

		// Dados básicos da Pessoa associada (Composição)
		PessoaResponse dadosPessoa) {
}
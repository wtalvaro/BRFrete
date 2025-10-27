package br.com.wta.frete.clientes.controller.dto;

import br.com.wta.frete.core.controller.dto.PessoaResponse;

/**
 * DTO de Resposta para a entidade DetalheCliente (clientes.detalhes). Retorna
 * os detalhes específicos de um cliente, incluindo seus dados básicos de
 * Pessoa.
 */
public record DetalheClienteResponse(
		// ID da Pessoa, que é a Chave Primária/Estrangeira da tabela clientes.detalhes
		Long pessoaId,

		// Tipo de cliente (Ex: Pessoa Física ou Pessoa Jurídica)
		String tipoCliente,

		// Preferências de coleta salvas pelo cliente
		String preferenciasColeta,

		// Dados básicos da Pessoa associada (Composição com o outro DTO)
		PessoaResponse dadosPessoa) {
}
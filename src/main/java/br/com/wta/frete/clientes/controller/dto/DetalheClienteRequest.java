package br.com.wta.frete.clientes.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * DTO de Requisição para receber dados específicos de um DetalheCliente
 * (clientes.detalhes).
 */
public record DetalheClienteRequest(
		// ID da Pessoa, que é a Chave Primária/Estrangeira da tabela clientes.detalhes
		// Este campo é essencial para ligar o detalhe à Pessoa
		@NotNull(message = "O ID da pessoa é obrigatório") Long pessoaId,

		@NotBlank(message = "O tipo de cliente é obrigatório (PF, PJ, etc.)") @Size(max = 20) String tipoCliente,

		// Campo opcional (TEXT no BD)
		String preferenciasColeta) {
}
package br.com.wta.frete.clientes.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
// import jakarta.validation.constraints.Size; // Removido!

/**
 * DTO de Requisição para receber dados específicos de um DetalheCliente
 * (clientes.detalhes).
 */
public record DetalheClienteRequest(
		// ID da Pessoa, que é a Chave Primária/Estrangeira da tabela clientes.detalhes
		@NotNull(message = "O ID da pessoa é obrigatório") Long pessoaId,

		// O Mapper garantirá que o String recebido corresponda a um dos valores do
		// Enum.
		@NotBlank(message = "O tipo de cliente é obrigatório (PF, PJ, etc.)") String tipoCliente,

		// Campo opcional (TEXT no BD)
		String preferenciasColeta) {
}
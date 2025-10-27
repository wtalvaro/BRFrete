package br.com.wta.frete.clientes.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * DTO de Requisição para receber dados de uma nova solicitação de PedidoColeta
 * (clientes.pedidos_coleta).
 */
public record PedidoColetaRequest(
		// FK para o Cliente que está fazendo o pedido (clientes.detalhes.pessoa_id)
		@NotNull(message = "O ID do Cliente é obrigatório") Long clientePessoaId,

		@NotBlank(message = "A descrição do pedido é obrigatória") String descricaoPedido) {
}
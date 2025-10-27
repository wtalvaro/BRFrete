package br.com.wta.frete.clientes.controller.dto;

import java.time.ZonedDateTime;

/**
 * DTO de Resposta para a entidade PedidoColeta (clientes.pedidos_coleta).
 * Retorna os detalhes de um pedido de coleta existente.
 */
public record PedidoColetaResponse(
		// Chave primária do pedido (renomeado de 'id' para clareza)
		Long pedidoId,

		// FK para o Cliente que fez o pedido
		Long clientePessoaId,

		// Descrição detalhada do material a ser coletado
		String descricaoPedido,

		// Data e hora da solicitação (TIMESTAMP WITH TIME ZONE)
		ZonedDateTime dataSolicitacao) {
	// Nota: O tipo de dados ZonedDateTime é apropriado para a coluna TIMESTAMP WITH
	// TIME ZONE do PostgreSQL.
}
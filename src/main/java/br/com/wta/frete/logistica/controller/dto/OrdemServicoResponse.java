package br.com.wta.frete.logistica.controller.dto;

import java.time.ZonedDateTime;
import java.math.BigDecimal;

/**
 * DTO de Resposta para a entidade OrdemServico (logistica.ordens_servico).
 * Retorna os detalhes de uma ordem de serviço de transporte.
 */
public record OrdemServicoResponse(
		// Chave primária da Ordem de Serviço (renomeado de 'id' para clareza)
		Long ordemServicoId,

		// FK para o Cliente que solicitou o serviço
		Long clientePessoaId,

		// FK para o Transportador atribuído (Pode ser null)
		Long transportadorPessoaId,

		// Endereço ou descrição do local de origem
		String localOrigem,

		// Endereço ou descrição do local de destino
		String localDestino,

		// Distância da rota em quilômetros (NUMERIC)
		BigDecimal distanciaKm,

		// Data/hora limite para a coleta ser realizada (TIMESTAMP WITH TIME ZONE)
		ZonedDateTime prazoColeta,

		// Data/hora de criação da Ordem de Serviço (TIMESTAMP WITH TIME ZONE)
		ZonedDateTime dataCriacao,

		// Status atual da Ordem de Serviço (Mapeado de um Enum, ex: CRIADA, ATRIBUIDA,
		// CONCLUIDA)
		String status) {
}
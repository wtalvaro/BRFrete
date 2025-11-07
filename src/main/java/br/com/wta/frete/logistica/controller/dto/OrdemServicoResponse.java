package br.com.wta.frete.logistica.controller.dto;

import java.time.LocalDate;
import java.time.ZonedDateTime;

/**
 * DTO de Resposta (Record) para a entidade OrdemServico
 * (logistica.ordens_servico).
 */
public record OrdemServicoResponse(

		Long ordemServicoId,

		// FKs
		Long clienteSolicitanteId,
		Long transportadorDesignadoId,

		// Dados de Localização
		String enderecoColeta,
		String cepColeta,
		String cepDestino,

		// Datas e Status
		ZonedDateTime dataSolicitacao,
		LocalDate dataPrevistaColeta,
		String status // StatusServico mapeado para String
) {
}
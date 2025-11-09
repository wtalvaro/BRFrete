package br.com.wta.frete.logistica.controller.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO de resposta para um Lance.
 * CORREÇÃO: 'valorProposto' renomeado para 'valorLance'. Adicionado
 * 'motivoCancelamento'.
 */
public record LanceResponse(
		Long lanceId,
		Long freteId, // Corresponde a frete.freteId
		Long transportadorId,
		String nomeTransportador,
		BigDecimal valorLance,
		LocalDateTime dataLance,
		boolean vencedor,
		String motivoCancelamento) {
}
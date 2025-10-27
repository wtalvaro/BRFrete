package br.com.wta.frete.colaboradores.controller.dto;

import java.math.BigDecimal;

/**
 * DTO de Resposta para a entidade Veículo (colaboradores.veiculos). Retorna os
 * detalhes e capacidades de um veículo de transporte cadastrado.
 */
public record VeiculoResponse(
		// Chave primária do veículo (renomeado de 'id')
		Long veiculoId,

		// ID da Pessoa Transportadora associada
		Long transportadorPessoaId,

		// Placa ou matrícula do veículo (UNIQUE)
		String matricula,

		// Tipo do veículo (ex: TRUCK, VAN, MOTO)
		String tipoVeiculo,

		// Capacidade máxima de peso em KG (NUMERIC)
		BigDecimal capacidadePesoKg,

		// Capacidade máxima de volume em M3 (NUMERIC)
		BigDecimal capacidadeVolumeM3,

		// Status do veículo (ex: ATIVO, MANUTENCAO, INATIVO)
		String statusVeiculo) {
}
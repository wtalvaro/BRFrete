package br.com.wta.frete.colaboradores.controller.dto;

import java.math.BigDecimal;
// Novos Imports para os Enums (melhor para o consumidor da API)
import br.com.wta.frete.colaboradores.entity.enums.StatusVeiculo;
import br.com.wta.frete.colaboradores.entity.enums.TipoVeiculo;

/**
 * DTO de Resposta para a entidade Veículo (colaboradores.veiculos).
 * CORREÇÃO: Estrutura atualizada para refletir todos os campos da entidade
 * Veiculo.
 */
public record VeiculoResponse(
		// Chave primária do veículo
		Integer veiculoId, // Alterado para Integer, tipo da PK na Entidade Veiculo

		// ID da Pessoa Transportadora associada (Chave Estrangeira)
		Long transportadorPessoaId,

		// CORRIGIDO: Placa (VARCHAR(10) UNIQUE NOT NULL)
		String placa,

		// NOVO CAMPO: Renavam (VARCHAR(11) UNIQUE NOT NULL)
		String renavam,

		// Tipo do veículo (Enum)
		TipoVeiculo tipoVeiculo, // Alterado para TipoVeiculo (Enum)

		// NOVO CAMPO: Ano de fabricação (INTEGER)
		Integer anoFabricacao,

		// CORRIGIDO: Capacidade máxima de peso em KG
		BigDecimal capacidadeKg,

		// CORRIGIDO: Capacidade máxima de volume em M3
		BigDecimal capacidadeM3,

		// NOVO CAMPO: Possui Rastreador (BOOLEAN)
		Boolean possuiRastreador,

		// Status do veículo (Enum)
		StatusVeiculo statusVeiculo) { // Alterado para StatusVeiculo (Enum)
}
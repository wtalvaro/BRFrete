package br.com.wta.frete.colaboradores.controller.dto;

import java.math.BigDecimal;

import br.com.wta.frete.colaboradores.entity.enums.StatusVeiculo;
import br.com.wta.frete.colaboradores.entity.enums.TipoVeiculo;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min; // Novo Import
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * DTO de Requisição para receber dados de um novo Veículo
 * (colaboradores.veiculos).
 * CORREÇÃO: DTO atualizado para refletir a nova estrutura da tabela 'veiculos'
 * (placa, renavam, etc.).
 */
public record VeiculoRequest(
		// FK para o Transportador (Quem está cadastrando)
		@NotNull(message = "O ID do Transportador é obrigatório") Long transportadorPessoaId,

		// CORRIGIDO: Nome do campo mudou para 'placa' e tamanho ajustado (VARCHAR(10))
		@NotBlank(message = "A placa é obrigatória") @Size(max = 10) String placa,

		// NOVO CAMPO: Adicionado 'renavam' (VARCHAR(11) NOT NULL)
		@NotBlank(message = "O Renavam é obrigatório") @Size(max = 11) String renavam,

		@NotNull(message = "O tipo de veículo é obrigatório") TipoVeiculo tipoVeiculo,

		// NOVO CAMPO: Adicionado 'anoFabricacao' (INTEGER)
		@NotNull(message = "O ano de fabricação é obrigatório") @Min(value = 1900, message = "O ano deve ser válido") Integer anoFabricacao,

		// CORRIGIDO: Nome do campo mudou para 'capacidadeKg' (NUMERIC(10, 2) NOT NULL)
		@NotNull(message = "A capacidade de peso (KG) é obrigatória") @DecimalMin(value = "0.01", message = "A capacidade deve ser positiva") BigDecimal capacidadeKg,

		// CORRIGIDO: Nome do campo mudou para 'capacidadeM3' (NUMERIC(10, 2))
		BigDecimal capacidadeM3,

		// NOVO CAMPO: Adicionado 'possuiRastreador' (BOOLEAN DEFAULT FALSE)
		Boolean possuiRastreador,

		// StatusVeiculo (Enum)
		StatusVeiculo statusVeiculo) {
}
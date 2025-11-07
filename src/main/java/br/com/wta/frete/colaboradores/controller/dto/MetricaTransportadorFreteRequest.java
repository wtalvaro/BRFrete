package br.com.wta.frete.colaboradores.controller.dto;

import java.math.BigDecimal;

import br.com.wta.frete.colaboradores.entity.enums.TipoVeiculo;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * DTO de Requisição (Record) para criação ou atualização de uma Métrica de
 * Frete.
 * Usa o padrão Record para imutabilidade e concisão (Java 17+).
 */
public record MetricaTransportadorFreteRequest(

        // ID do Transportador (Será ignorado no Mapper, mas útil para o Service)
        @NotNull(message = "O ID do Transportador é obrigatório.") Long transportadorPessoaId,

        @NotBlank(message = "O nome da métrica é obrigatório.") @Size(max = 100) String nomeMetrica,

        // --- CAMPOS DE FILTRO ---

        @Size(max = 100) String tipoCargaMaterial,

        TipoVeiculo tipoVeiculo,

        // ID da Modalidade de Frete
        Long modalidadeFreteId,

        // --- CUSTOS ---

        @NotNull(message = "O custo fixo por viagem é obrigatório.") @DecimalMin(value = "0.00", message = "O custo fixo deve ser positivo ou zero.") BigDecimal custoFixoViagem,

        @NotNull(message = "O custo por km é obrigatório.") @DecimalMin(value = "0.0000", message = "O custo por km deve ser positivo ou zero.") BigDecimal custoPorKm,

        @NotNull(message = "A margem de lucro é obrigatória.") @DecimalMin(value = "0.0000", message = "A margem de lucro deve ser positiva ou zero.") BigDecimal margemLucro,

        @NotNull(message = "O custo por hora de espera é obrigatório.") @DecimalMin(value = "0.00", message = "O custo por hora de espera deve ser positivo ou zero.") BigDecimal custoHoraEspera) {
}
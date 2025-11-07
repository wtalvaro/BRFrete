package br.com.wta.frete.colaboradores.controller.dto;

import java.math.BigDecimal;

import br.com.wta.frete.colaboradores.entity.enums.TipoVeiculo;
import br.com.wta.frete.logistica.controller.dto.ModalidadeFreteResponse;

/**
 * DTO de Resposta (Record) para os detalhes da Métrica de Frete.
 */
public record MetricaTransportadorFreteResponse(

        Long metricaId,
        Long transportadorPessoaId,
        String nomeMetrica,

        // --- CAMPOS DE FILTRO ---
        String tipoCargaMaterial,
        TipoVeiculo tipoVeiculo,
        ModalidadeFreteResponse modalidadeFrete, // DTO aninhado para modalidade

        // --- CUSTOS ---
        BigDecimal custoFixoViagem,
        BigDecimal custoPorKm,
        BigDecimal margemLucro,
        BigDecimal custoHoraEspera,

        Integer versao) {
}
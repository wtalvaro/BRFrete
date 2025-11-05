package br.com.wta.frete.colaboradores.controller.dto;

import java.time.LocalTime;

/**
 * DTO de Resposta para retornar os detalhes de um horário de operação.
 */
public record HorarioOperacaoResponse(
        Long horarioId,
        Long pessoaId,
        Short diaSemana,
        LocalTime horaAbertura,
        LocalTime horaFechamento) {
}
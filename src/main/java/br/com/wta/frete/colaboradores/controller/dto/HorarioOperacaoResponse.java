package br.com.wta.frete.colaboradores.controller.dto;

import java.time.LocalTime;

import br.com.wta.frete.shared.enums.DiaSemanaEnum; // Importar o novo ENUM

/**
 * DTO de Resposta para retornar os detalhes de um horário de operação.
 */
public record HorarioOperacaoResponse(
                Long horarioId,
                Long pessoaId,
                DiaSemanaEnum diaSemana, // Tipo alterado de Short para DiaSemanaEnum
                LocalTime horaAbertura,
                LocalTime horaFechamento) {
}
package br.com.wta.frete.colaboradores.controller.dto;

import java.time.LocalTime;

import br.com.wta.frete.shared.enums.DiaSemanaEnum;
import jakarta.validation.constraints.NotNull;

/**
 * DTO de Requisição para cadastrar ou atualizar um horário de operação.
 */
public record HorarioOperacaoRequest(
                @NotNull(message = "O ID da pessoa é obrigatório") Long pessoaId,

                @NotNull(message = "O dia da semana é obrigatório") DiaSemanaEnum diaSemana,

                @NotNull(message = "O horário de abertura é obrigatório") LocalTime horaAbertura,

                @NotNull(message = "O horário de fechamento é obrigatório") LocalTime horaFechamento) {
}
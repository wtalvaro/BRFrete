package br.com.wta.frete.colaboradores.controller.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;

/**
 * DTO de Requisição para cadastrar ou atualizar um horário de operação.
 */
public record HorarioOperacaoRequest(
        @NotNull(message = "O ID da pessoa é obrigatório") Long pessoaId,

        @NotNull(message = "O dia da semana é obrigatório") @Min(value = 1, message = "O dia da semana deve ser 1 (Domingo) ou superior") @Max(value = 7, message = "O dia da semana deve ser 7 (Sábado) ou inferior") Short diaSemana,

        @NotNull(message = "O horário de abertura é obrigatório") LocalTime horaAbertura,

        @NotNull(message = "O horário de fechamento é obrigatório") LocalTime horaFechamento) {
}
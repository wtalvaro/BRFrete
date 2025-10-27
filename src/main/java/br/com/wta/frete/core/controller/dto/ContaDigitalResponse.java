package br.com.wta.frete.core.controller.dto;

import java.time.LocalDateTime;

/**
 * DTO de Resposta para a entidade ContaDigital (core.contas_digitais).
 * Representa os detalhes da conta financeira de um usuário.
 */
public record ContaDigitalResponse(
		// ID da Pessoa, que é a chave primária da tabela core.contas_digitais
		Long pessoaId,

		// Identificador único da conta digital (UUID)
		String contaUuid,

		// Status do processo de "Know Your Customer" (KYC)
		String statusKyc,

		// Data de abertura da conta
		LocalDateTime dataAbertura) {
	// Não é necessário adicionar corpo ao record se não houver validações ou
	// métodos customizados
}
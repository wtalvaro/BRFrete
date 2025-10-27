package br.com.wta.frete.colaboradores.controller.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * DTO de Requisição para receber dados específicos de um Transportador
 * (colaboradores.transportadores). É usado para complementar o cadastro de uma
 * Pessoa existente.
 */
public record TransportadorRequest(
		// ID da Pessoa, que é a Chave Primária/Estrangeira
		@NotNull(message = "O ID da pessoa é obrigatório para o Transportador") Long pessoaId,

		// Dados específicos do Transportador
		@Size(max = 100) String licencaTransporte) {
	// NOTA: Em um cenário real de API, este DTO seria aninhado ou combinado
	// com PessoaRequest para formar um único endpoint de cadastro.
}
package br.com.wta.frete.colaboradores.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * DTO de Requisição para receber dados de um Lojista (colaboradores.lojistas).
 * Usado para complementar o cadastro de uma Pessoa existente como lojista.
 */
public record LojistaRequest(
		// ID da Pessoa, que é a Chave Primária/Estrangeira
		@NotNull(message = "O ID da pessoa é obrigatório para o Lojista") Long pessoaId,

		@NotBlank(message = "O nome da loja é obrigatório") @Size(max = 255) String nomeLoja,

		@NotBlank(message = "O endereço principal é obrigatório") String enderecoPrincipal) {
}
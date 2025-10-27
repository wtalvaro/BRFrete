package br.com.wta.frete.colaboradores.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * DTO de Requisição para receber dados de um Sucateiro
 * (colaboradores.sucateiros). Usado para complementar o cadastro de uma Pessoa
 * PJ/PF como sucateiro.
 */
public record SucateiroRequest(
		// ID da Pessoa, que é a Chave Primária/Estrangeira
		@NotNull(message = "O ID da pessoa é obrigatório para o Sucateiro") Long pessoaId,

		@NotBlank(message = "A razão social é obrigatória") @Size(max = 255) String razaoSocial,

		@Size(max = 18) String cnpjSecundario,

		@Size(max = 100) String licencaAmbiental,

		@NotBlank(message = "O endereço do pátio é obrigatório") String enderecoPatio) {
}
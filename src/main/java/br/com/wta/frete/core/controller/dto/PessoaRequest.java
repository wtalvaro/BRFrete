package br.com.wta.frete.core.controller.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO de Requisição para receber dados de Pessoa (cadastro/atualização).
 * Representa os dados básicos de um novo usuário que está se registrando.
 */
public record PessoaRequest(

		@NotBlank(message = "O nome é obrigatório") @Size(max = 255) String nomeCompleto,

		@NotBlank(message = "O documento (CPF/CNPJ) é obrigatório") @Size(max = 18) String documento,

		@NotBlank(message = "O email é obrigatório") @Email(message = "Email inválido") // Segunda Linha de Defesa
		@Size(max = 100) String email,

		@NotBlank(message = "A senha é obrigatória") String senha,

		@NotBlank(message = "O telefone é obrigatório") @Size(max = 20) String telefone) {
}
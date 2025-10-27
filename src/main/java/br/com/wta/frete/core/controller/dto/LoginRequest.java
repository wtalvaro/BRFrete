package br.com.wta.frete.core.controller.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO de Requisição para receber dados de Login (Autenticação).
 * Contém apenas os campos essenciais: e-mail e senha.
 */
public record LoginRequest(

        @NotBlank(message = "O email é obrigatório para o login.") @Email(message = "O formato do email é inválido.") @Size(max = 100) String email,

        @NotBlank(message = "A senha é obrigatória para o login.") String senha) {
    // O Record é final e imutável, ideal para DTOs de requisição.
}
package br.com.wta.frete.core.controller.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO de Requisição para um Cadastro Simplificado (Apenas e-mail e senha).
 * Usado para cadastros rápidos ou como primeiro passo para ativação.
 * * NOTE: Os campos obrigatórios 'nome' e 'documento' da entidade Pessoa serão
 * preenchidos com placeholders no Mapper.
 */
public record CadastroSimplificadoRequest(

        @NotBlank(message = "O e-mail é obrigatório para o cadastro.") @Email(message = "O formato do e-mail é inválido.") @Size(max = 100) String email,

        @NotBlank(message = "A senha é obrigatória para o cadastro.") @Size(min = 6, max = 50, message = "A senha deve ter entre 6 e 50 caracteres.") String senha) {
    // O Record é final e imutável, ideal para DTOs de requisição.
}
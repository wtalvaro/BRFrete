package br.com.wta.frete.core.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO de Requisição (Record) para criação e atualização de um Perfil
 * (core.perfis).
 */
public record PerfilRequest(

        // O nome único do perfil é obrigatório
        @NotBlank(message = "O nome do perfil é obrigatório") @Size(max = 50) String nomePerfil,

        // A descrição do perfil é obrigatória
        @NotBlank(message = "A descrição do perfil é obrigatória") String descricao) {
}
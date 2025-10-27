package br.com.wta.frete.core.controller.dto;

import lombok.Data;

/**
 * DTO: OAuth2UserRequestDTO
 * Propósito: Estrutura de dados para encapsular informações de um utilizador
 * obtidas de um provedor de Login Social (Google, Facebook).
 */
@Data
public class OAuth2UserRequestDTO {

    private final String socialId; // 💥 NOVO: ID único fornecido pelo Google (o "sub")
    private final String nome;
    private final String email;
    private final String provedor; // Ex: "google"
}
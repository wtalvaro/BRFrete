package br.com.wta.frete.core.controller.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO de Resposta para a entidade Pessoa.
 * Usado para expor dados da Pessoa ao Front-end de forma segura e padronizada.
 * * @param id ID único da Pessoa.
 * 
 * @param nomeCompleto   Nome completo.
 * @param email          E-mail (usado para login/identificação).
 * @param telefone       Telefone de contato.
 * @param dataNascimento Data em que o usuário nasceu
 * @param dataCadastro   Data em que o usuário se cadastrou.
 * @param ativo          Status de ativação da conta (Crucial no nosso fluxo).
 */
public record PessoaResponse(
        Long id,
        String nomeCompleto,
        String email,
        String telefone,
        LocalDate dataNascimento,
        LocalDateTime dataCadastro,
        boolean ativo,
        boolean isColaborador,
        boolean isCliente) {
    // O Record é final e imutável, ideal para DTOs de resposta.
    // A senha e o documento (CPF/CNPJ) são omitidos por segurança.
}
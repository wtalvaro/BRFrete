package br.com.wta.frete.core.controller.dto;

import br.com.wta.frete.core.entity.enums.StatusKYC;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * DTO de requisição para atualizar o status KYC da Conta Digital.
 */
@Data // Cria getters, setters, construtor sem argumentos, e métodos padrão
public class StatusKycUpdateRequest {

    /**
     * O novo status KYC. É obrigatório e deve ser um dos valores válidos do Enum
     * StatusKYC.
     */
    @NotNull(message = "O status KYC não pode ser nulo.")
    private StatusKYC statusKyc;
}
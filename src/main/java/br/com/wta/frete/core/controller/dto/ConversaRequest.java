package br.com.wta.frete.core.controller.dto;

import br.com.wta.frete.core.entity.enums.TipoConversa;
import jakarta.validation.constraints.NotNull;

/**
 * DTO de Requisição (Record) para iniciar uma nova Conversa.
 * Garante que os dados sejam imutáveis e concisos.
 */
public record ConversaRequest(

        // O tipo da conversa (ex: PRIVADA, GRUPO, SUPORTE) é obrigatório.
        // A anotação @NotNull é aplicada diretamente ao componente do Record.
        @NotNull(message = "O tipo da conversa é obrigatório.") TipoConversa tipoConversa) {
    // A compact constructor pode ser adicionado aqui para lógica de validação
    // adicional, se necessário.
}
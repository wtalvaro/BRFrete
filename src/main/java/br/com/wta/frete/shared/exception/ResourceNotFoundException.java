package br.com.wta.frete.shared.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exceção Padrão: ResourceNotFoundException
 * * Propósito: Ser lançada quando uma entidade (recurso) procurada por ID
 * não é encontrada.
 * * Mapeamento HTTP: Retorna o status 404 Not Found,
 * que é tratado pelo GlobalExceptionHandler.
 */
@ResponseStatus(HttpStatus.NOT_FOUND) // Define o status HTTP para 404
public class ResourceNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Construtor que aceita uma mensagem de erro.
     * 
     * @param message A mensagem detalhada do erro.
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
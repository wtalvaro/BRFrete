package br.com.wta.frete.shared.exception;

import java.io.Serial;

/**
 * Exceção de Negócio completa para sinalizar que os dados de entrada
 * (Request/DTO) são inválidos. * Inclui campos para: 1. message: Mensagem de
 * erro amigável para o cliente. 2. reasonCode: Código de erro interno para
 * categorização e documentação (ex: "CPF_INVALIDO"). 3. field: Campo específico
 * que causou a falha (opcional, ex: "cpf").
 */
public class InvalidDataException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

	private final String reasonCode;
	private final String field; // Campo opcional que causou a falha

	/**
	 * Construtor recomendado para a maioria dos erros de validação de dados.
	 * 
	 * @param message    Mensagem de erro legível para o cliente.
	 * @param reasonCode Código interno para identificar o tipo de erro (ex:
	 *                   "CNPJ_FORMATO_INVALIDO").
	 * @param field      Nome do campo (ex: "cnpj") que gerou o erro.
	 */
	public InvalidDataException(String message, String reasonCode, String field) {
		super(message);
		this.reasonCode = reasonCode;
		this.field = field;
	}

	/**
	 * Construtor para erros gerais de dados inválidos onde o campo específico não é
	 * relevante.
	 * 
	 * @param message    Mensagem de erro legível para o cliente.
	 * @param reasonCode Código interno para identificar o tipo de erro (ex:
	 *                   "REGRA_NEGOCIO_VIOLADA").
	 */
	public InvalidDataException(String message, String reasonCode) {
		this(message, reasonCode, null);
	}

	/**
	 * Construtor com causa raiz (Throwable)
	 */
	public InvalidDataException(String message, String reasonCode, String field, Throwable cause) {
		super(message, cause);
		this.reasonCode = reasonCode;
		this.field = field;
	}

	// --- Getters ---

	public String getReasonCode() {
		return reasonCode;
	}

	public String getField() {
		return field;
	}
}
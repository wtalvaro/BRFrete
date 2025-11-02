package br.com.wta.frete.shared.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.FieldError; // Novo Import
import org.springframework.web.bind.MethodArgumentNotValidException; // Novo Import
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.net.URI;
import java.time.Instant;
import java.util.HashMap; // Novo Import
import java.util.Map; // Novo Import

/**
 * Componente global de tratamento de exceções (@ControllerAdvice). Responsável
 * por interceptar exceções e convertê-las em respostas HTTP padronizadas
 * utilizando o formato Problem Details (RFC 7807).
 */
@ControllerAdvice
public class GlobalExceptionHandler {

	/**
	 * NOVO: Trata exceções de falha de validação de DTOs (HTTP 400 Bad Request)
	 * lançadas automaticamente pelo Spring MVC (@Valid, @Validated).
	 */
	@SuppressWarnings("null")
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ProblemDetail handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
		ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);

		problemDetail.setTitle("Falha na Validação dos Dados de Entrada");
		problemDetail.setDetail("Um ou mais campos da requisição contêm erros. Verifique a lista de erros.");

		// Coleta todos os erros de campo e suas mensagens
		Map<String, String> validationErrors = new HashMap<>();
		for (FieldError error : ex.getBindingResult().getFieldErrors()) {
			// Adiciona o nome do campo e a mensagem de erro padrão
			validationErrors.put(error.getField(), error.getDefaultMessage());
		}

		problemDetail.setProperty("validationErrors", validationErrors); // Adiciona o mapa ao ProblemDetail
		problemDetail.setType(URI.create("/docs/erros#validation-failure"));
		problemDetail.setProperty("timestamp", Instant.now());

		return problemDetail;
	}

	/**
	 * Trata exceções de Validação/Dados Inválidos de REGRA DE NEGÓCIO (HTTP 400 Bad
	 * Request). Esta exceção deve ser lançada pelo seu Service
	 * (InvalidDataException).
	 */
	@SuppressWarnings("null")
	@ExceptionHandler(InvalidDataException.class)
	public ProblemDetail handleInvalidDataException(InvalidDataException ex) {
		ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);

		problemDetail.setTitle("Dados de Entrada Inválidos");
		problemDetail.setDetail(ex.getMessage());

		// --- Adiciona os novos campos da exceção ao ProblemDetail ---
		problemDetail.setProperty("reasonCode", ex.getReasonCode());

		if (ex.getField() != null) {
			problemDetail.setProperty("field", ex.getField());
		}
		// -----------------------------------------------------------

		problemDetail.setType(URI.create("/docs/erros#" + ex.getReasonCode().toLowerCase()));
		problemDetail.setProperty("timestamp", Instant.now());

		return problemDetail;
	}

	/**
	 * Handler genérico para capturar qualquer Exception não tratada (HTTP 500
	 * Internal Server Error). Retorna um erro genérico e seguro para o cliente.
	 */
	@SuppressWarnings("null")
	@ExceptionHandler(Exception.class)
	public ProblemDetail handleGenericException(Exception ex) {
		// Logar a stack trace completa no servidor para investigação
		ex.printStackTrace();

		ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);

		// Mensagem genérica para o cliente, ocultando detalhes internos
		problemDetail.setTitle("Erro Interno do Servidor");
		problemDetail.setDetail("Ocorreu um erro interno inesperado. Tente novamente mais tarde.");
		problemDetail.setType(URI.create("/docs/erros#internal-error"));
		problemDetail.setProperty("timestamp", Instant.now());

		return problemDetail;
	}
}
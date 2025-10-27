package br.com.wta.frete.shared.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail; // Importação chave do Spring Boot 3+
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.net.URI;
import java.time.Instant; // Opcional, para adicionar timestamp

/**
 * Componente global de tratamento de exceções (@ControllerAdvice). Responsável
 * por interceptar exceções e convertê-las em respostas HTTP padronizadas
 * utilizando o formato Problem Details (RFC 7807).
 */
@ControllerAdvice
public class GlobalExceptionHandler {

	// O Spring MVC converte automaticamente o ProblemDetail para a resposta HTTP
	// com o status e corpo corretos.

	/**
	 * Trata exceções de Validação/Dados Inválidos (HTTP 400 Bad Request).
	 */
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

		return problemDetail;
	}

	// Exemplo de como adicionar um handler para uma exceção de Recurso Não
	// Encontrado (404)
	/*
	 * @ExceptionHandler(ResourceNotFoundException.class) public ProblemDetail
	 * handleResourceNotFoundException(ResourceNotFoundException ex) { ProblemDetail
	 * problemDetail = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
	 * 
	 * problemDetail.setTitle("Recurso Não Encontrado");
	 * problemDetail.setDetail(ex.getMessage());
	 * problemDetail.setType(URI.create("/docs/erros#not-found"));
	 * problemDetail.setProperty("timestamp", Instant.now());
	 * 
	 * return problemDetail; }
	 */

	/**
	 * Handler genérico para capturar qualquer Exception não tratada (HTTP 500
	 * Internal Server Error). Retorna um erro genérico e seguro para o cliente.
	 */
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
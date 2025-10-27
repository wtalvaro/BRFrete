package br.com.wta.frete.logistica.controller.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank; // Adicionado para a descrição

/**
 * DTO de Requisição para atualizar dados de um Parâmetro ANTT
 * (logistica.antt_parametros).
 */
public record AnttParametroRequest(
		// Adicionamos o ID para operações de UPDATE/PATCH que usam o corpo
		@NotNull(message = "O ID do parâmetro é obrigatório para atualização") Integer anttParametroId,

		@NotNull(message = "O valor é obrigatório") @DecimalMin(value = "0.0", inclusive = false, message = "O valor deve ser positivo") BigDecimal valor,

		@NotBlank(message = "A descrição não pode ser vazia") // Assumindo que a descrição é relevante
		String descricao,

		@NotNull(message = "A data de vigência é obrigatória") // Assumindo que toda atualização tem uma vigência
		LocalDate dataVigencia) {
	// NOTA: Se este DTO for usado em um PATCH onde campos são opcionais,
	// as anotações @NotNull e @NotBlank precisariam ser removidas
	// e o Service lidaria com os nulos. Mantivemos a versão mais estrita aqui.
}
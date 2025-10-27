package br.com.wta.frete.social.controller.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * DTO para registrar uma Avaliação (social.avaliacoes). Contém a lógica de
 * validação para a chave polimórfica (OrdemServico OU Produto).
 */
public record AvaliacaoRequest(
		// Quem avalia
		@NotNull(message = "O ID do avaliador é obrigatório") Long avaliadorId,

		// Quem está sendo avaliado
		@NotNull(message = "O ID do avaliado é obrigatório") Long avaliadoId,

		// Chaves estrangeiras condicionais:
		Long ordemServicoId, // Opcional
		Integer produtoId, // Opcional

		@NotNull(message = "A nota é obrigatória") @Min(value = 1, message = "A nota mínima é 1") @Max(value = 5, message = "A nota máxima é 5") Integer nota,

		@Size(max = 1000, message = "O comentário deve ter no máximo 1000 caracteres") String comentario) {
	/**
	 * Construtor Compacto com validação de regra de negócio para polimorfismo.
	 * Garante que APENAS UM dos campos (ordemServicoId ou produtoId) esteja
	 * preenchido.
	 */
	public AvaliacaoRequest {
		boolean isOrdemServicoPresent = ordemServicoId != null;
		boolean isProdutoPresent = produtoId != null;

		// Regra: Deve ter exatamente um dos dois (uso do XOR lógico ^)
		if (isOrdemServicoPresent == isProdutoPresent) {
			// Em vez de retornar false, lançamos uma exceção para integrar com o fluxo de
			// validação do Spring/Jakarta.
			String message = "A avaliação deve ser associada a EXATAMENTE UMA chave: ordemServicoId OU produtoId, mas não ambas e nem nenhuma.";

			// Para integração com a validação do Spring/Jakarta, é comum usar uma exceção
			// customizada.
			// Aqui, lançaremos uma IllegalArgumentException simples, mas no Service,
			// você pode usar uma Bean Validation Exception customizada.
			throw new IllegalArgumentException(message);
		}
	}
}
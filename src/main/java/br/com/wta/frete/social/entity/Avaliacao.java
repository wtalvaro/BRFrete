package br.com.wta.frete.social.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.ZonedDateTime;

import br.com.wta.frete.core.entity.Pessoa;
import br.com.wta.frete.logistica.entity.OrdemServico;
import br.com.wta.frete.marketplace.entity.Produto;

/**
 * Mapeia a tabela 'social.avaliacoes'. Implementa a lógica polimórfica para
 * avaliar OrdemServico OU Produto, mas nunca ambos.
 */
@Entity
@Table(name = "avaliacoes", schema = "social")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Avaliacao {

	/**
	 * Chave primária (BIGSERIAL). Mapeado para Long.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "avaliacao_id")
	private Long id;

	// --- Relacionamentos (Chaves Estrangeiras) ---

	/**
	 * Pessoa que fez a avaliação (avaliador_id BIGINT NOT NULL). Mapeado para a
	 * entidade Pessoa.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "avaliador_id", nullable = false)
	private Pessoa avaliador;

	/**
	 * Pessoa que foi avaliada (avaliado_id BIGINT NOT NULL). Mapeado para a
	 * entidade Pessoa.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "avaliado_id", nullable = false)
	private Pessoa avaliado;

	// --- Chaves Estrangeiras Condicionais (Polimórficas) ---

	/**
	 * Ordem de Serviço avaliada (ordem_servico_id BIGINT). Pode ser NULL.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ordem_servico_id", nullable = true) // nullable = true para ser opcional
	private OrdemServico ordemServico;

	/**
	 * Produto avaliado (produto_id INTEGER). Pode ser NULL.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "produto_id", nullable = true) // nullable = true para ser opcional
	private Produto produto;

	// --- Dados da Avaliação ---

	/**
	 * Nota da avaliação (INTEGER NOT NULL). Restrição de 1 a 5 no SQL, mas aqui
	 * usamos a lógica de aplicação.
	 */
	@Column(name = "nota", nullable = false)
	private Integer nota;

	/**
	 * Comentário opcional da avaliação (TEXT).
	 */
	@Column(name = "comentario", columnDefinition = "TEXT")
	private String comentario;

	/**
	 * Data e hora da avaliação (TIMESTAMP WITH TIME ZONE DEFAULT
	 * CURRENT_TIMESTAMP).
	 */
	@Column(name = "data_avaliacao", columnDefinition = "TIMESTAMP WITH TIME ZONE")
	private ZonedDateTime dataAvaliacao = ZonedDateTime.now();

	/**
	 * Método de ciclo de vida do JPA para garantir a regra de negócio (CHECK
	 * CONSTRAINT SQL) antes de persistir ou atualizar no banco de dados.
	 */
	@PrePersist
	@PreUpdate
	private void validateRelatedEntity() {
		boolean isOrdemServicoPresent = this.ordemServico != null;
		boolean isProdutoPresent = this.produto != null;

		// Regra SQL: (ordem_servico_id IS NOT NULL AND produto_id IS NULL) OR
		// (ordem_servico_id IS NULL AND produto_id IS NOT NULL)
		// Deve ter exatamente uma das duas chaves preenchidas.
		if (isOrdemServicoPresent == isProdutoPresent) {
			throw new IllegalStateException(
					"Avaliação deve estar relacionada a exatamente uma entidade: ou Ordem de Serviço, ou Produto.");
		}
	}
}
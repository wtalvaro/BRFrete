package br.com.wta.frete.logistica.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

import br.com.wta.frete.colaboradores.entity.Transportador;

/**
 * Mapeia a tabela 'logistica.lances'. Registra as propostas de preço feitas
 * pelos Transportadores para um Frete.
 */
@Entity
@Table(name = "lances", schema = "logistica")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Lance {

	/**
	 * Chave primária (BIGSERIAL). Mapeado para Long.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "lance_id")
	private Long id;

	// --- Relacionamentos (Chaves Estrangeiras) ---

	/**
	 * Frete ao qual este lance se refere (ordem_servico_id BIGINT NOT NULL).
	 * Relacionamento Many-to-One com Frete.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ordem_servico_id", nullable = false)
	private Frete frete;

	/**
	 * Transportador que fez o lance (transportador_pessoa_id BIGINT NOT NULL).
	 * Relacionamento Many-to-One com Transportador.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "transportador_pessoa_id", nullable = false)
	private Transportador transportador;

	// --- Detalhes do Lance ---

	/**
	 * Valor proposto pelo Transportador (NUMERIC(10, 2) NOT NULL).
	 */
	@Column(name = "valor_proposto", nullable = false, precision = 10, scale = 2)
	private BigDecimal valorProposto;

	/**
	 * Data e hora em que o lance foi registrado (TIMESTAMP WITH TIME ZONE DEFAULT
	 * CURRENT_TIMESTAMP).
	 */
	@Column(name = "data_lance", columnDefinition = "TIMESTAMP WITH TIME ZONE")
	private ZonedDateTime dataLance = ZonedDateTime.now();

	/**
	 * Indica se o lance foi o lance vencedor (BOOLEAN NOT NULL DEFAULT false).
	 */
	@Column(name = "vencedor", nullable = false)
	private Boolean vencedor = false;
}
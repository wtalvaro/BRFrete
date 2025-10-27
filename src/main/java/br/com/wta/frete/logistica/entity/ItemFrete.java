package br.com.wta.frete.logistica.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;

/**
 * Mapeia a tabela 'logistica.itens_frete'. Detalha os materiais ou produtos que
 * compõem um Frete.
 */
@Entity
@Table(name = "itens_frete", schema = "logistica")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemFrete {

	/**
	 * Chave primária (BIGSERIAL). Mapeado para Long.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "item_id")
	private Long id;

	/**
	 * Chave estrangeira para o Frete (ordem_servico_id BIGINT NOT NULL).
	 * Relacionamento Many-to-One: Muitos Itens para Um Frete.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ordem_servico_id", nullable = false)
	private Frete frete;

	/**
	 * Descrição do item (VARCHAR(255) NOT NULL).
	 */
	@Column(name = "descricao_item", nullable = false, length = 255)
	private String descricaoItem;

	/**
	 * Quantidade em peso do item (NUMERIC(10, 2) NOT NULL).
	 */
	@Column(name = "quantidade_peso_kg", nullable = false, precision = 10, scale = 2)
	private BigDecimal quantidadePesoKg;

	/**
	 * Volume cúbico do item, se aplicável (NUMERIC(10, 2)).
	 */
	@Column(name = "volume_m3", precision = 10, scale = 2)
	private BigDecimal volumeM3;

	/**
	 * Valor unitário estimado do item (NUMERIC(10, 2)).
	 */
	@Column(name = "valor_estimado_unitario", precision = 10, scale = 2)
	private BigDecimal valorEstimadoUnitario;
}
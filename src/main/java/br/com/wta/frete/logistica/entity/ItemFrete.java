package br.com.wta.frete.logistica.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;

/**
 * Mapeia a tabela 'logistica.itens_frete'. Detalha os materiais ou produtos que
 * compõem um Frete.
 * * CORREÇÕES: Alinhamento de nomes de colunas com o schema SQL.
 */
@Entity
@Table(name = "itens_frete", schema = "logistica")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemFrete {

	/**
	 * Chave primária (BIGSERIAL -> SERIAL).
	 * NOME CORRIGIDO: de 'item_id' para 'item_frete_id'.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "item_frete_id")
	private Long id;

	/**
	 * Chave estrangeira para o Frete (frete_id INTEGER NOT NULL).
	 * NOME CORRIGIDO: de 'ordem_servico_id' para 'frete_id'.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "frete_id", nullable = false)
	private Frete frete;

	/**
	 * Descrição do item (TEXT NOT NULL).
	 * NOME CORRIGIDO: de 'descricao_item' para 'descricao'.
	 */
	@Column(name = "descricao", nullable = false, columnDefinition = "TEXT")
	private String descricao;

	/**
	 * Tipo do material (VARCHAR(100)).
	 * NOVO CAMPO: 'tipo_material'.
	 */
	@Column(name = "tipo_material", length = 100)
	private String tipoMaterial;

	/**
	 * Quantidade em peso do item (NUMERIC(10, 2) NOT NULL).
	 * NOME CORRIGIDO: de 'quantidade_peso_kg' para 'peso_estimado_kg'.
	 */
	@Column(name = "peso_estimado_kg", nullable = false, precision = 10, scale = 2)
	private BigDecimal pesoEstimadoKg;

	/**
	 * Volume cúbico do item, se aplicável (NUMERIC(10, 2)).
	 * NOME CORRIGIDO: de 'volume_m3' para 'volume_estimado_m3'.
	 */
	@Column(name = "volume_estimado_m3", precision = 10, scale = 2)
	private BigDecimal volumeEstimadoM3;

	/**
	 * CAMPO REMOVIDO: 'valor_estimado_unitario' não existe na tabela SQL.
	 */
	// private BigDecimal valorEstimadoUnitario;
}
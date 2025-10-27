package br.com.wta.frete.inventario.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

import br.com.wta.frete.colaboradores.entity.Sucateiro;

/**
 * Mapeia a tabela 'inventario.estoque'. Representa o estoque de materiais de
 * sucata/reciclagem gerenciado pelos Sucateiros.
 */
@Entity
@Table(name = "estoque", schema = "inventario")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EstoqueSucata {

	/**
	 * Chave primária (BIGSERIAL). Mapeado para Long.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "estoque_id")
	private Long id;

	// --- Relacionamento (Chave Estrangeira) ---

	/**
	 * Sucateiro proprietário do estoque (sucateiro_pessoa_id BIGINT NOT NULL).
	 * Relacionamento Many-to-One com Sucateiro.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sucateiro_pessoa_id", nullable = false)
	private Sucateiro sucateiro;

	// --- Dados do Estoque ---

	/**
	 * Nome do material em estoque (VARCHAR(100) NOT NULL).
	 */
	@Column(name = "nome_material", nullable = false, length = 100)
	private String nomeMaterial;

	/**
	 * Quantidade atual em peso (NUMERIC(10, 2) NOT NULL).
	 */
	@Column(name = "quantidade_peso_kg", nullable = false, precision = 10, scale = 2)
	private BigDecimal quantidadePesoKg;

	/**
	 * Status da qualidade ou classificação do material (VARCHAR(50)).
	 */
	@Column(name = "status_qualidade", length = 50)
	private String statusQualidade;

	/**
	 * Data da última atualização do estoque (TIMESTAMP WITH TIME ZONE DEFAULT
	 * CURRENT_TIMESTAMP).
	 */
	@Column(name = "data_atualizacao", columnDefinition = "TIMESTAMP WITH TIME ZONE")
	private ZonedDateTime dataAtualizacao = ZonedDateTime.now();
}
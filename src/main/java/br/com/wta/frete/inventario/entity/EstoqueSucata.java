package br.com.wta.frete.inventario.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import br.com.wta.frete.colaboradores.entity.Sucateiro;
import br.com.wta.frete.inventario.entity.enums.TipoMaterialEnum; // NOVO IMPORT

/**
 * Mapeia a tabela 'inventario.estoque'. Representa o estoque de materiais de
 * sucata/reciclagem gerenciado pelos Sucateiros.
 * ATUALIZAÇÃO: O campo 'nomeMaterial' agora usa o ENUM TipoMaterialEnum.
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
	 * Tipo de material em estoque (ENUM inventario.tipo_material_enum).
	 */
	@Enumerated(EnumType.STRING)
	@JdbcTypeCode(SqlTypes.NAMED_ENUM)
	@Column(name = "tipo_material", nullable = false) // Coluna alinhada com o schema SQL
	private TipoMaterialEnum tipoMaterial; // Campo usando o ENUM

	/**
	 * Quantidade atual em peso (NUMERIC(10, 2) NOT NULL).
	 * Coluna no DB: quantidade_kg
	 */
	@Column(name = "quantidade_kg", nullable = false, precision = 10, scale = 2)
	private BigDecimal quantidadePesoKg;

	/**
	 * Status da qualidade ou classificação do material (VARCHAR(50)).
	 */
	@Column(name = "status_qualidade", length = 50)
	private String statusQualidade;

	/**
	 * Localização física do estoque (VARCHAR(100)).
	 */
	@Column(name = "localizacao", length = 100)
	private String localizacao;

	/**
	 * Data da última atualização do estoque (TIMESTAMP WITH TIME ZONE DEFAULT
	 * CURRENT_TIMESTAMP).
	 */
	@Column(name = "data_atualizacao", columnDefinition = "TIMESTAMP WITH TIME ZONE")
	private ZonedDateTime dataAtualizacao = ZonedDateTime.now();
}
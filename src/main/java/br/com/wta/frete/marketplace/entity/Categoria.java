package br.com.wta.frete.marketplace.entity;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import br.com.wta.frete.marketplace.entity.enums.TipoGeralEnum;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * Mapeia a tabela 'marketplace.categorias'. Define as categorias para os
 * produtos do marketplace.
 */
@Entity
@Table(name = "categorias", schema = "marketplace", uniqueConstraints = {
		@UniqueConstraint(columnNames = "nome_categoria") })
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Categoria {

	/**
	 * Chave primária (SERIAL). Mapeado para Integer.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "categoria_id")
	private Integer id;

	/**
	 * Nome único da categoria (VARCHAR(100) UNIQUE NOT NULL).
	 */
	@Column(name = "nome_categoria", nullable = false, length = 100)
	private String nomeCategoria;

	/**
	 * Tipo geral da categoria (ENUM no BD: marketplace.tipo_geral_enum).
	 */
	@Enumerated(EnumType.STRING) // Define que o valor do Enum será armazenado como String no BD
	@JdbcTypeCode(SqlTypes.NAMED_ENUM) // Mapeia o ENUM corretamente para o PostgreSQL
	@Column(name = "tipo_geral", nullable = false)
	private TipoGeralEnum tipoGeral;
}
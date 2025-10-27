package br.com.wta.frete.marketplace.entity;

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
	 * Descrição da categoria (TEXT).
	 */
	@Column(name = "descricao", columnDefinition = "TEXT")
	private String descricao;
}
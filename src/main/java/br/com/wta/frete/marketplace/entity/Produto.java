package br.com.wta.frete.marketplace.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

import br.com.wta.frete.colaboradores.entity.Lojista;

/**
 * Mapeia a tabela 'marketplace.produtos'. Representa um produto disponível para
 * venda no marketplace.
 */
@Entity
@Table(name = "produtos", schema = "marketplace", uniqueConstraints = {
		@UniqueConstraint(columnNames = { "sku", "lojista_pessoa_id" }) // SKU deve ser único por Lojista
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Produto {

	/**
	 * Chave primária (SERIAL). Mapeado para Integer.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "produto_id")
	private Integer id;

	// --- Relacionamentos (Chaves Estrangeiras) ---

	/**
	 * Lojista que está vendendo o produto (lojista_pessoa_id BIGINT NOT NULL).
	 * Relacionamento Many-to-One com Lojista.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "lojista_pessoa_id", nullable = false)
	private Lojista lojista;

	/**
	 * Categoria do produto (categoria_id INTEGER NOT NULL). Relacionamento
	 * Many-to-One com Categoria.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "categoria_id", nullable = false)
	private Categoria categoria;

	// --- Dados do Produto ---

	/**
	 * Nome do produto (VARCHAR(255) NOT NULL).
	 */
	@Column(name = "nome_produto", nullable = false, length = 255)
	private String nomeProduto;

	/**
	 * Descrição detalhada do produto (TEXT).
	 */
	@Column(name = "descricao", columnDefinition = "TEXT")
	private String descricao;

	/**
	 * SKU ou código do produto no inventário do lojista (VARCHAR(50) NOT NULL).
	 */
	@Column(name = "sku", nullable = false, length = 50)
	private String sku;

	/**
	 * Preço unitário de venda (NUMERIC(10, 2) NOT NULL).
	 */
	@Column(name = "preco", nullable = false, precision = 10, scale = 2)
	private BigDecimal preco;

	/**
	 * Peso do item em kg, para cálculo de frete (NUMERIC(10, 2)).
	 */
	@Column(name = "peso_kg", precision = 10, scale = 2)
	private BigDecimal pesoKg;

	/**
	 * Status de disponibilidade (VARCHAR(20) NOT NULL DEFAULT 'DISPONIVEL').
	 */
	@Column(name = "status_disponibilidade", nullable = false, length = 20)
	private String statusDisponibilidade = "DISPONIVEL";

	/**
	 * Data de listagem do produto (TIMESTAMP WITH TIME ZONE DEFAULT
	 * CURRENT_TIMESTAMP).
	 */
	@Column(name = "data_listagem", columnDefinition = "TIMESTAMP WITH TIME ZONE")
	private ZonedDateTime dataListagem = ZonedDateTime.now();
}
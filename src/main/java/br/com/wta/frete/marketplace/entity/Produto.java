package br.com.wta.frete.marketplace.entity;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import br.com.wta.frete.colaboradores.entity.Lojista;
import br.com.wta.frete.marketplace.entity.enums.UnidadeMedidaEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Mapeia a tabela 'marketplace.produtos'. Representa um produto disponível para
 * venda no marketplace.
 * * ATUALIZAÇÃO: Alinhamento com o esquema V1__Initial_Schema.sql.
 * - Renomeado: nomeProduto para titulo.
 * - Renomeado: dataListagem para dataPublicacao.
 * - Adicionado: quantidade, unidadeMedida, isDisponivel, isDoacao.
 * - Adicionada a restrição de unicidade para SKU/Lojista (conforme SQL).
 */
@Entity
@Table(name = "produtos", schema = "marketplace", uniqueConstraints = {
		@UniqueConstraint(columnNames = { "sku", "vendedor_id" }) // SKU deve ser único por Lojista (vendedor_id)
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
	 * Lojista que está vendendo o produto (vendedor_id BIGINT NOT NULL).
	 * Relacionamento Many-to-One com Lojista.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "vendedor_id", nullable = false)
	private Lojista lojista;

	/**
	 * Categoria do produto (categoria_id INTEGER NOT NULL). Relacionamento
	 * Many-to-One com Categoria.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "categoria_id", nullable = false)
	private Categoria categoria;

	// --- Dados do Produto (ATUALIZADOS) ---

	/**
	 * Título/Nome do produto (titulo VARCHAR(255) NOT NULL) - Anteriormente:
	 * nome_produto.
	 */
	@Column(name = "titulo", nullable = false, length = 255)
	private String titulo; // Renomeado para 'titulo'

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
	 * Quantidade de itens disponíveis (INTEGER NOT NULL DEFAULT 1).
	 */
	@Column(name = "quantidade", nullable = false)
	private Integer quantidade; // Novo campo

	/**
	 * Unidade de medida (ENUM marketplace.unidade_medida_enum).
	 * NOTA: Assumindo que você tem uma classe Java para 'UnidadeMedidaEnum'.
	 * Por simplicidade no mapeamento, usaremos 'String' ou o Enum customizado se
	 * existir.
	 */
	// Se a classe UnidadeMedidaEnum existir, use: @Enumerated(EnumType.STRING)
	@Column(name = "unidade_medida", nullable = false, length = 10)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM) // Mapeia o ENUM corretamente para o PostgreSQL
	@Enumerated(EnumType.STRING)
	private UnidadeMedidaEnum unidadeMedida; // Novo campo

	/**
	 * Se o produto está disponível para venda (BOOLEAN NOT NULL DEFAULT true).
	 */
	@Column(name = "is_disponivel", nullable = false)
	private Boolean isDisponivel; // Novo campo

	/**
	 * Se o produto é uma doação (BOOLEAN NOT NULL DEFAULT false).
	 */
	@Column(name = "is_doacao", nullable = false)
	private Boolean isDoacao; // Novo campo

	/**
	 * Data de publicação/listagem do produto (TIMESTAMP WITH TIME ZONE) -
	 * Anteriormente: data_listagem.
	 */
	@Column(name = "data_publicacao", columnDefinition = "TIMESTAMP WITH TIME ZONE")
	private ZonedDateTime dataPublicacao = ZonedDateTime.now(); // Renomeado para 'dataPublicacao'
}
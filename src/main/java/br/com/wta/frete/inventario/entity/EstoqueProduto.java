package br.com.wta.frete.inventario.entity;

import java.time.ZonedDateTime;

import br.com.wta.frete.marketplace.entity.Produto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Mapeia a tabela 'inventario.estoque_produto' (chave derivada via @MapsId).
 * O campo 'quantidade' é a única fonte de verdade para o saldo em estoque,
 * em alinhamento com a remoção da coluna redundante em marketplace.produtos.
 */
@Entity
@Table(name = "estoque_produto", schema = "inventario")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EstoqueProduto {

	/**
	 * Chave primária (produto_id INTEGER). Usa o mesmo valor da chave primária da
	 * entidade Produto (@MapsId).
	 */
	@Id
	@Column(name = "produto_id")
	private Integer produtoId;

	/**
	 * Relacionamento Um-para-Um com Produto.
	 */
	@OneToOne(fetch = FetchType.LAZY)
	@MapsId // Mapeia a chave primária (produto_id) para a PK de Produto
	@JoinColumn(name = "produto_id", nullable = false)
	private Produto produto;

	/**
	 * Quantidade em estoque (INTEGER NOT NULL).
	 */
	@Column(name = "quantidade", nullable = false)
	private Integer quantidade;

	/**
	 * Ponto de reposição ou alerta (INTEGER).
	 */
	@Column(name = "ponto_reposicao")
	private Integer pontoReposicao;

	/**
	 * Localização física do estoque (VARCHAR(100)).
	 */
	@Column(name = "localizacao", length = 100)
	private String localizacao;

	/**
	 * Data da última movimentação/atualização (TIMESTAMP WITH TIME ZONE).
	 */
	@Column(name = "ultima_atualizacao", columnDefinition = "TIMESTAMP WITH TIME ZONE")
	private ZonedDateTime ultimaAtualizacao = ZonedDateTime.now();
}
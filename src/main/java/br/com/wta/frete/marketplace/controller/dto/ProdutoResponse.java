package br.com.wta.frete.marketplace.controller.dto;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * DTO de Resposta para a entidade Produto (marketplace.produtos), transformado
 * em um POJO (Plain Old Java Object) imutável.
 *
 * NOTA TÉCNICA: A escolha de utilizar um POJO imutável, em detrimento do
 * recurso 'record' do Java (que simplificaria o código), foi motivada por
 * **incompatibilidade no ambiente de desenvolvimento (STS/Eclipse JDT)**. * O
 * processamento de anotações (APT) do MapStruct, ao inspecionar a classe
 * 'record', causava um **Internal error / NullPointerException** na IDE. A
 * manutenção desta classe como POJO imutável garante a estabilidade da
 * compilação no ambiente de desenvolvimento. * Imutabilidade garantida por: 1.
 * Classe declarada como 'final' (não pode ser estendida). 2. Todos os campos
 * são 'private final' (não podem ser alterados após a criação). 3. Ausência de
 * métodos 'setters'.
 */
public final class ProdutoResponse {

	private final Integer produtoId;
	private final Long lojistaPessoaId;
	private final Integer categoriaId;
	private final String nomeProduto;
	private final String descricao;
	private final String sku;
	private final BigDecimal preco;
	private final BigDecimal pesoKg;
	private final String statusDisponibilidade;
	private final ZonedDateTime dataListagem;

	/**
	 * Construtor canônico para inicializar todos os campos. É o único ponto onde os
	 * valores dos campos 'final' podem ser definidos.
	 */
	public ProdutoResponse(Integer produtoId, Long lojistaPessoaId, Integer categoriaId, String nomeProduto,
			String descricao, String sku, BigDecimal preco, BigDecimal pesoKg, String statusDisponibilidade,
			ZonedDateTime dataListagem) {
		this.produtoId = produtoId;
		this.lojistaPessoaId = lojistaPessoaId;
		this.categoriaId = categoriaId;
		this.nomeProduto = nomeProduto;
		this.descricao = descricao;
		this.sku = sku;
		this.preco = preco;
		this.pesoKg = pesoKg;
		this.statusDisponibilidade = statusDisponibilidade;
		this.dataListagem = dataListagem;
	}

	// --- Métodos de Acesso (Getters) ---

	public Integer getProdutoId() {
		return produtoId;
	}

	public Long getLojistaPessoaId() {
		return lojistaPessoaId;
	}

	public Integer getCategoriaId() {
		return categoriaId;
	}

	public String getNomeProduto() {
		return nomeProduto;
	}

	public String getDescricao() {
		return descricao;
	}

	public String getSku() {
		return sku;
	}

	public BigDecimal getPreco() {
		return preco;
	}

	public BigDecimal getPesoKg() {
		return pesoKg;
	}

	public String getStatusDisponibilidade() {
		return statusDisponibilidade;
	}

	public ZonedDateTime getDataListagem() {
		return dataListagem;
	}

	// --- Métodos Padrão Sobrescritos ---

	/**
	 * Compara se este objeto é igual a outro, baseando-se em todos os campos.
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		ProdutoResponse that = (ProdutoResponse) o;
		return Objects.equals(produtoId, that.produtoId) && Objects.equals(lojistaPessoaId, that.lojistaPessoaId)
				&& Objects.equals(categoriaId, that.categoriaId) && Objects.equals(nomeProduto, that.nomeProduto)
				&& Objects.equals(descricao, that.descricao) && Objects.equals(sku, that.sku)
				&& Objects.equals(preco, that.preco) && Objects.equals(pesoKg, that.pesoKg)
				&& Objects.equals(statusDisponibilidade, that.statusDisponibilidade)
				&& Objects.equals(dataListagem, that.dataListagem);
	}

	/**
	 * Gera um código de hash único, baseado em todos os campos. Essencial para o
	 * funcionamento correto em coleções hash (como HashMap).
	 */
	@Override
	public int hashCode() {
		return Objects.hash(produtoId, lojistaPessoaId, categoriaId, nomeProduto, descricao, sku, preco, pesoKg,
				statusDisponibilidade, dataListagem);
	}

	/**
	 * Retorna uma representação textual do objeto, útil para logs e depuração.
	 */
	@Override
	public String toString() {
		return "ProdutoResponse{" + "produtoId=" + produtoId + ", lojistaPessoaId=" + lojistaPessoaId + ", categoriaId="
				+ categoriaId + ", nomeProduto='" + nomeProduto + '\'' + ", descricao='" + descricao + '\'' + ", sku='"
				+ sku + '\'' + ", preco=" + preco + ", pesoKg=" + pesoKg + ", statusDisponibilidade='"
				+ statusDisponibilidade + '\'' + ", dataListagem=" + dataListagem + '}';
	}
}
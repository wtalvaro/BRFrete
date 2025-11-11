package br.com.wta.frete.marketplace.controller.dto;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * DTO de Resposta para a entidade Produto (marketplace.produtos), transformado
 * em um POJO (Plain Old Java Object) imutável.
 *
 * ATUALIZAÇÃO: Alinhamento com a Entidade 'Produto'.
 * - Renomeado: nomeProduto para titulo.
 * - Adicionado: quantidade, unidadeMedida, isDoacao, isDisponivel.
 * - Renomeado: dataListagem para dataPublicacao.
 */
public final class ProdutoResponse {

	private final Integer produtoId;
	private final Long lojistaPessoaId;
	private final Integer categoriaId;
	private final String titulo; // Renomeado
	private final String descricao;
	private final String sku;
	private final BigDecimal preco;
	private final String unidadeMedida; // Novo campo
	private final Boolean isDoacao; // Novo campo
	private final Boolean isDisponivel; // Novo campo
	private final String statusDisponibilidade;
	private final ZonedDateTime dataPublicacao; // Renomeado

	/**
	 * Construtor canônico para inicializar todos os campos.
	 */
	public ProdutoResponse(Integer produtoId, Long lojistaPessoaId, Integer categoriaId, String titulo,
			String descricao, String sku, BigDecimal preco, Integer quantidade, String unidadeMedida, Boolean isDoacao,
			Boolean isDisponivel, String statusDisponibilidade, ZonedDateTime dataPublicacao) {
		this.produtoId = produtoId;
		this.lojistaPessoaId = lojistaPessoaId;
		this.categoriaId = categoriaId;
		this.titulo = titulo;
		this.descricao = descricao;
		this.sku = sku;
		this.preco = preco;
		this.unidadeMedida = unidadeMedida;
		this.isDoacao = isDoacao;
		this.isDisponivel = isDisponivel;
		this.statusDisponibilidade = statusDisponibilidade;
		this.dataPublicacao = dataPublicacao;
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

	public String getTitulo() { // Renomeado
		return titulo;
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

	public String getUnidadeMedida() { // Novo Getter
		return unidadeMedida;
	}

	public Boolean getIsDoacao() { // Novo Getter
		return isDoacao;
	}

	public Boolean getIsDisponivel() { // Novo Getter
		return isDisponivel;
	}

	public String getStatusDisponibilidade() {
		return statusDisponibilidade;
	}

	public ZonedDateTime getDataPublicacao() { // Renomeado
		return dataPublicacao;
	}

	// --- Métodos Padrão Sobrescritos ---

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		ProdutoResponse that = (ProdutoResponse) o;
		return Objects.equals(produtoId, that.produtoId) && Objects.equals(lojistaPessoaId, that.lojistaPessoaId)
				&& Objects.equals(categoriaId, that.categoriaId) && Objects.equals(titulo, that.titulo)
				&& Objects.equals(descricao, that.descricao) && Objects.equals(sku, that.sku)
				&& Objects.equals(unidadeMedida, that.unidadeMedida) && Objects.equals(isDoacao, that.isDoacao)
				&& Objects.equals(isDisponivel, that.isDisponivel)
				&& Objects.equals(statusDisponibilidade, that.statusDisponibilidade)
				&& Objects.equals(dataPublicacao, that.dataPublicacao);
	}

	@Override
	public int hashCode() {
		return Objects.hash(produtoId, lojistaPessoaId, categoriaId, titulo, descricao, sku, preco,
				unidadeMedida, isDoacao, isDisponivel, statusDisponibilidade, dataPublicacao);
	}

	@Override
	public String toString() {
		return "ProdutoResponse{" + "produtoId=" + produtoId + ", lojistaPessoaId=" + lojistaPessoaId + ", categoriaId="
				+ categoriaId + ", titulo='" + titulo + '\'' + ", descricao='" + descricao + '\'' + ", sku='" + sku
				+ '\'' + ", preco=" + preco + ", unidadeMedida='" + unidadeMedida
				+ '\'' + ", isDoacao=" + isDoacao + ", isDisponivel=" + isDisponivel + ", statusDisponibilidade='"
				+ statusDisponibilidade + '\'' + ", dataPublicacao=" + dataPublicacao + '}';
	}
}
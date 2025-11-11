package br.com.wta.frete.marketplace.repository;

import br.com.wta.frete.colaboradores.entity.Lojista;
import br.com.wta.frete.marketplace.entity.Produto;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositório para a entidade Produto (marketplace.produtos).
 * * ATUALIZAÇÃO: Ajustado o método de busca por nome/título.
 */
@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Integer> {

	/**
	 * Busca produtos pelo Lojista vendedor.
	 */
	List<Produto> findByLojista(Lojista lojista);

	/**
	 * Busca produtos pelo SKU (código de estoque).
	 */
	Produto findBySku(String sku);

	/**
	 * Busca produtos por Título, ignorando maiúsculas/minúsculas.
	 * (Campo 'titulo' no banco de dados, mapeado na Entidade).
	 */
	List<Produto> findByTituloContainingIgnoreCase(String titulo);
}
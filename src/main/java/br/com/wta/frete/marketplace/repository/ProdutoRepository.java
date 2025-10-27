package br.com.wta.frete.marketplace.repository;

import br.com.wta.frete.colaboradores.entity.Lojista;
import br.com.wta.frete.marketplace.entity.Produto;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositório para a entidade Produto (marketplace.produtos).
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
	 * Busca produtos por nome, ignorando maiúsculas/minúsculas.
	 */
	List<Produto> findByNomeProdutoContainingIgnoreCase(String nome);
}
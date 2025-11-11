package br.com.wta.frete.marketplace.repository;

import br.com.wta.frete.colaboradores.entity.Lojista;
import br.com.wta.frete.marketplace.entity.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional; // Importação necessária

/**
 * Repositório para a entidade Produto (marketplace.produtos).
 */
@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Integer> {

	/**
	 * Busca produtos pelo Lojista vendedor.
	 */
	List<Produto> findByVendedor(Lojista lojista); // Nome do campo na Entidade é 'vendedor'

	/**
	 * Busca produtos pelo SKU (código de estoque).
	 */
	Produto findBySku(String sku);

	/**
	 * Busca produtos por Título, ignorando maiúsculas/minúsculas.
	 */
	List<Produto> findByTituloContainingIgnoreCase(String titulo);

	/**
	 * NOVO MÉTODO (CORREÇÃO): Verifica se um SKU já existe para um Lojista
	 * específico.
	 * Mapeia para o campo 'vendedor' da Entidade e o campo 'pessoaId' da Entidade
	 * Lojista.
	 */
	Optional<Produto> findByVendedorPessoaIdAndSku(Long vendedorPessoaId, String sku);
}
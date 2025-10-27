package br.com.wta.frete.inventario.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.wta.frete.inventario.entity.EstoqueProduto;

import java.util.List;

/**
 * Repositório para a entidade EstoqueProduto (inventario.estoque_produto).
 * Chave primária: produto_id (Integer).
 */
@Repository
public interface EstoqueProdutoRepository extends JpaRepository<EstoqueProduto, Integer> {

	/**
	 * Busca todos os itens de estoque cuja quantidade é inferior a um valor (alerta
	 * de reposição).
	 */
	List<EstoqueProduto> findByQuantidadeLessThan(Integer quantidade);
}
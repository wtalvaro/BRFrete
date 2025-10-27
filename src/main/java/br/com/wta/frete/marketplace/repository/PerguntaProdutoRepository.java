package br.com.wta.frete.marketplace.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.wta.frete.marketplace.entity.PerguntaProduto;

/**
 * Repositório para a entidade PerguntaProduto. Permite operações de CRUD e
 * consultas personalizadas no banco de dados.
 */
@Repository
public interface PerguntaProdutoRepository extends JpaRepository<PerguntaProduto, Long> {

	// Você pode adicionar métodos de busca específicos aqui, como:
	// List<PerguntaProduto> findByProdutoId(Integer produtoId);
	// List<PerguntaProduto> findByAutorId(Long autorId);
}
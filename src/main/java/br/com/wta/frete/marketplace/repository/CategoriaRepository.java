package br.com.wta.frete.marketplace.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.wta.frete.marketplace.entity.Categoria;

/**
 * Repositório para a entidade Categoria (marketplace.categorias).
 */
@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {

	/**
	 * Busca uma Categoria pelo nome.
	 */
	Categoria findByNomeCategoria(String nomeCategoria);
}
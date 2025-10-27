package br.com.wta.frete.colaboradores.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.wta.frete.colaboradores.entity.Lojista;

/**
 * Repositório para a entidade Lojista (colaboradores.lojistas). Chave primária:
 * pessoa_id (Long).
 */
@Repository
public interface LojistaRepository extends JpaRepository<Lojista, Long> {

	/**
	 * Busca um Lojista pelo nome da sua loja.
	 */
	Lojista findByNomeLoja(String nomeLoja);
}
package br.com.wta.frete.colaboradores.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.wta.frete.colaboradores.entity.Catador;

import java.util.List;

/**
 * Repositório para a entidade Catador (colaboradores.catadores). Chave
 * primária: pessoa_id (Long).
 */
@Repository
public interface CatadorRepository extends JpaRepository<Catador, Long> {

	/**
	 * Busca catadores por associação.
	 */
	List<Catador> findByAssociacaoId(Integer associacaoId);
}
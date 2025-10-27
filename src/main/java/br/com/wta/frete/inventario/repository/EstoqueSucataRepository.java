package br.com.wta.frete.inventario.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.wta.frete.inventario.entity.EstoqueSucata;

import java.util.List;

/**
 * Repositório para a entidade EstoqueSucata (inventario.estoque).
 */
@Repository
public interface EstoqueSucataRepository extends JpaRepository<EstoqueSucata, Long> {

	/**
	 * Busca todo o estoque pertencente a um Sucateiro.
	 */
	List<EstoqueSucata> findBySucateiroPessoaId(Long sucateiroPessoaId);

	/**
	 * Busca estoque por material, ignorando maiúsculas/minúsculas.
	 */
	List<EstoqueSucata> findByNomeMaterialContainingIgnoreCase(String nomeMaterial);
}
package br.com.wta.frete.logistica.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.wta.frete.logistica.entity.CotacaoMaterial;

/**
 * Repositório para a entidade CotacaoMaterial (logistica.cotacoes_materiais).
 */
@Repository
public interface CotacaoMaterialRepository extends JpaRepository<CotacaoMaterial, Integer> {

	/**
	 * Busca a cotação pelo nome do material.
	 */
	CotacaoMaterial findByMaterialNome(String materialNome);
}
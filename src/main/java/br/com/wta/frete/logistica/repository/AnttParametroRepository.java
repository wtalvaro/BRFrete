package br.com.wta.frete.logistica.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.wta.frete.logistica.entity.AnttParametro;

/**
 * Repositório para a entidade AnttParametro (logistica.antt_parametros).
 */
@Repository
public interface AnttParametroRepository extends JpaRepository<AnttParametro, Integer> {

	/**
	 * Busca um parâmetro pela sua chave única.
	 */
	AnttParametro findByChave(String chave);
}
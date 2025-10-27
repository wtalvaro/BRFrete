package br.com.wta.frete.logistica.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.wta.frete.logistica.entity.StatusLeilao;

/**
 * Repositório para a entidade StatusLeilao (logistica.status_leilao).
 */
@Repository
public interface StatusLeilaoRepository extends JpaRepository<StatusLeilao, Integer> {

	/**
	 * Busca um status de leilão pelo seu nome.
	 */
	StatusLeilao findByNomeStatus(String nomeStatus);
}
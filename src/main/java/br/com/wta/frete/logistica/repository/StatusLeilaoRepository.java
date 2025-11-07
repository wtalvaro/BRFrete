package br.com.wta.frete.logistica.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.wta.frete.logistica.entity.StatusLeilao;

/**
 * Interface Repository para a Entidade StatusLeilao.
 */
@Repository
public interface StatusLeilaoRepository extends JpaRepository<StatusLeilao, Integer> {

	/**
	 * Busca um StatusLeilao pelo seu nome.
	 * Deve retornar um Optional para usar .orElseThrow() no Service.
	 */
	Optional<StatusLeilao> findByNomeStatus(String nomeStatus);
}
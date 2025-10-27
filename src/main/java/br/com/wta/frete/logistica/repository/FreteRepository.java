package br.com.wta.frete.logistica.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.wta.frete.logistica.entity.Frete;

import java.util.List;

/**
 * Repositório para a entidade Frete (logistica.fretes). A chave primária é o
 * ordem_servico_id (Long).
 */
@Repository
public interface FreteRepository extends JpaRepository<Frete, Long> {

	/**
	 * Busca fretes com base no status do leilão (ID).
	 */
	List<Frete> findByStatusLeilaoId(Integer statusLeilaoId);

	/**
	 * Busca fretes pela modalidade (ID).
	 */
	List<Frete> findByModalidadeId(Integer modalidadeId);
}
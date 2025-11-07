package br.com.wta.frete.logistica.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.wta.frete.logistica.entity.ModalidadeFrete;

/**
 * Interface Repository para a Entidade ModalidadeFrete.
 */
@Repository
public interface ModalidadeFreteRepository extends JpaRepository<ModalidadeFrete, Integer> {

	/**
	 * Busca uma ModalidadeFrete pelo seu nome.
	 * Deve retornar um Optional para usar .orElseThrow() no Service.
	 */
	Optional<ModalidadeFrete> findByNomeModalidade(String nomeModalidade);
}
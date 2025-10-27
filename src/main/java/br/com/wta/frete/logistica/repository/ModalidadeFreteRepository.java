package br.com.wta.frete.logistica.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.wta.frete.logistica.entity.ModalidadeFrete;

/**
 * Repositório para a entidade ModalidadeFrete (logistica.modalidades_frete).
 */
@Repository
public interface ModalidadeFreteRepository extends JpaRepository<ModalidadeFrete, Integer> {

	/**
	 * Busca uma modalidade de frete pelo seu nome.
	 */
	ModalidadeFrete findByNomeModalidade(String nomeModalidade);
}
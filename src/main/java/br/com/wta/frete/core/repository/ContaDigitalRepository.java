package br.com.wta.frete.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.wta.frete.core.entity.ContaDigital;

/**
 * Repositório para a entidade ContaDigital (core.contas_digitais). Chave
 * primária: pessoa_id (Long).
 */
@Repository
public interface ContaDigitalRepository extends JpaRepository<ContaDigital, Long> {

	/**
	 * Busca uma conta pelo seu identificador único (UUID).
	 */
	ContaDigital findByContaUuid(String contaUuid);
}
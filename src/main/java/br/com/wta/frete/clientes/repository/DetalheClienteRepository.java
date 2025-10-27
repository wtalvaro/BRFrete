package br.com.wta.frete.clientes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.wta.frete.clientes.entity.DetalheCliente;

import java.util.List;

/**
 * Repositório para a entidade DetalheCliente (clientes.detalhes). Chave
 * primária: pessoa_id (Long).
 */
@Repository
public interface DetalheClienteRepository extends JpaRepository<DetalheCliente, Long> {

	/**
	 * Busca clientes por tipo (e.g., PF, PJ).
	 */
	List<DetalheCliente> findByTipoCliente(String tipoCliente);
}
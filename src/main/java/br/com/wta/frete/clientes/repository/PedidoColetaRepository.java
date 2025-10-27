package br.com.wta.frete.clientes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.wta.frete.clientes.entity.PedidoColeta;

import java.util.List;

/**
 * Repositório para a entidade PedidoColeta (clientes.pedidos_coleta).
 */
@Repository
public interface PedidoColetaRepository extends JpaRepository<PedidoColeta, Integer> {

	/**
	 * Busca todos os pedidos de coleta feitos por um Cliente.
	 */
	List<PedidoColeta> findByClientePessoaId(Long clientePessoaId);
}
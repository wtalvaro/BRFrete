package br.com.wta.frete.logistica.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.wta.frete.logistica.entity.ItemFrete;

import java.util.List;

/**
 * Repositório para a entidade ItemFrete (logistica.itens_frete).
 */
@Repository
public interface ItemFreteRepository extends JpaRepository<ItemFrete, Long> {

	/**
	 * Busca todos os itens de um Frete específico (usando a ordem_servico_id).
	 */
	List<ItemFrete> findByFreteOrdemServicoId(Long ordemServicoId);
}
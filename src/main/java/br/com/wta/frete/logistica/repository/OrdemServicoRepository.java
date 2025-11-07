package br.com.wta.frete.logistica.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.wta.frete.core.entity.enums.StatusServico;
import br.com.wta.frete.logistica.entity.OrdemServico;

/**
 * Repositório para a entidade OrdemServico (logistica.ordens_servico). Inclui
 * consultas comuns baseadas em status e relacionamentos.
 */
@Repository
public interface OrdemServicoRepository extends JpaRepository<OrdemServico, Long> {

	/**
	 * Busca todas as Ordens de Serviço por um determinado Status.
	 */
	List<OrdemServico> findByStatus(StatusServico status);

	/**
	 * Busca todas as Ordens de Serviço criadas por um Cliente específico.
	 * CORREÇÃO: O nome do campo é 'clienteSolicitante', então a busca aninhada
	 * deve ser findByClienteSolicitantePessoaId.
	 */
	List<OrdemServico> findByClienteSolicitantePessoaId(Long clientePessoaId); // CORRIGIDO

	/**
	 * Busca todas as Ordens de Serviço atribuídas a um Transportador específico.
	 * CORREÇÃO: O nome do campo é 'transportadorDesignado', então a busca aninhada
	 * deve ser findByTransportadorDesignadoPessoaId.
	 */
	List<OrdemServico> findByTransportadorDesignadoPessoaId(Long transportadorPessoaId); // CORRIGIDO
}
package br.com.wta.frete.logistica.repository;

import br.com.wta.frete.core.entity.enums.StatusServico;
import br.com.wta.frete.logistica.entity.OrdemServico;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

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
	 * Busca todas as Ordens de Serviço criadas por um Cliente específico. O Spring
	 * Data JPA traduz 'Cliente' e 'Id' automaticamente pelo relacionamento.
	 */
	List<OrdemServico> findByClientePessoaId(Long clientePessoaId);

	/**
	 * Busca todas as Ordens de Serviço atribuídas a um Transportador específico.
	 */
	List<OrdemServico> findByTransportadorPessoaId(Long transportadorPessoaId);
}
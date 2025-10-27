package br.com.wta.frete.logistica.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.wta.frete.logistica.entity.Lance;

import java.util.List;

/**
 * Repositório para a entidade Lance (logistica.lances).
 */
@Repository
public interface LanceRepository extends JpaRepository<Lance, Long> {

	/**
	 * Busca todos os lances feitos para um Frete específico (usando o ID da Ordem
	 * de Serviço).
	 */
	List<Lance> findByFreteOrdemServicoId(Long ordemServicoId);

	/**
	 * Busca os lances feitos por um Transportador específico, ordenados pela data.
	 */
	List<Lance> findByTransportadorPessoaIdOrderByDataLanceDesc(Long transportadorPessoaId);

	/**
	 * Busca o lance vencedor de um Frete.
	 */
	Lance findByFreteOrdemServicoIdAndVencedorTrue(Long ordemServicoId);
}
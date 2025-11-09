// Caminho: src/main/java/br/com/wta/frete/logistica/repository/LanceRepository.java
package br.com.wta.frete.logistica.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.wta.frete.logistica.entity.Lance;

import java.util.List;
import java.util.Optional;

/**
 * Repositório para a entidade Lance (logistica.lances).
 */
@Repository
public interface LanceRepository extends JpaRepository<Lance, Long> {

	/**
	 * Busca todos os lances feitos para um Frete específico (usando a PK da
	 * OrdemServico).
	 */
	@Query("SELECT l FROM Lance l WHERE l.frete.ordemServico.id = :ordemServicoId")
	List<Lance> findByFreteOrdemServico_Id(Long ordemServicoId);

	/**
	 * Busca o lance vencedor de um Frete.
	 * CORREÇÃO: A query JPQL estava correta, o erro era de outra query.
	 */
	@Query("SELECT l FROM Lance l WHERE l.frete.ordemServico.id = :ordemServicoId AND l.vencedor = TRUE")
	Lance findByFreteOrdemServico_IdAndVencedorTrue(Long ordemServicoId);

	/**
	 * Encontra o lance de menor valor para um Frete (o melhor lance no Leilão
	 * Reverso).
	 * CORREÇÃO: 'l.valorProposto' alterado para 'l.valorLance'.
	 * O nome do método Spring Data também deve ser ajustado, embora a query
	 * explícita (@Query) seja a prioritária.
	 */
	// O nome do método ideal seria:
	// findTopByFreteOrdemServico_IdOrderByValorLanceAsc
	@Query("SELECT l FROM Lance l WHERE l.frete.ordemServico.id = :ordemServicoId ORDER BY l.valorLance ASC LIMIT 1")
	Optional<Lance> findTopByFreteOrdemServico_IdOrderByValorPropostoAsc(Long ordemServicoId);

	/**
	 * Encontra o lance específico de um Transportador para um Frete.
	 * CORREÇÃO: A query JPQL estava correta.
	 */
	@Query("SELECT l FROM Lance l WHERE l.frete.ordemServico.id = :ordemServicoId AND l.transportador.pessoaId = :transportadorPessoaId")
	Optional<Lance> findByFreteOrdemServico_IdAndTransportadorPessoaId(Long ordemServicoId,
			Long transportadorPessoaId);
}
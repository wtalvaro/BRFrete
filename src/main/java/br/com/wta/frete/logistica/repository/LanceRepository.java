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
	 * Busca todos os lances feitos para um Frete específico (chave primária
	 * freteId).
	 */
	List<Lance> findByFreteFreteId(Long freteId);

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
	 */
	// Este método busca no banco de dados apenas o primeiro registro após ordenar por valor, sendo muito mais eficiente.
	Optional<Lance> findTopByFrete_OrdemServico_IdOrderByValorLanceAsc(Long ordemServicoId);

	/**
	 * Encontra o lance específico de um Transportador para um Frete.
	 * CORREÇÃO: A query JPQL estava correta.
	 */
	Optional<Lance> findByFreteFreteIdAndTransportadorPessoaId(Long freteId,
			Long transportadorPessoaId);
}
package br.com.wta.frete.logistica.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.wta.frete.logistica.entity.Frete;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repositório para a entidade Frete (logistica.fretes). A chave primária é o
 * **frete_id** (Long) (Autoincrementado).
 */
@Repository
public interface FreteRepository extends JpaRepository<Frete, Long> {

	/**
	 * Busca fretes com base no status do leilão (ID).
	 */
	List<Frete> findByStatusLeilaoId(Integer statusLeilaoId);

	/**
	 * Busca fretes pela modalidade (ID).
	 */
	List<Frete> findByModalidadeId(Integer modalidadeId);

	/**
	 * NOVO MÉTODO: Busca todos os fretes que expiraram e ainda estão abertos.
	 * * @param now O momento atual (LocalDateTime.now()).
	 * 
	 * @param nomeStatusAberto O status que indica que o leilão está ativo para
	 *                         lances (ex: "AGUARDANDO_LANCES").
	 * @return Lista de Fretes expirados para processamento.
	 */
	List<Frete> findByDataExpiracaoNegociacaoBeforeAndStatusLeilaoNomeStatus(LocalDateTime now,
			String nomeStatusAberto);

	// Método para buscar status, assumindo que StatusLeilao é uma entidade separada
	// (A injeção do StatusLeilaoRepository será feita no Service)
}
package br.com.wta.frete.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.wta.frete.core.entity.Conversa;

/**
 * Repositório para a entidade Conversa. Estende JpaRepository para herdar
 * métodos de persistência e consulta. * Parâmetros: - Conversa: A entidade que
 * este repositório gerencia. - Long: O tipo da chave primária (ID) da entidade
 * Conversa.
 */
@Repository
public interface ConversaRepository extends JpaRepository<Conversa, Long> {

	// Você pode adicionar métodos de consulta específicos aqui, se necessário.
	// Exemplo: List<Conversa> findByOrdemServicoId(Long ordemServicoId);
}
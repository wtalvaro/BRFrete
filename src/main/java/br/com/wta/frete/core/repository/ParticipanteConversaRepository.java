package br.com.wta.frete.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.wta.frete.core.entity.ParticipanteConversa;
import br.com.wta.frete.core.entity.ParticipanteConversaId;

/**
 * Repositório para a entidade ParticipanteConversa. Estende JpaRepository para
 * herdar métodos de persistência e consulta. * Parâmetros: -
 * ParticipanteConversa: A entidade que este repositório gerencia. -
 * ParticipanteConversaId: O tipo da chave primária composta (ID) da entidade.
 */
@Repository
public interface ParticipanteConversaRepository extends JpaRepository<ParticipanteConversa, ParticipanteConversaId> {

	// Método de exemplo para buscar participantes por ID da conversa.
	// List<ParticipanteConversa> findByIdConversaId(Long conversaId);

	// Método de exemplo para verificar se uma pessoa está em uma conversa.
	// Optional<ParticipanteConversa> findByIdPessoaIdAndIdConversaId(Long pessoaId,
	// Long conversaId);
}
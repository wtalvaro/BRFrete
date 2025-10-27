package br.com.wta.frete.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.wta.frete.core.entity.Mensagem;

/**
 * Repositório para a entidade Mensagem. Estende JpaRepository para herdar
 * métodos de persistência e consulta. * Parâmetros: - Mensagem: A entidade que
 * este repositório gerencia. - Long: O tipo da chave primária (ID) da entidade
 * Mensagem.
 */
@Repository
public interface MensagemRepository extends JpaRepository<Mensagem, Long> {

	// Método de exemplo para buscar mensagens por conversa, ordenadas por data.
	// List<Mensagem> findByConversaIdOrderByDataMensagemAsc(Long conversaId);
}
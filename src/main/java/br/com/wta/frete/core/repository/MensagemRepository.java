package br.com.wta.frete.core.repository;

import java.util.List;

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

	/**
	 * Busca todas as mensagens pertencentes a uma conversa específica,
	 * ordenando-as pela data de envio (dataEnvio) em ordem ascendente.
	 * (CORRIGIDO: O nome do campo é dataEnvio, e não dataMensagem)
	 */
	List<Mensagem> findByConversaIdOrderByDataEnvioAsc(Long conversaId);
}
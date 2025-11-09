// Caminho: src/main/java/br/com/wta/frete/logistica/repository/ItemFreteRepository.java
package br.com.wta.frete.logistica.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query; // NOVO IMPORT
import org.springframework.stereotype.Repository;

import br.com.wta.frete.logistica.entity.ItemFrete;

import java.util.List;

/**
 * Repositório para a entidade ItemFrete (logistica.itens_frete).
 */
@Repository
public interface ItemFreteRepository extends JpaRepository<ItemFrete, Long> {

	/**
	 * Busca todos os itens de um Frete específico, filtrando pelo ID da Ordem de
	 * Serviço.
	 * CORREÇÃO: Usando @Query para navegação explícita i.frete.ordemServico.id.
	 */
	@Query("SELECT i FROM ItemFrete i WHERE i.frete.ordemServico.id = :ordemServicoId")
	List<ItemFrete> findByFreteOrdemServico_Id(Long ordemServicoId);
}
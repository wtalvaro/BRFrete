package br.com.wta.frete.inventario.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.wta.frete.inventario.entity.EstoqueSucata;
import br.com.wta.frete.inventario.entity.enums.TipoMaterialEnum;
import java.util.Optional;
import java.util.List;

/**
 * Repositório para a entidade EstoqueSucata (inventario.estoque).
 * Chave primária: estoque_id (Long).
 */
@Repository
public interface EstoqueSucataRepository extends JpaRepository<EstoqueSucata, Long> {

	/**
	 * Busca um item de estoque pelo ID do Sucateiro e o Tipo de Material.
	 * Útil para garantir a unicidade e para a lógica de 'criar ou atualizar'.
	 */
	Optional<EstoqueSucata> findBySucateiroPessoaIdAndTipoMaterial(Long sucateiroPessoaId,
			TipoMaterialEnum tipoMaterial);

	/**
	 * Busca todos os itens de estoque de um Sucateiro específico.
	 */
	List<EstoqueSucata> findBySucateiroPessoaId(Long sucateiroPessoaId);
}
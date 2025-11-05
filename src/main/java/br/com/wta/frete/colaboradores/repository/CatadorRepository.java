package br.com.wta.frete.colaboradores.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.wta.frete.colaboradores.entity.Catador;

import java.util.List;
import java.util.Optional; // Importação necessária

/**
 * Repositório para a entidade Catador (colaboradores.catadores). Chave
 * primária: pessoa_id (Long).
 */
@Repository
public interface CatadorRepository extends JpaRepository<Catador, Long> {

	/**
	 * Busca catadores por associação.
	 */
	List<Catador> findByAssociacaoId(Integer associacaoId);

	/**
	 * NOVO: Busca um Catador pelo ID da Pessoa (pessoa_id).
	 */
	Optional<Catador> findByPessoaId(Long pessoaId);
}
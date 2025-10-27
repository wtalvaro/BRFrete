package br.com.wta.frete.social.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.wta.frete.social.entity.Seguidor;
import br.com.wta.frete.social.entity.SeguidorId;

import java.util.List;

/**
 * Repositório para a entidade Seguidor (social.seguidores). Usa a chave
 * composta SeguidorId.
 */
@Repository
public interface SeguidorRepository extends JpaRepository<Seguidor, SeguidorId> {

	/**
	 * Busca todas as Pessoas que uma Pessoa está seguindo (o 'Seguidor' é o autor).
	 */
	List<Seguidor> findByIdSeguidorId(Long seguidorPessoaId);

	/**
	 * Busca todas as Pessoas que estão seguindo uma Pessoa (o 'Seguido' é o alvo).
	 */
	List<Seguidor> findByIdSeguidoId(Long seguidoPessoaId);
}
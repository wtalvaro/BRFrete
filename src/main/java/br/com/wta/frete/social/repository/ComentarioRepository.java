package br.com.wta.frete.social.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.wta.frete.social.entity.Comentario;

import java.util.List;

/**
 * Repositório para a entidade Comentario (social.comentarios).
 */
@Repository
public interface ComentarioRepository extends JpaRepository<Comentario, Long> {

	/**
	 * Busca todos os comentários feitos para um Produto específico.
	 */
	List<Comentario> findByProdutoId(Integer produtoId);

	/**
	 * Busca apenas os comentários principais (sem pai) de um Produto.
	 */
	List<Comentario> findByProdutoIdAndComentarioPaiIsNull(Integer produtoId);

	/**
	 * Busca todas as respostas para um comentário pai.
	 */
	List<Comentario> findByComentarioPaiId(Long comentarioPaiId);
}
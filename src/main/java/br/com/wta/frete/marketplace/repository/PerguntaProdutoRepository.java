package br.com.wta.frete.marketplace.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.wta.frete.marketplace.entity.PerguntaProduto;
import java.util.List;

/**
 * Repositório para a entidade PerguntaProduto.
 * * ATUALIZAÇÃO: Adicionados métodos para buscar threads (perguntas principais
 * e respostas).
 */
@Repository
public interface PerguntaProdutoRepository extends JpaRepository<PerguntaProduto, Long> {

	/**
	 * Busca todas as Perguntas (tópicos principais) de um Produto,
	 * ou seja, onde perguntaPai é nulo (não é uma resposta).
	 * Ordenadas pela mais recente.
	 */
	List<PerguntaProduto> findByProdutoIdAndPerguntaPaiIsNullOrderByDataPublicacaoDesc(Integer produtoId);

	/**
	 * Busca todas as Respostas de uma Pergunta Pai específica.
	 * Ordenadas pela data mais antiga (cronológica).
	 */
	List<PerguntaProduto> findByPerguntaPaiIdOrderByDataPublicacaoAsc(Long perguntaPaiId);
}
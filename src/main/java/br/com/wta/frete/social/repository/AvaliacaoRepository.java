package br.com.wta.frete.social.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.wta.frete.social.entity.Avaliacao;

import java.util.List;

/**
 * Repositório para a entidade Avaliacao (social.avaliacoes).
 */
@Repository
public interface AvaliacaoRepository extends JpaRepository<Avaliacao, Long> {

    /**
     * Busca avaliações recebidas por uma Pessoa específica.
     */
    List<Avaliacao> findByAvaliadoId(Long avaliadoId);

    /**
     * Busca avaliações feitas para uma Ordem de Serviço específica.
     */
    List<Avaliacao> findByOrdemServicoId(Long ordemServicoId);

    /**
     * Busca avaliações feitas para um Produto específico.
     */
    List<Avaliacao> findByProdutoId(Integer produtoId);
}
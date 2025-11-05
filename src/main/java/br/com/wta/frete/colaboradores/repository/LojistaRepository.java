package br.com.wta.frete.colaboradores.repository;

import java.util.Optional; // Importação necessária

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.wta.frete.colaboradores.entity.Lojista;

/**
 * Repositório para a entidade Lojista (colaboradores.lojistas). Chave primária:
 * pessoa_id (Long).
 */
@Repository
public interface LojistaRepository extends JpaRepository<Lojista, Long> {

	/**
	 * Busca um Lojista pelo nome da sua loja.
	 */
	Lojista findByNomeLoja(String nomeLoja);

	/**
	 * NOVO: Busca um Lojista pelo ID da Pessoa (pessoa_id).
	 */
	Optional<Lojista> findByPessoaId(Long pessoaId);
}
package br.com.wta.frete.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.wta.frete.core.entity.PessoaPerfil;
import br.com.wta.frete.core.entity.PessoaPerfilId;

import java.util.List;

/**
 * Repositório para a entidade de associação PessoaPerfil (core.pessoa_perfil).
 * Usa a chave composta PessoaPerfilId.
 */
@Repository
public interface PessoaPerfilRepository extends JpaRepository<PessoaPerfil, PessoaPerfilId> {

	/**
	 * Busca todos os perfis associados a uma Pessoa.
	 */
	List<PessoaPerfil> findByPessoaId(Long pessoaId);

	/**
	 * Busca todas as Pessoas que possuem um Perfil específico.
	 */
	List<PessoaPerfil> findByPerfilId(Integer perfilId);
}
package br.com.wta.frete.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.wta.frete.core.entity.Perfil;

/**
 * Repositório para a entidade Perfil (core.perfis).
 */
@Repository
public interface PerfilRepository extends JpaRepository<Perfil, Integer> {

	/**
	 * Busca um Perfil pelo nome.
	 */
	Perfil findByNomePerfil(String nomePerfil);
}
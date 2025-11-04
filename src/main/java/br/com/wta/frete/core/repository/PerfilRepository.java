package br.com.wta.frete.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.wta.frete.core.entity.Perfil;
import java.util.Optional; // NOVO IMPORT

/**
 * Repositório para a entidade Perfil (core.perfis).
 */
@Repository
public interface PerfilRepository extends JpaRepository<Perfil, Integer> {

	/**
	 * Busca um Perfil pelo nome.
	 * CORREÇÃO: Mudar retorno para Optional para uso seguro com orElseThrow no
	 * Service.
	 */
	Optional<Perfil> findByNomePerfil(String nomePerfil);
}
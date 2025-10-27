package br.com.wta.frete.colaboradores.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.wta.frete.colaboradores.entity.Sucateiro;

/**
 * Repositório para a entidade Sucateiro (colaboradores.sucateiros). A chave
 * primária é o pessoa_id (Long).
 */
@Repository
public interface SucateiroRepository extends JpaRepository<Sucateiro, Long> {

	/**
	 * Busca um Sucateiro pelo nome da sua Razão Social.
	 */
	Sucateiro findByRazaoSocial(String razaoSocial);

	/**
	 * Busca Sucateiros pela licença ambiental.
	 */
	Sucateiro findByLicencaAmbiental(String licenca);
}
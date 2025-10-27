package br.com.wta.frete.colaboradores.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.wta.frete.colaboradores.entity.Transportador;

/**
 * Repositório para a entidade Transportador (colaboradores.transportadores). A
 * chave primária é o pessoa_id (Long).
 */
@Repository
public interface TransportadorRepository extends JpaRepository<Transportador, Long> {

	/**
	 * Busca um Transportador pela sua licença de transporte.
	 */
	Transportador findByLicencaTransporte(String licencaTransporte);
}
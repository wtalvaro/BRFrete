package br.com.wta.frete.colaboradores.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.wta.frete.colaboradores.entity.Transportador;
import java.util.Optional; // NOVO IMPORT

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

	/**
	 * Busca um Transportador pelo ID da Pessoa (pessoa_id).
	 * Retorna Optional para permitir validação no Service.
	 */
	Optional<Transportador> findByPessoaId(Long pessoaId); // NOVO MÉTODO PARA COMPATIBILIDADE DA LÓGICA
}
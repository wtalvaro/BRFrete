package br.com.wta.frete.colaboradores.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.wta.frete.colaboradores.entity.Veiculo;
import java.util.Optional;

/**
 * Repositório para a entidade Veiculo (colaboradores.veiculos).
 * CORREÇÃO: Métodos de busca ajustados para usar 'Placa' e 'Renavam'.
 */
@Repository
public interface VeiculoRepository extends JpaRepository<Veiculo, Integer> {

	/**
	 * Documentação: Busca um veículo pela sua placa. Necessário para a validação
	 * de unicidade no cadastro.
	 */
	Optional<Veiculo> findByPlaca(String placa); // CORRIGIDO: findByMatricula -> findByPlaca

	/**
	 * Documentação: Busca um veículo pelo seu Renavam. Necessário para a validação
	 * de unicidade no cadastro.
	 */
	Optional<Veiculo> findByRenavam(String renavam);

	// NOVO MÉTODO: Busca por placa OU renavam, ignorando o case.
    Optional<Veiculo> findByPlacaIgnoreCaseOrRenavamIgnoreCase(String placa, String renavam);
}
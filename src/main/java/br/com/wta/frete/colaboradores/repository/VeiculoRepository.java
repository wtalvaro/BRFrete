package br.com.wta.frete.colaboradores.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.wta.frete.colaboradores.entity.Veiculo;

import java.util.List;

/**
 * Repositório para a entidade Veiculo (colaboradores.veiculos).
 */
@Repository
public interface VeiculoRepository extends JpaRepository<Veiculo, Integer> {

	/**
	 * Busca um veículo pela sua matrícula (placa).
	 */
	Veiculo findByMatricula(String matricula);

	/**
	 * Busca todos os veículos de um Transportador específico.
	 */
	List<Veiculo> findByTransportadorPessoaId(Long transportadorPessoaId);
}
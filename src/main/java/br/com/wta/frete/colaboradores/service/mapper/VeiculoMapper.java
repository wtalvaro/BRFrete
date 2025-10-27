package br.com.wta.frete.colaboradores.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import br.com.wta.frete.colaboradores.controller.dto.VeiculoRequest;
import br.com.wta.frete.colaboradores.controller.dto.VeiculoResponse;
import br.com.wta.frete.colaboradores.entity.Veiculo;
import br.com.wta.frete.colaboradores.entity.enums.StatusVeiculo;
import br.com.wta.frete.colaboradores.entity.enums.TipoVeiculo;

/**
 * Interface Mapper para converter entre a Entidade Veículo e seus DTOs. Lida
 * com mapeamento de IDs de relacionamento e conversão de Enums (String <->
 * Enum).
 */
@Mapper(componentModel = "spring")
public interface VeiculoMapper {

	/**
	 * Mapeamento de Request (DTO) para Entidade. Mapeia o ID do Transportador do
	 * DTO para a Entidade Transportador (objeto). Converte Strings do DTO para os
	 * Enums da Entidade. * @param dto O DTO de requisição.
	 * 
	 * @return A nova entidade Veiculo.
	 */
	// Ignora 'id' pois é gerado pelo banco de dados.
	@Mapping(target = "id", ignore = true)
	// Ignora 'transportador' pois o mapeamento 'transportador.pessoaId' o cobre.
	@Mapping(target = "transportador", ignore = true)
	@Mapping(source = "transportadorPessoaId", target = "transportador.pessoaId")
	@Mapping(source = "tipoVeiculo", target = "tipoVeiculo") // String -> Enum
	@Mapping(source = "statusVeiculo", target = "statusVeiculo") // String -> Enum
	Veiculo toEntity(VeiculoRequest dto);

	/**
	 * Mapeamento de Entidade para Response (DTO). Mapeia a PK 'id' da Entidade para
	 * 'veiculoId' do Response. Mapeia o ID do Transportador do objeto Entidade para
	 * o Long do Response. Converte Enums da Entidade para Strings do DTO. * @param
	 * entity A entidade Veiculo.
	 * 
	 * @return O DTO de resposta.
	 */
	@Mapping(source = "id", target = "veiculoId")
	@Mapping(source = "transportador.pessoaId", target = "transportadorPessoaId")
	@Mapping(source = "tipoVeiculo", target = "tipoVeiculo") // Enum -> String
	@Mapping(source = "statusVeiculo", target = "statusVeiculo") // Enum -> String
	VeiculoResponse toResponse(Veiculo entity);

	/**
	 * Atualiza uma Entidade Veiculo existente com os dados de um Request.
	 * 
	 * @MappingTarget garante que a instância existente seja atualizada. * @param
	 *                dto O DTO de requisição com os novos dados.
	 * @param entity A entidade Veiculo que será atualizada.
	 */
	// Ignora 'id' e 'transportador' em atualizações, pois não devem ser alterados.
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "transportador", ignore = true)
	// Também ignoramos o transportadorPessoaId porque não queremos trocar a FK do
	// Transportador em uma atualização.
	@Mapping(target = "transportador.pessoaId", ignore = true)
	@Mapping(source = "tipoVeiculo", target = "tipoVeiculo")
	@Mapping(source = "statusVeiculo", target = "statusVeiculo")
	void updateEntity(VeiculoRequest dto, @MappingTarget Veiculo entity);

	/*
	 * MÉTODOS AUXILIARES: Conversão String <-> Enum. Eles são automaticamente
	 * usados pelo MapStruct.
	 */

	default TipoVeiculo mapTipoVeiculo(String tipoVeiculo) {
		if (tipoVeiculo == null)
			return null;
		try {
			return TipoVeiculo.valueOf(tipoVeiculo.toUpperCase());
		} catch (IllegalArgumentException e) {
			// Em um ambiente real, você pode lançar uma exceção de validação
			return null;
		}
	}

	default String mapTipoVeiculo(TipoVeiculo tipoVeiculo) {
		return tipoVeiculo != null ? tipoVeiculo.name() : null;
	}

	default StatusVeiculo mapStatusVeiculo(String statusVeiculo) {
		if (statusVeiculo == null)
			return null;
		try {
			return StatusVeiculo.valueOf(statusVeiculo.toUpperCase());
		} catch (IllegalArgumentException e) {
			// Em um ambiente real, você pode lançar uma exceção de validação
			return null;
		}
	}

	default String mapStatusVeiculo(StatusVeiculo statusVeiculo) {
		return statusVeiculo != null ? statusVeiculo.name() : null;
	}
}
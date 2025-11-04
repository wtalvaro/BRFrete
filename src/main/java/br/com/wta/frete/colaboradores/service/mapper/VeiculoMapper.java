package br.com.wta.frete.colaboradores.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import br.com.wta.frete.colaboradores.controller.dto.VeiculoRequest;
import br.com.wta.frete.colaboradores.controller.dto.VeiculoResponse;
import br.com.wta.frete.colaboradores.entity.Veiculo;

/**
 * Mapper para a entidade Veiculo.
 * Responsável por converter VeiculoRequest para Veiculo e Veiculo para
 * VeiculoResponse.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface VeiculoMapper {

	/**
	 * Mapeia de VeiculoRequest (DTO de entrada) para Veiculo (Entidade).
	 *
	 * @param request O DTO de requisição com os dados do veículo.
	 * @return A entidade Veiculo.
	 *
	 * @Mapping(target = "transportador", ignore = true): Ignora o campo
	 *                 'transportador' na Entidade,
	 *                 pois ele será definido pelo Service (usando o
	 *                 transportadorPessoaId do DTO para buscar a Entidade
	 *                 Transportador).
	 *                 Todos os outros campos (placa, renavam, capacidadeKg, etc.)
	 *                 são mapeados automaticamente
	 *                 devido aos nomes idênticos no DTO e na Entidade.
	 */
	@Mapping(target = "transportador", ignore = true)
	Veiculo toEntity(VeiculoRequest request);

	/**
	 * Mapeia de Veiculo (Entidade) para VeiculoResponse (DTO de saída).
	 *
	 * @param entity A entidade Veiculo.
	 * @return O DTO de resposta.
	 *
	 * @Mapping(source = "transportador.pessoaId", target =
	 *                 "transportadorPessoaId"): Mapeia
	 *                 o ID da Pessoa (que é a chave do Transportador) para o campo
	 *                 do DTO de resposta.
	 */
	@Mapping(source = "transportador.pessoaId", target = "transportadorPessoaId")
	VeiculoResponse toResponse(Veiculo entity);
}
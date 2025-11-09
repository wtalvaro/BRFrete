package br.com.wta.frete.logistica.service.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import br.com.wta.frete.logistica.controller.dto.ModalidadeFreteResponse;
import br.com.wta.frete.logistica.entity.ModalidadeFrete;

/**
 * Mapper para a entidade ModalidadeFrete, responsável por converter entre
 * ModalidadeFrete (Entidade) e ModalidadeFreteResponse (DTO). É utilizado para
 * lidar com entidades de lookup (consulta).
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ModalidadeFreteMapper {

	// Linha removida: ModalidadeFreteMapper INSTANCE =
	// Mappers.getMapper(ModalidadeFreteMapper.class);

	// --- Mapeamento de Entidade para Response ---

	/**
	 * Converte a entidade ModalidadeFrete para o DTO de Resposta
	 * ModalidadeFreteResponse. Realiza o mapeamento de: - id (ModalidadeFrete) para
	 * modalidadeFreteId (ModalidadeFreteResponse). - nomeModalidade
	 * (ModalidadeFrete) para nomeModalidade (ModalidadeFreteResponse).
	 *
	 * @param entity A entidade ModalidadeFrete.
	 * @return O DTO de resposta ModalidadeFreteResponse.
	 */
	@Mapping(target = "modalidadeFreteId", source = "id")
	ModalidadeFreteResponse toResponse(ModalidadeFrete entity);

	/**
	 * Converte uma lista de entidades ModalidadeFrete para uma lista de DTOs
	 * ModalidadeFreteResponse.
	 *
	 * @param entities A lista de entidades.
	 * @return A lista de DTOs de resposta.
	 */
	List<ModalidadeFreteResponse> toResponse(List<ModalidadeFrete> entities);
}
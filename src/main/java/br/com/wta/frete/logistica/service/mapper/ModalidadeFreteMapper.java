package br.com.wta.frete.logistica.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import br.com.wta.frete.logistica.controller.dto.ModalidadeFreteResponse;
import br.com.wta.frete.logistica.entity.ModalidadeFrete;

import java.util.List;

/**
 * Mapper para a entidade ModalidadeFrete, responsável por converter entre
 * ModalidadeFrete (Entidade) e ModalidadeFreteResponse (DTO). É utilizado para
 * lidar com entidades de lookup (consulta).
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ModalidadeFreteMapper {

	// Opcional: Para uso fora do contexto Spring (embora componentModel="spring"
	// seja recomendado)
	ModalidadeFreteMapper INSTANCE = Mappers.getMapper(ModalidadeFreteMapper.class);

	// --- Mapeamento de Entidade para Response ---

	/**
	 * Converte a entidade ModalidadeFrete para o DTO de Resposta
	 * ModalidadeFreteResponse. Realiza o mapeamento de: - id (ModalidadeFrete) para
	 * modalidadeFreteId (ModalidadeFreteResponse). - nomeModalidade
	 * (ModalidadeFrete) para nomeModalidade (ModalidadeFreteResponse). * @param
	 * entity A entidade ModalidadeFrete.
	 * 
	 * @return O DTO de resposta ModalidadeFreteResponse.
	 */
	@Mapping(target = "modalidadeFreteId", source = "id")
	ModalidadeFreteResponse toResponse(ModalidadeFrete entity);

	/**
	 * Converte uma lista de entidades ModalidadeFrete para uma lista de DTOs
	 * ModalidadeFreteResponse. * @param entities A lista de entidades
	 * ModalidadeFrete.
	 * 
	 * @return A lista de DTOs ModalidadeFreteResponse.
	 */
	List<ModalidadeFreteResponse> toResponseList(List<ModalidadeFrete> entities);
}
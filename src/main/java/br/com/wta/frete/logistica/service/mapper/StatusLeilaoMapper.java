package br.com.wta.frete.logistica.service.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import br.com.wta.frete.logistica.controller.dto.StatusLeilaoResponse;
import br.com.wta.frete.logistica.entity.StatusLeilao;

/**
 * Mapper para a entidade StatusLeilao, responsável por converter entre
 * StatusLeilao (Entidade) e StatusLeilaoResponse (DTO). É utilizado para
 * lidar com entidades de lookup (consulta).
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface StatusLeilaoMapper {

	/**
	 * Converte a entidade StatusLeilao para o DTO de Resposta
	 * StatusLeilaoResponse.
	 *
	 * @param entity A entidade StatusLeilao.
	 * @return O DTO de resposta StatusLeilaoResponse.
	 */
	@Mapping(target = "statusLeilaoId", source = "id")
	StatusLeilaoResponse toResponse(StatusLeilao entity);

	/**
	 * Converte uma lista de entidades StatusLeilao para uma lista de DTOs
	 * StatusLeilaoResponse.
	 *
	 * @param entities A lista de entidades.
	 * @return A lista de DTOs de resposta.
	 */
	List<StatusLeilaoResponse> toResponse(List<StatusLeilao> entities);
}
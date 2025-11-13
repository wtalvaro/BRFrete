package br.com.wta.frete.core.service.mapper;

import java.util.List; // Importar List
import java.util.stream.Collectors; // Importar Collectors

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import br.com.wta.frete.core.controller.dto.ContaDigitalResponse;
import br.com.wta.frete.core.entity.ContaDigital;
import br.com.wta.frete.core.entity.enums.StatusKYC;

/**
 * Mapper para conversão entre a Entidade ContaDigital e o DTO
 * ContaDigitalResponse. Configurado como componente Spring.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ContaDigitalMapper {

	/**
	 * Converte a entidade ContaDigital para o DTO de resposta ContaDigitalResponse.
	 * * @param entity A entidade ContaDigital a ser convertida.
	 * 
	 * @return O DTO ContaDigitalResponse.
	 */
	ContaDigitalResponse toResponse(ContaDigital entity);

	// CORREÇÃO: Implementação do método default para listagem.
	/**
	 * Converte uma lista de Entidades ContaDigital para uma lista de DTOs de
	 * Resposta.
	 *
	 * @param entities A lista de entidades ContaDigital.
	 * @return A lista de DTOs ContaDigitalResponse.
	 */
	default List<ContaDigitalResponse> toResponseList(List<ContaDigital> entities) {
		if (entities == null) {
			return List.of();
		}
		// Mapeia cada item da lista usando o toResponse()
		return entities.stream()
				.map(this::toResponse)
				.collect(Collectors.toList());
	}

	default String mapStatusKyc(StatusKYC status) {
		return status != null ? status.name() : null;
	}
}
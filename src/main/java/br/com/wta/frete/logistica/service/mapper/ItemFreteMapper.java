package br.com.wta.frete.logistica.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

import br.com.wta.frete.logistica.controller.dto.ItemFreteRequest;
import br.com.wta.frete.logistica.controller.dto.ItemFreteResponse;
import br.com.wta.frete.logistica.entity.ItemFrete;

/**
 * Mapeador MapStruct para conversão entre DTOs e a entidade ItemFrete.
 *
 * CORREÇÃO: Alinhamento da Foreign Key (FK) para frete.freteId.
 */
@Mapper(componentModel = "spring")
public interface ItemFreteMapper {

	/**
	 * Converte um DTO de requisição para a entidade ItemFrete.
	 */
	@Mappings({
			@Mapping(target = "id", ignore = true),
			@Mapping(target = "frete", ignore = true)
	})
	ItemFrete toEntity(ItemFreteRequest request);

	/**
	 * Converte a entidade ItemFrete para o DTO de resposta.
	 * O campo freteId é mapeado usando a PK real da entidade Frete.
	 */
	@Mappings({
			@Mapping(source = "id", target = "itemFreteId"), // Mapeia a PK da entidade ItemFrete
			// CORREÇÃO FINAL: Usando frete.freteId, conforme a entidade Frete.java
			@Mapping(source = "frete.freteId", target = "freteId")
	})
	ItemFreteResponse toResponse(ItemFrete entity);

	/**
	 * Atualiza uma entidade ItemFrete existente com base nos dados do DTO.
	 */
	@Mappings({
			@Mapping(target = "id", ignore = true),
			@Mapping(target = "frete", ignore = true)
	})
	void updateEntity(ItemFreteRequest request, @MappingTarget ItemFrete entity);
}
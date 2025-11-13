package br.com.wta.frete.core.service.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import br.com.wta.frete.core.controller.dto.PerfilRequest; // NOVO IMPORT
import br.com.wta.frete.core.controller.dto.PerfilResponse;
import br.com.wta.frete.core.entity.Perfil;

/**
 * Mapper para a entidade Perfil (core.perfis). Responsável pela conversão entre
 * a entidade Perfil e seus respectivos DTOs.
 */
@Mapper(componentModel = "spring")
public interface PerfilMapper {

	/**
	 * Mapeia um objeto Perfil (Entidade) para um PerfilResponse (DTO).
	 */
	@Mapping(source = "id", target = "perfilId")
	PerfilResponse toResponse(Perfil entity);

	/**
	 * Mapeia um PerfilRequest (DTO de Requisição) para a entidade Perfil.
	 * O campo 'id' (PK) é ignorado, pois é gerado pelo banco de dados.
	 * 
	 * @param request O DTO de requisição.
	 * @return A entidade Perfil para persistência.
	 */
	@Mapping(target = "id", ignore = true)
	Perfil toEntity(PerfilRequest request); // <<< NOVO MÉTODO

	/**
	 * Mapeia uma lista de entidades Perfil para uma lista de DTOs PerfilResponse.
	 */
	List<PerfilResponse> toResponseList(List<Perfil> entities);
}
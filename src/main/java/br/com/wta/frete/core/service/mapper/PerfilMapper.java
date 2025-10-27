package br.com.wta.frete.core.service.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import br.com.wta.frete.core.controller.dto.PerfilResponse;
import br.com.wta.frete.core.entity.Perfil;

/**
 * Mapper para a entidade Perfil (core.perfis). Responsável pela conversão entre
 * a entidade Perfil e seus respectivos DTOs.
 */
@Mapper(componentModel = "spring")
public interface PerfilMapper {

	/**
	 * Mapeia um objeto Perfil (Entidade) para um PerfilResponse (DTO). * @param
	 * entity A entidade Perfil a ser mapeada.
	 * 
	 * @return O DTO PerfilResponse preenchido. * Nota sobre o mapeamento:
	 *         - @Mapping(source = "id", target = "perfilId"): Resolve a diferença
	 *         de nomenclatura entre a Entity (id) e o DTO (perfilId). - Os campos
	 *         com nomes idênticos (nomePerfil e descricao) são mapeados
	 *         automaticamente pelo MapStruct.
	 */
	@Mapping(source = "id", target = "perfilId")
	PerfilResponse toResponse(Perfil entity);

	/**
	 * Mapeia uma lista de entidades Perfil para uma lista de DTOs PerfilResponse.
	 * * @param entities A lista de entidades Perfil.
	 * 
	 * @return A lista de DTOs PerfilResponse.
	 */
	List<PerfilResponse> toResponseList(List<Perfil> entities);
}
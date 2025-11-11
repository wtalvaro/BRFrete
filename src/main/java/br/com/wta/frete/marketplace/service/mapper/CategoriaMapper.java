package br.com.wta.frete.marketplace.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import br.com.wta.frete.marketplace.controller.dto.CategoriaRequest;
import br.com.wta.frete.marketplace.controller.dto.CategoriaResponse;
import br.com.wta.frete.marketplace.entity.Categoria;

/**
 * Mapper para a entidade Categoria, responsável por converter entre Entidade,
 * Request DTO e Response DTO.
 */
@Mapper(componentModel = "spring")
public interface CategoriaMapper {

	// Instância estática para uso fora do contexto Spring (opcional, mas útil)
	CategoriaMapper INSTANCE = Mappers.getMapper(CategoriaMapper.class);

	/**
	 * Converte uma Categoria (Entity) para CategoriaResponse (DTO).
	 * Mapeia explicitamente 'id' da Entity para 'categoriaId' do DTO.
	 *
	 * @param entity A entidade Categoria.
	 * @return O DTO de resposta CategoriaResponse.
	 */
	@Mapping(source = "id", target = "categoriaId")
	CategoriaResponse toResponse(Categoria entity);

	/**
	 * Converte um CategoriaRequest (DTO) para Categoria (Entity).
	 * O campo 'id' da Entity é ignorado para permitir a criação de um novo
	 * registro.
	 *
	 * @param dto O DTO de requisição CategoriaRequest.
	 * @return A entidade Categoria.
	 */
	@Mapping(target = "id", ignore = true) // Ignora o 'id' para criação
	Categoria toEntity(CategoriaRequest dto);

	/**
	 * ATUALIZAÇÃO: Aplica as mudanças de CategoriaRequest em uma entidade Categoria
	 * existente.
	 * O campo 'id' (chave primária) é explicitamente ignorado para remover o aviso
	 * (warning).
	 *
	 * @param dto    O DTO de requisição CategoriaRequest.
	 * @param entity O objeto Categoria existente a ser atualizado (Target).
	 */
	@Mapping(target = "id", ignore = true) // <-- CORREÇÃO: Ignora o ID para remover o aviso
	void updateEntity(CategoriaRequest dto, @MappingTarget Categoria entity);
}
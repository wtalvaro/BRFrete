package br.com.wta.frete.marketplace.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import br.com.wta.frete.marketplace.controller.dto.CategoriaRequest;
import br.com.wta.frete.marketplace.controller.dto.CategoriaResponse;
import br.com.wta.frete.marketplace.entity.Categoria;

/**
 * Mapper para a entidade Categoria, responsável por converter entre Entidade,
 * Request DTO e Response DTO.
 */
@Mapper(componentModel = "spring") // Define o MapStruct para usar o modelo de componente Spring
public interface CategoriaMapper {

	// Instância estática para uso fora do contexto Spring (opcional, mas útil)
	CategoriaMapper INSTANCE = Mappers.getMapper(CategoriaMapper.class);

	/**
	 * Converte uma Categoria (Entity) para CategoriaResponse (DTO). Mapeia
	 * explicitamente 'id' da Entity para 'categoriaId' do DTO. * @param entity A
	 * entidade Categoria.
	 * 
	 * @return O DTO de resposta CategoriaResponse.
	 */
	@Mapping(source = "id", target = "categoriaId")
	CategoriaResponse toResponse(Categoria entity);

	/**
	 * Converte um CategoriaRequest (DTO) para Categoria (Entity). O campo 'id' da
	 * Entity é ignorado, permitindo a criação de um novo registro.
	 *
	 * CORREÇÃO: Ignora a propriedade 'id' (chave primária) para resolver o warning.
	 * * @param dto O DTO de requisição CategoriaRequest.
	 * 
	 * @return A entidade Categoria.
	 */
	@Mapping(target = "id", ignore = true) // <-- Linha adicionada para ignorar o 'id'
	Categoria toEntity(CategoriaRequest dto);
}
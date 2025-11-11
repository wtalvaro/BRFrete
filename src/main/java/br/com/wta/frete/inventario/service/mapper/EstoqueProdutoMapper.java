package br.com.wta.frete.inventario.service.mapper;

import br.com.wta.frete.inventario.controller.dto.EstoqueProdutoRequest;
import br.com.wta.frete.inventario.controller.dto.EstoqueProdutoResponse;
import br.com.wta.frete.inventario.entity.EstoqueProduto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper para conversão entre a Entidade EstoqueProduto e seus DTOs.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EstoqueProdutoMapper {

	/**
	 * Converte um DTO de Requisição para a Entidade JPA. O campo 'produto' na
	 * Entidade é ignorado, pois o Service se encarrega de setá-lo com base no
	 * 'produtoId' do Request.
	 */
	@Mapping(target = "produto", ignore = true)
	EstoqueProduto toEntity(EstoqueProdutoRequest request);

	/**
	 * Converte a Entidade JPA para um DTO de Resposta.
	 */
	EstoqueProdutoResponse toResponse(EstoqueProduto entity);

	/**
	 * Atualiza uma Entidade existente com base nos dados do DTO de Requisição.
	 */
	@Mapping(target = "produtoId", ignore = true)
	@Mapping(target = "produto", ignore = true)
	@Mapping(target = "ultimaAtualizacao", ignore = true)
	void updateEntityFromRequest(EstoqueProdutoRequest request, @MappingTarget EstoqueProduto entity);
}
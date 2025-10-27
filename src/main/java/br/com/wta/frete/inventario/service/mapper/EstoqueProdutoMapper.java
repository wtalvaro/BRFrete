package br.com.wta.frete.inventario.service.mapper;

import br.com.wta.frete.inventario.controller.dto.EstoqueProdutoRequest;
import br.com.wta.frete.inventario.controller.dto.EstoqueProdutoResponse;
import br.com.wta.frete.inventario.entity.EstoqueProduto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper para conversão entre a Entidade EstoqueProduto e seus DTOs. Utiliza o
 * MapStruct para geração automática de código. ComponentModel "spring" integra
 * como Bean do Spring para injeção de dependência. O mapeamento ignora o campo
 * 'produto' da Entity, que deve ser setado manualmente no Service layer com
 * base no produtoId.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EstoqueProdutoMapper {

	/**
	 * Converte um DTO de Requisição para a Entidade JPA. O campo 'produto' na
	 * Entidade é ignorado, pois o Service se encarrega de setá-lo com base no
	 * 'produtoId' do Request.
	 * 
	 * @param request O DTO de requisição com os dados do estoque.
	 * @return A Entidade EstoqueProduto.
	 */
	@Mapping(target = "produto", ignore = true)
	EstoqueProduto toEntity(EstoqueProdutoRequest request);

	/**
	 * Converte a Entidade JPA para um DTO de Resposta. Mapeamento direto, pois os
	 * campos são compatíveis.
	 * 
	 * @param entity A Entidade EstoqueProduto.
	 * @return O DTO de resposta com os detalhes do estoque.
	 */
	EstoqueProdutoResponse toResponse(EstoqueProduto entity);

	/**
	 * Atualiza uma Entidade existente com base nos dados do DTO de Requisição. Útil
	 * para operações de atualização. O campo 'produto' e a chave primária
	 * 'produtoId' são explicitamente ignorados na atualização. A
	 * 'ultimaAtualizacao' deve ser tratada no Service.
	 * 
	 * @param request O DTO de requisição.
	 * @param entity  A Entidade a ser atualizada.
	 */
	@Mapping(target = "produtoId", ignore = true)
	@Mapping(target = "produto", ignore = true)
	@Mapping(target = "ultimaAtualizacao", ignore = true)
	void updateEntityFromRequest(EstoqueProdutoRequest request, @MappingTarget EstoqueProduto entity);
}
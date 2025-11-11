package br.com.wta.frete.inventario.service.mapper;

import br.com.wta.frete.inventario.controller.dto.EstoqueSucataRequest;
import br.com.wta.frete.inventario.controller.dto.EstoqueSucataResponse;
import br.com.wta.frete.inventario.entity.EstoqueSucata;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper para conversão entre a Entidade EstoqueSucata e seus DTOs.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EstoqueSucataMapper {

	/**
	 * Converte DTO de Requisição para a Entidade JPA. O campo 'sucateiro' na
	 * Entidade é ignorado, pois o Service se encarrega de setá-lo com base no
	 * 'sucateiroPessoaId' do Request.
	 */
	@Mapping(target = "sucateiro", ignore = true)
	EstoqueSucata toEntity(EstoqueSucataRequest request);

	/**
	 * Converte a Entidade JPA para um DTO de Resposta.
	 * Mapeia o Enum para o valor literal e sua descrição para o Response.
	 */
	@Mapping(source = "sucateiro.pessoa.id", target = "sucateiroPessoaId")
	@Mapping(source = "tipoMaterial.descricao", target = "tipoMaterialDescricao") // Adiciona a descrição do ENUM
	EstoqueSucataResponse toResponse(EstoqueSucata entity);

	/**
	 * Atualiza uma Entidade existente com base nos dados do DTO de Requisição.
	 * O ID, o Sucateiro e a Data de Atualização são ignorados.
	 */
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "sucateiro", ignore = true)
	@Mapping(target = "dataAtualizacao", ignore = true)
	void updateEntityFromRequest(EstoqueSucataRequest request, @MappingTarget EstoqueSucata entity);
}
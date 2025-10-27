package br.com.wta.frete.inventario.service.mapper;

import br.com.wta.frete.inventario.controller.dto.EstoqueSucataRequest;
import br.com.wta.frete.inventario.controller.dto.EstoqueSucataResponse;
import br.com.wta.frete.inventario.entity.EstoqueSucata;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper para conversão entre a Entidade EstoqueSucata e seus DTOs. Utiliza o
 * MapStruct para geração automática de código.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EstoqueSucataMapper {

	/**
	 * Converte um DTO de Requisição para a Entidade JPA. O campo 'id' (PK) e
	 * 'sucateiro' (relacionamento) na Entidade são ignorados. O campo
	 * 'dataAtualizacao' também é ignorado, pois é setado automaticamente na Entity.
	 * 
	 * @param request O DTO de requisição com os dados do estoque de sucata.
	 * @return A Entidade EstoqueSucata.
	 */
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "sucateiro", ignore = true)
	@Mapping(target = "dataAtualizacao", ignore = true)
	EstoqueSucata toEntity(EstoqueSucataRequest request);

	/**
	 * Converte a Entidade JPA para um DTO de Resposta. Mapeamento específico: -
	 * 'id' (Entity) -> 'estoqueId' (Response). - O ID do sucateiro é acessado
	 * através do relacionamento 'sucateiro.id' (Entity) -> 'sucateiroPessoaId'
	 * (Response).
	 * 
	 * @param entity A Entidade EstoqueSucata.
	 * @return O DTO de resposta com os detalhes do estoque de sucata.
	 */
	@Mapping(source = "id", target = "estoqueId")
	@Mapping(source = "sucateiro.pessoaId", target = "sucateiroPessoaId")
	EstoqueSucataResponse toResponse(EstoqueSucata entity);

	/**
	 * Atualiza uma Entidade existente com base nos dados do DTO de Requisição. Útil
	 * para operações de atualização. Os campos 'id', 'sucateiro' e
	 * 'dataAtualizacao' são explicitamente ignorados.
	 * 
	 * @param request O DTO de requisição.
	 * @param entity  A Entidade a ser atualizada.
	 */
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "sucateiro", ignore = true)
	@Mapping(target = "dataAtualizacao", ignore = true)
	void updateEntityFromRequest(EstoqueSucataRequest request, @MappingTarget EstoqueSucata entity);
}
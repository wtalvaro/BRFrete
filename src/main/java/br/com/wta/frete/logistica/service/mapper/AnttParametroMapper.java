package br.com.wta.frete.logistica.service.mapper;

import br.com.wta.frete.logistica.controller.dto.AnttParametroRequest;
import br.com.wta.frete.logistica.controller.dto.AnttParametroResponse;
import br.com.wta.frete.logistica.entity.AnttParametro;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper para conversão entre a Entidade AnttParametro e seus DTOs. Utiliza o
 * MapStruct para geração automática de código.
 *
 * Entidade: AnttParametro (id, chave, valor, descricao, dataVigencia) Request:
 * AnttParametroRequest (anttParametroId, valor, descricao, dataVigencia)
 * Response: AnttParametroResponse (anttParametroId, chave, valor, descricao,
 * dataVigencia)
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AnttParametroMapper {

	/**
	 * Converte um DTO de Requisição para a Entidade JPA.
	 *
	 * Mapeamento Específico: - 'anttParametroId' (Request) -> 'id' (Entity).
	 *
	 * Regras: - O campo 'chave' na Entidade é ignorado, pois é definido fora do
	 * Request (geralmente fixo).
	 *
	 * @param request O DTO de requisição.
	 * @return A Entidade AnttParametro.
	 */
	@Mapping(source = "anttParametroId", target = "id")
	@Mapping(target = "chave", ignore = true)
	AnttParametro toEntity(AnttParametroRequest request);

	/**
	 * Converte a Entidade JPA para um DTO de Resposta.
	 *
	 * Mapeamento Específico: - 'id' (Entity) -> 'anttParametroId' (Response).
	 *
	 * @param entity A Entidade AnttParametro.
	 * @return O DTO de resposta.
	 */
	@Mapping(source = "id", target = "anttParametroId")
	AnttParametroResponse toResponse(AnttParametro entity);

	/**
	 * Atualiza uma Entidade existente com base nos dados do DTO de Requisição.
	 *
	 * Regras: - O ID ('id') da entidade e a chave ('chave') são ignorados para
	 * preservar a integridade da Entity.
	 *
	 * @param request O DTO de requisição.
	 * @param entity  A Entidade a ser atualizada.
	 */
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "chave", ignore = true)
	void updateEntityFromRequest(AnttParametroRequest request, @MappingTarget AnttParametro entity);
}
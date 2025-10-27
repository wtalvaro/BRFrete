package br.com.wta.frete.social.service.mapper;

import br.com.wta.frete.social.controller.dto.AvaliacaoRequest;
import br.com.wta.frete.social.controller.dto.AvaliacaoResponse;
import br.com.wta.frete.social.entity.Avaliacao;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper (MapStruct) para conversão entre a Entidade Avaliacao e seus
 * respectivos DTOs de Request e Response.
 */
@Mapper(componentModel = "spring", // Permite a injeção via Spring (@Autowired)
		unmappedTargetPolicy = ReportingPolicy.IGNORE) // Ignora campos que não serão mapeados (ex: id no Request)
public interface AvaliacaoMapper {

	/**
	 * Converte o DTO de Requisição (AvaliacaoRequest) para a Entidade (Avaliacao).
	 *
	 * O campo 'id' da Entidade não é mapeado aqui, pois é gerado pelo banco de
	 * dados. O campo 'dataAvaliacao' será preenchido na camada de Serviço.
	 *
	 * @param request O DTO de requisição da Avaliação.
	 * @return A Entidade Avaliacao.
	 */
	Avaliacao toEntity(AvaliacaoRequest request);

	/**
	 * Converte a Entidade (Avaliacao) para o DTO de Resposta (AvaliacaoResponse).
	 *
	 * O campo 'id' da Entidade é mapeado para 'avaliacaoId' do DTO de Resposta.
	 *
	 * @param entity A Entidade Avaliacao.
	 * @return O DTO de resposta da Avaliação.
	 */
	@Mapping(source = "id", target = "avaliacaoId")
	AvaliacaoResponse toResponse(Avaliacao entity);

	/**
	 * Converte uma lista de Entidades (Avaliacao) para uma lista de DTOs de
	 * Resposta (AvaliacaoResponse).
	 *
	 * @param entities A lista de Entidades Avaliacao.
	 * @return A lista de DTOs de resposta da Avaliação.
	 */
	List<AvaliacaoResponse> toResponseList(List<Avaliacao> entities);
}
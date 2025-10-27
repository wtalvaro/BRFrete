package br.com.wta.frete.social.service.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import br.com.wta.frete.social.controller.dto.SeguidorRequest;
import br.com.wta.frete.social.controller.dto.SeguidorResponse;
import br.com.wta.frete.social.entity.Seguidor;
import br.com.wta.frete.social.entity.SeguidorId;

/**
 * Mapper para conversão entre a Entidade Seguidor (social.seguidores) e seus
 * DTOs. Lida com a complexidade da chave composta SeguidorId.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SeguidorMapper {

	/**
	 * Converte SeguidorRequest para a Entidade Seguidor. * O MapStruct precisa de
	 * ajuda para criar o objeto 'SeguidorId' e atribuí-lo ao campo 'id'. Isso é
	 * feito com um método default helper. Os campos de relacionamento 'seguidor' e
	 * 'seguido' (objetos Pessoa) são ignorados, pois serão populados na camada de
	 * Serviço. * @param request O DTO de requisição.
	 * 
	 * @return A Entidade Seguidor.
	 */
	@Mapping(target = "id", expression = "java(toSeguidorId(request))")
	@Mapping(target = "seguidor", ignore = true)
	@Mapping(target = "seguido", ignore = true)
	Seguidor toEntity(SeguidorRequest request);

	/**
	 * Converte a Entidade Seguidor para o DTO de Resposta. * Os IDs são mapeados a
	 * partir dos campos do objeto aninhado 'id'. O campo 'status' (String) no
	 * Response é tipicamente preenchido pela Camada de Serviço após a operação ser
	 * concluída. * @param entity A Entidade Seguidor.
	 * 
	 * @return O DTO de resposta.
	 */
	@Mapping(source = "id.seguidorId", target = "seguidorId")
	@Mapping(source = "id.seguidoId", target = "seguidoId")
	@Mapping(target = "status", constant = "Relacionamento processado")
	SeguidorResponse toResponse(Seguidor entity);

	/**
	 * Mapeia uma lista de Entidades Seguidor para uma lista de DTOs de Resposta.
	 */
	List<SeguidorResponse> toResponseList(List<Seguidor> entities);

	/**
	 * [Método Helper do MapStruct] Cria o objeto de chave composta 'SeguidorId' a
	 * partir dos IDs contidos no Request DTO.
	 */
	default SeguidorId toSeguidorId(SeguidorRequest request) {
		if (request == null) {
			return null;
		}
		// Assumimos que a Entidade SeguidorId tem um construtor com todos os argumentos
		// (Lombok @AllArgsConstructor).
		return new SeguidorId(request.seguidorId(), request.seguidoId());
	}
}
package br.com.wta.frete.logistica.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import br.com.wta.frete.logistica.controller.dto.StatusLeilaoResponse;
import br.com.wta.frete.logistica.entity.StatusLeilao;

/**
 * Mapper para conversão entre a Entidade StatusLeilao e a DTO
 * StatusLeilaoResponse. Utiliza a biblioteca MapStruct para geração automática
 * do código de conversão.
 */
@Mapper(componentModel = "spring")
public interface StatusLeilaoMapper {

	/**
	 * Mapeia e retorna a instância única (Singleton) do Mapper. Útil para ambientes
	 * fora do Spring ou para testes unitários, mas em um contexto Spring, o ideal é
	 * injetar a interface.
	 */
	StatusLeilaoMapper INSTANCE = Mappers.getMapper(StatusLeilaoMapper.class);

	/**
	 * Converte a entidade StatusLeilao para a DTO de resposta StatusLeilaoResponse.
	 *
	 * O campo 'id' da entidade é explicitamente mapeado para 'statusLeilaoId' da
	 * DTO, pois os nomes são diferentes. O campo 'nomeStatus' é mapeado
	 * automaticamente por ter o mesmo nome.
	 *
	 * @param entity A entidade StatusLeilao a ser convertida.
	 * @return A DTO StatusLeilaoResponse resultante.
	 */
	@Mapping(source = "id", target = "statusLeilaoId")
	StatusLeilaoResponse toResponse(StatusLeilao entity);

}
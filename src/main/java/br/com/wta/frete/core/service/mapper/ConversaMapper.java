package br.com.wta.frete.core.service.mapper;

import br.com.wta.frete.core.controller.dto.ConversaResponse;
import br.com.wta.frete.core.entity.Conversa;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * Interface de Mapeamento (Mapper) utilizando o MapStruct para conversão entre
 * a Entidade Conversa e o DTO ConversaResponse.
 */
@Mapper(componentModel = "spring")
public interface ConversaMapper {

	// Instância estática para uso fora do contexto Spring (opcional, mas bom ter)
	ConversaMapper INSTANCE = Mappers.getMapper(ConversaMapper.class);

	/**
	 * Mapeia a entidade Conversa para o DTO ConversaResponse. * @param conversa A
	 * entidade Conversa a ser mapeada.
	 * 
	 * @return O DTO ConversaResponse resultante.
	 */
	@Mapping(source = "id", target = "conversaId")
	ConversaResponse toResponse(Conversa conversa);

	/**
	 * Mapeia uma lista de entidades Conversa para uma lista de DTOs
	 * ConversaResponse. * @param conversas A lista de entidades Conversa a ser
	 * mapeada.
	 * 
	 * @return A lista de DTOs ConversaResponse resultante.
	 */
	java.util.List<ConversaResponse> toResponseList(java.util.List<Conversa> conversas);
}
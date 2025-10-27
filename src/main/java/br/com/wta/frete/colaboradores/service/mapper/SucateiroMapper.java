package br.com.wta.frete.colaboradores.service.mapper;

import br.com.wta.frete.colaboradores.controller.dto.SucateiroRequest;
import br.com.wta.frete.colaboradores.controller.dto.SucateiroResponse;
import br.com.wta.frete.colaboradores.entity.Sucateiro;
import br.com.wta.frete.core.service.mapper.PessoaMapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

/**
 * Interface Mapper para converter entre a Entidade Sucateiro e seus DTOs.
 */
@Mapper(componentModel = "spring", uses = { PessoaMapper.class })
public interface SucateiroMapper {

	SucateiroMapper INSTANCE = Mappers.getMapper(SucateiroMapper.class);

	/**
	 * Converte um SucateiroRequest (DTO de entrada) para a Entidade Sucateiro.
	 */
	// REMOVIDA A ANOTAÇÃO DE MAPPING: 'enderecoPatio' existe em ambos, então o
	// MapStruct mapeia automaticamente.
	@Mapping(target = "pessoa", ignore = true) // Continua ignorando o objeto 'pessoa', a ser populado no Service.
	Sucateiro toEntity(SucateiroRequest dto);

	/**
	 * Converte a Entidade Sucateiro para um SucateiroResponse (DTO de saída). O
	 * mapeamento de 'enderecoPatio' será automático.
	 */
	@Mapping(source = "pessoa", target = "dadosPessoa") // O único mapeamento explícito necessário é o relacionamento.
	SucateiroResponse toResponse(Sucateiro entity);

	/**
	 * Atualiza uma Entidade Sucateiro existente com os dados de um
	 * SucateiroRequest.
	 */
	// REMOVIDA A ANOTAÇÃO DE MAPPING: 'enderecoPatio' é mapeado automaticamente.
	@Mapping(target = "pessoaId", ignore = true)
	@Mapping(target = "pessoa", ignore = true)
	void updateEntityFromRequest(SucateiroRequest request, @MappingTarget Sucateiro target);
}
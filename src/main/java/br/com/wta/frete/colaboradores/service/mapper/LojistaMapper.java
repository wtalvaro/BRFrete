package br.com.wta.frete.colaboradores.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import br.com.wta.frete.colaboradores.controller.dto.LojistaRequest;
import br.com.wta.frete.colaboradores.controller.dto.LojistaResponse;
import br.com.wta.frete.colaboradores.entity.Lojista;
import br.com.wta.frete.core.service.mapper.PessoaMapper;

/**
 * Interface Mapper para converter entre a Entidade Lojista e seus DTOs. Usamos
 * 'uses = {PessoaMapper.class}' para que o MapStruct saiba usar o PessoaMapper
 * no relacionamento Pessoa -> PessoaResponse.
 */
@Mapper(componentModel = "spring", uses = { PessoaMapper.class })
public interface LojistaMapper {

	// Caso precise de uma instância manual (fora do Spring)
	LojistaMapper INSTANCE = Mappers.getMapper(LojistaMapper.class);

	/**
	 * Converte um LojistaRequest (DTO de entrada) para a Entidade Lojista. * @param
	 * dto O DTO de requisição.
	 * * @return A nova entidade Lojista.
	 */
	@Mapping(source = "enderecoPrincipal", target = "enderecoColeta")
	@Mapping(target = "pessoa", ignore = true) // Será associado no Service
	Lojista toEntity(LojistaRequest dto);

	/**
	 * Converte a Entidade Lojista para um LojistaResponse (DTO de saída). * @param
	 * entity A entidade Lojista.
	 * * @return O DTO de resposta.
	 */
	@Mapping(source = "enderecoColeta", target = "enderecoPrincipal")
	@Mapping(source = "pessoa", target = "dadosPessoa") // Mapeia o relacionamento usando o PessoaMapper
	LojistaResponse toResponse(Lojista entity);

	/**
	 * Atualiza uma Entidade Lojista existente com os dados de um LojistaRequest.
	 * * @param request O DTO de requisição.
	 * * @param target A entidade Lojista a ser atualizada.
	 */
	@Mapping(source = "enderecoPrincipal", target = "enderecoColeta")
	@Mapping(target = "pessoaId", ignore = true) // PK nunca deve ser alterada
	@Mapping(target = "pessoa", ignore = true) // Não sobrescreve o objeto Pessoa.
	void updateEntityFromRequest(LojistaRequest request, @MappingTarget Lojista target);
}
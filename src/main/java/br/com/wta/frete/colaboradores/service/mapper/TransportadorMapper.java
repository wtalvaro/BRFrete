package br.com.wta.frete.colaboradores.service.mapper;

import br.com.wta.frete.colaboradores.controller.dto.TransportadorRequest;
import br.com.wta.frete.colaboradores.controller.dto.TransportadorResponse;
import br.com.wta.frete.colaboradores.entity.Transportador;
import br.com.wta.frete.core.service.mapper.PessoaMapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 * Interface Mapper para converter entre a Entidade Transportador e seus DTOs.
 * Utiliza PessoaMapper para mapear o relacionamento Pessoa -> PessoaResponse.
 */
@Mapper(componentModel = "spring", uses = { PessoaMapper.class })
public interface TransportadorMapper {

	/**
	 * Converte TransportadorRequest para a Entidade Transportador. O campo 'pessoa'
	 * da Entity é IGNORADO pois o Request só tem o 'pessoaId'. O 'pessoa' será
	 * populado no Service. * @param dto O DTO de requisição.
	 * 
	 * @return A nova entidade Transportador.
	 */
	@Mapping(target = "pessoa", ignore = true) // <--- CORREÇÃO AQUI
	Transportador toEntity(TransportadorRequest dto);

	/**
	 * Converte a Entidade Transportador para um TransportadorResponse. Mapeia o
	 * relacionamento 'pessoa' para 'dadosPessoa' (usa PessoaMapper). * @param
	 * entity A entidade Transportador.
	 * 
	 * @return O DTO de resposta.
	 */
	@Mapping(source = "pessoa", target = "dadosPessoa")
	TransportadorResponse toResponse(Transportador entity);

	/**
	 * Atualiza uma Entidade Transportador existente com os dados de um Request. O
	 * 'pessoaId' e o objeto 'pessoa' são IGNORADOS para preservar a integridade.
	 * * @param dto O DTO de requisição com os dados de atualização.
	 * 
	 * @param target A entidade Transportador a ser atualizada.
	 */
	@Mapping(target = "pessoaId", ignore = true) // Ignora a chave primária
	@Mapping(target = "pessoa", ignore = true) // <--- CORREÇÃO AQUI
	void updateEntityFromRequest(TransportadorRequest dto, @MappingTarget Transportador target);
}
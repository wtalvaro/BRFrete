package br.com.wta.frete.logistica.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import br.com.wta.frete.logistica.controller.dto.LanceRequest;
import br.com.wta.frete.logistica.controller.dto.LanceResponse;
import br.com.wta.frete.logistica.entity.Lance;

/**
 * Mapeador MapStruct para conversão entre DTOs e a entidade Lance.
 */
@Mapper(componentModel = "spring")
public interface LanceMapper {

	/**
	 * Converte o DTO de Requisição para a Entidade Lance.
	 */
	@Mappings({
			@Mapping(target = "id", ignore = true),
			@Mapping(target = "frete", ignore = true),
			@Mapping(target = "transportador", ignore = true),
			// O MapStruct pode precisar de ajuda extra quando o nome do DTO não coincide
			// exatamente com o nome da Entidade (valorProposto/valorLance), mas a
			// correção anterior já alinhou para 'valorLance', então o mapeamento é
			// implícito ou por getter. Vamos manter limpo.
			@Mapping(target = "dataLance", ignore = true), // Será setado no Service (agora)
			@Mapping(target = "vencedor", ignore = true), // Será setado no Service
			@Mapping(target = "motivoCancelamento", ignore = true) // Será setado no Service ou no futuro
	})
	Lance toEntity(LanceRequest request);

	/**
	 * Converte a Entidade Lance para o DTO de Resposta.
	 */
	@Mappings({
			@Mapping(source = "id", target = "lanceId"),
			@Mapping(source = "frete.freteId", target = "freteId"),
			@Mapping(source = "transportador.pessoaId", target = "transportadorId"),

			// CORREÇÃO: Mapeamentos explícitos para resolver o erro "Unmapped target
			// properties"
			@Mapping(source = "dataLance", target = "dataLance"),
			@Mapping(source = "vencedor", target = "vencedor"),
			@Mapping(source = "motivoCancelamento", target = "motivoCancelamento"),

			// Lógica para buscar o nome do Transportador: transportador -> pessoa -> nome
			@Mapping(target = "nomeTransportador", expression = "java(entity.getTransportador() != null && entity.getTransportador().getPessoa() != null ? entity.getTransportador().getPessoa().getNome() : \"Desconhecido\")")
	})
	LanceResponse toResponse(Lance entity);
}
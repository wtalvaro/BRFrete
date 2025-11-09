package br.com.wta.frete.logistica.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

import br.com.wta.frete.logistica.controller.dto.FreteRequest;
import br.com.wta.frete.logistica.controller.dto.FreteResponse;
import br.com.wta.frete.logistica.entity.Frete;

/**
 * Mapeador MapStruct para conversão entre DTOs e a entidade Frete.
 */
@Mapper(componentModel = "spring")
public interface FreteMapper {

	// Mapeamento de FreteRequest para Frete (Entidade) - (Inalterado da última
	// correção)
	@Mappings({
			@Mapping(source = "prazoEncerramento", target = "dataExpiracaoNegociacao"),
			@Mapping(target = "pesoTotalKg", ignore = true),
			@Mapping(target = "freteId", ignore = true),
			@Mapping(target = "ordemServico", ignore = true),
			@Mapping(target = "modalidade", ignore = true),
			@Mapping(target = "statusLeilao", ignore = true),
			@Mapping(target = "transportadorSelecionado", ignore = true),
			@Mapping(target = "distanciaKm", ignore = true),
			@Mapping(target = "anttPisoMinimo", ignore = true),
			@Mapping(target = "precoSugerido", ignore = true),
			@Mapping(target = "custoBaseMercado", ignore = true),
			@Mapping(target = "valorFinalAceito", ignore = true),
			@Mapping(source = "tipoEmbalagem", target = "tipoEmbalagem"),
			@Mapping(source = "valorInicialProposto", target = "valorInicialProposto")
	})
	Frete toEntity(FreteRequest request);

	// Mapeamento de Frete (Entidade) para FreteResponse
	@Mappings({
			@Mapping(source = "freteId", target = "freteId"),
			@Mapping(source = "ordemServico.id", target = "ordemServicoId"),
			@Mapping(source = "transportadorSelecionado.pessoaId", target = "transportadorSelecionadoId"),
			@Mapping(source = "modalidade.id", target = "modalidadeId"),
			@Mapping(source = "modalidade.nomeModalidade", target = "nomeModalidade"),
			@Mapping(source = "statusLeilao.id", target = "statusLeilaoId"),
			@Mapping(source = "statusLeilao.nomeStatus", target = "nomeStatusLeilao")
	})
	FreteResponse toResponse(Frete entity);

	// Mapeamento de atualização (updateEntity) - (Inalterado da última correção)
	@Mappings({
			@Mapping(source = "prazoEncerramento", target = "dataExpiracaoNegociacao"),
			@Mapping(target = "pesoTotalKg", ignore = true),
			@Mapping(target = "freteId", ignore = true),
			@Mapping(target = "ordemServico", ignore = true),
			@Mapping(target = "modalidade", ignore = true),
			@Mapping(target = "statusLeilao", ignore = true),
			@Mapping(target = "transportadorSelecionado", ignore = true),
			@Mapping(target = "distanciaKm", ignore = true),
			@Mapping(target = "anttPisoMinimo", ignore = true),
			@Mapping(target = "precoSugerido", ignore = true),
			@Mapping(target = "custoBaseMercado", ignore = true),
			@Mapping(target = "valorFinalAceito", ignore = true),
			@Mapping(source = "tipoEmbalagem", target = "tipoEmbalagem"),
			@Mapping(source = "valorInicialProposto", target = "valorInicialProposto")
	})
	void updateEntity(FreteRequest request, @MappingTarget Frete entity);
}
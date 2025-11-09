package br.com.wta.frete.logistica.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import br.com.wta.frete.logistica.controller.dto.CotacaoMaterialResponse;
import br.com.wta.frete.logistica.entity.CotacaoMaterial;

/**
 * Interface Mapper MapStruct para conversão entre a Entidade CotacaoMaterial
 * (logistica.cotacoes_materiais) e seus DTOs.
 */
// Removemos a convenção MapStruct.Mappers.INSTANCE e focamos no componente
// Spring.
@Mapper(componentModel = "spring")
public interface CotacaoMaterialMapper {

	/**
	 * Converte a Entidade CotacaoMaterial para o DTO de Resposta
	 * CotacaoMaterialResponse, mapeando todos os campos atualizados.
	 */
	@Mappings({
			@Mapping(source = "id", target = "cotacaoId"), // PK
			@Mapping(source = "dataVigencia", target = "dataCotacao") // dataVigencia mapeada para dataCotacao do DTO
			// Campos mapeados implicitamente: materialNome, precoMedioKg, unidadeMedida,
			// dataAtualizacao
	})
	CotacaoMaterialResponse toResponse(CotacaoMaterial entity);

	// NOTA: Os métodos toEntity(Request) e updateEntity(Request, @MappingTarget)
	// foram omitidos, pois não há CotacaoMaterialRequest, alinhado com a revisão.
}
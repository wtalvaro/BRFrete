package br.com.wta.frete.logistica.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import br.com.wta.frete.logistica.controller.dto.CotacaoMaterialResponse;
import br.com.wta.frete.logistica.entity.CotacaoMaterial;

/**
 * Interface Mapper MapStruct para conversão entre a Entidade CotacaoMaterial
 * (logistica.cotacoes_materiais) e seus DTOs.
 */
@Mapper(componentModel = "spring")
public interface CotacaoMaterialMapper {

	// O MapStruct pode gerar uma instância estática, mas a injeção Spring é
	// preferida.
	// Deixamos a fábrica aqui apenas como convenção MapStruct, mas o uso será via
	// @Autowired.
	CotacaoMaterialMapper INSTANCE = Mappers.getMapper(CotacaoMaterialMapper.class);

	/**
	 * Converte a Entidade CotacaoMaterial para o DTO de Resposta
	 * CotacaoMaterialResponse. * Mapeamentos explícitos: - 'id' da entidade é
	 * mapeado para 'cotacaoId' no DTO. - 'dataAtualizacao' da entidade é mapeado
	 * para 'dataCotacao' no DTO. O MapStruct lida automaticamente com a conversão
	 * de ZonedDateTime para LocalDate. * @param entity A entidade CotacaoMaterial.
	 * 
	 * @return O DTO de resposta com os dados mapeados.
	 */
	@Mapping(source = "id", target = "cotacaoId") // Mapeia o campo 'id' da entidade para 'cotacaoId' no DTO
	@Mapping(source = "dataAtualizacao", target = "dataCotacao") // NOVO MAPPEAMENTO: Resolve o 'Unmapped target
																	// property'
	CotacaoMaterialResponse toResponse(CotacaoMaterial entity);
}
package br.com.wta.frete.core.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import br.com.wta.frete.core.controller.dto.ContaDigitalResponse;
import br.com.wta.frete.core.entity.ContaDigital;
import br.com.wta.frete.core.entity.enums.StatusKYC;

/**
 * Mapper para conversão entre a Entidade ContaDigital e o DTO
 * ContaDigitalResponse. Configurado como componente Spring.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ContaDigitalMapper {

	/**
	 * Converte a entidade ContaDigital para o DTO de resposta ContaDigitalResponse.
	 * * Nota: O MapStruct converte o Enum StatusKYC (Entity) para String (DTO)
	 * automaticamente (usando o nome da constante do Enum). * @param entity A
	 * entidade ContaDigital a ser convertida.
	 * 
	 * @return O DTO ContaDigitalResponse.
	 */
	ContaDigitalResponse toResponse(ContaDigital entity);

	// Opcional: Adicionar um mapeamento para converter o StatusKYC para String de
	// forma explícita
	// Isso é opcional, pois a conversão implícita já funciona, mas é um bom
	// exemplo!
	default String mapStatusKyc(StatusKYC status) {
		return status != null ? status.name() : null;
	}
}
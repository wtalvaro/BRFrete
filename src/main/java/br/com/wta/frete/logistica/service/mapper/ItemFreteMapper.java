package br.com.wta.frete.logistica.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import br.com.wta.frete.logistica.controller.dto.ItemFreteRequest;
import br.com.wta.frete.logistica.controller.dto.ItemFreteResponse;
import br.com.wta.frete.logistica.entity.Frete;
import br.com.wta.frete.logistica.entity.ItemFrete;

/**
 * Mapper para a entidade ItemFrete, responsável por converter entre
 * ItemFreteRequest/Response e ItemFrete (Entidade).
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ItemFreteMapper {

	ItemFreteMapper INSTANCE = Mappers.getMapper(ItemFreteMapper.class);

	/**
	 * Converte um ItemFreteRequest para a entidade ItemFrete. Mapeia
	 * 'ordemServicoId' do Request para a entidade 'frete' em ItemFrete.
	 * 
	 * @param request DTO de requisição (entrada de dados).
	 * @return A entidade ItemFrete.
	 */
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "frete", source = "ordemServicoId")
	ItemFrete toEntity(ItemFreteRequest request);

	/**
	 * Converte a entidade ItemFrete para o DTO de Resposta ItemFreteResponse.
	 * Mapeia 'frete.ordemServicoId' (PK do Frete) para 'ordemServicoId' do
	 * Response.
	 * 
	 * @param entity Entidade ItemFrete.
	 * @return O DTO de resposta.
	 */
	@Mapping(target = "itemFreteId", source = "id")
	@Mapping(target = "ordemServicoId", source = "frete.ordemServicoId") // Corrigido para frete.ordemServicoId
	ItemFreteResponse toResponse(ItemFrete entity);

	/**
	 * Método auxiliar para o MapStruct criar a entidade Frete a partir do ID. Agora
	 * usa o setter correto: setOrdemServicoId().
	 * 
	 * @param ordemServicoId O ID da Ordem de Serviço (Frete).
	 * @return Uma nova entidade Frete com apenas o ID preenchido.
	 */
	default Frete toFrete(Long ordemServicoId) {
		if (ordemServicoId == null) {
			return null;
		}

		// CORREÇÃO: Usando o setter correto gerado pelo Lombok para a PK da Entidade
		// Frete.
		Frete frete = new Frete();
		frete.setOrdemServicoId(ordemServicoId);

		return frete;
	}
}
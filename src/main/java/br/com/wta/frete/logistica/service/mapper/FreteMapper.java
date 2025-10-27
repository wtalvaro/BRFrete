package br.com.wta.frete.logistica.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

import br.com.wta.frete.logistica.controller.dto.FreteRequest;
import br.com.wta.frete.logistica.controller.dto.FreteResponse;
import br.com.wta.frete.logistica.entity.Frete;
import br.com.wta.frete.logistica.entity.ModalidadeFrete;
import br.com.wta.frete.logistica.entity.StatusLeilao;

/**
 * Mapper para a entidade Frete, responsável por converter entre FreteRequest,
 * FreteResponse e a entidade Frete.
 */
@Mapper(componentModel = "spring")
public interface FreteMapper {

	/**
	 * Mapeia o DTO de Requisição para a entidade Frete. * @param request O DTO de
	 * requisição do Frete.
	 * 
	 * @return A entidade Frete.
	 */
	@Mapping(target = "modalidade", ignore = true)
	@Mapping(target = "statusLeilao", ignore = true)
	@Mapping(target = "ordemServico", ignore = true) // A entidade OrdemServico será associada no Service
	@Mapping(target = "valorFinalAceito", ignore = true) // CORREÇÃO: Não existe no Request, será preenchido após o
															// leilão.
	Frete toEntity(FreteRequest request);

	/**
	 * Mapeia a entidade Frete para o DTO de Resposta. * @param entity A entidade
	 * Frete.
	 * 
	 * @return O DTO de resposta do Frete.
	 */
	@Mappings({ @Mapping(source = "modalidade.id", target = "modalidadeId"),
			@Mapping(source = "statusLeilao.id", target = "statusLeilaoId") })
	FreteResponse toResponse(Frete entity);

	/**
	 * Atualiza uma entidade Frete existente com base nos dados do DTO de
	 * requisição. * @param request O DTO de requisição com os dados de atualização.
	 * 
	 * @param target A entidade Frete a ser atualizada.
	 */
	@Mapping(target = "ordemServicoId", ignore = true) // Chave primária não deve ser alterada
	@Mapping(target = "ordemServico", ignore = true)
	@Mapping(target = "modalidade", ignore = true)
	@Mapping(target = "statusLeilao", ignore = true)
	@Mapping(target = "valorFinalAceito", ignore = true) // CORREÇÃO: Deve ser ignorado para não ser sobrescrito pelo
															// valor default (null) do Request.
	void updateEntityFromRequest(FreteRequest request, @MappingTarget Frete target);

	// --- Métodos Auxiliares para Relacionamentos ---

	/**
	 * Converte um ID simples para uma entidade ModalidadeFrete. A lógica de busca
	 * real deve estar no Service.
	 */
	default ModalidadeFrete mapModalidadeFrete(Integer id) {
		if (id == null) {
			return null;
		}
		// Construtor correto: (Integer id, String nomeModalidade)
		return new ModalidadeFrete(id, null);
	}

	/**
	 * Converte um ID simples para uma entidade StatusLeilao. A lógica de busca real
	 * deve estar no Service.
	 */
	default StatusLeilao mapStatusLeilao(Integer id) {
		if (id == null) {
			return null;
		}
		// Construtor correto: (Integer id, String nomeStatus)
		return new StatusLeilao(id, null);
	}
}
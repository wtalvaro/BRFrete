package br.com.wta.frete.logistica.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import br.com.wta.frete.core.entity.enums.StatusServico;
import br.com.wta.frete.logistica.controller.dto.OrdemServicoRequest;
import br.com.wta.frete.logistica.controller.dto.OrdemServicoResponse;
import br.com.wta.frete.logistica.entity.OrdemServico;

/**
 * Interface Mapper para converter entre a Entidade OrdemServico e seus DTOs.
 * Garante que os novos nomes dos campos na Entidade sejam refletidos.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrdemServicoMapper {

	/**
	 * Mapeamento de Entidade (com Enum) para Response DTO (com String).
	 */
	@Mapping(source = "id", target = "ordemServicoId")
	// CORREÇÃO FINAL: Usando .pessoaId, o campo de ID correto nas entidades
	// DetalheCliente e Transportador.
	@Mapping(source = "clienteSolicitante.pessoaId", target = "clienteSolicitanteId")
	@Mapping(source = "transportadorDesignado.pessoaId", target = "transportadorDesignadoId")
	@Mapping(source = "status", target = "status", qualifiedByName = "statusServicoToString")
	OrdemServicoResponse toResponse(OrdemServico entity);

	/**
	 * Mapeamento de Request DTO (com String) para Entidade (com Enum).
	 * O Service fará a busca e associação das Entidades de FK (Cliente,
	 * Transportador).
	 */
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "clienteSolicitante", ignore = true)
	@Mapping(target = "transportadorDesignado", ignore = true)
	@Mapping(target = "dataSolicitacao", ignore = true)
	// CORREÇÃO: Removida a linha de ignorar "distanciaKm" pois o campo não existe
	// na Entidade.
	@Mapping(target = "status", ignore = true) // Status inicial é PENDENTE
	OrdemServico toEntity(OrdemServicoRequest request);

	/**
	 * Atualiza uma Entidade OrdemServico existente com os dados de um Request.
	 */
	@Mapping(target = "clienteSolicitante", ignore = true)
	@Mapping(target = "transportadorDesignado", ignore = true)
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "dataSolicitacao", ignore = true)
	// CORREÇÃO: Removida a linha de ignorar "distanciaKm" pois o campo não existe
	// na Entidade.
	@Mapping(target = "status", ignore = true)
	void updateEntityFromRequest(OrdemServicoRequest request, @MappingTarget OrdemServico target);

	// --- Métodos de Conversão Customizados (para mapeamento de Enum <-> String)
	// ---

	/**
	 * Converte o Enum StatusServico para String, usando a descrição amigável.
	 */
	@Named("statusServicoToString")
	default String statusServicoToString(StatusServico status) {
		return status != null ? status.getDescricao() : null;
	}

	/**
	 * Converte String (Descrição) para o Enum StatusServico.
	 */
	@Named("stringToStatusServico")
	default StatusServico stringToStatusServico(String status) {
		if (status == null || status.trim().isEmpty())
			return null;

		String statusUpperCase = status.trim().toUpperCase();

		try {
			// Tenta converter pelo nome da constante (o mais comum)
			return StatusServico.valueOf(statusUpperCase);
		} catch (IllegalArgumentException e) {
			// Fallback: Procura pela descrição
			for (StatusServico s : StatusServico.values()) {
				if (s.getDescricao().equalsIgnoreCase(statusUpperCase)) {
					return s;
				}
			}
		}
		return null; // Retorna null se não encontrar correspondência
	}
}
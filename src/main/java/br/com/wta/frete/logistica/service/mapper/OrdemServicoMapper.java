package br.com.wta.frete.logistica.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named; // IMPORTANTE: Certifique-se desta importação
import org.mapstruct.ReportingPolicy;

import br.com.wta.frete.core.entity.enums.StatusServico;
import br.com.wta.frete.logistica.controller.dto.OrdemServicoRequest;
import br.com.wta.frete.logistica.controller.dto.OrdemServicoResponse;
import br.com.wta.frete.logistica.entity.OrdemServico;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrdemServicoMapper {

	/**
	 * Mapeamento de Entidade (com Enum) para Response DTO (com String).
	 */
	@Mapping(source = "id", target = "ordemServicoId")
	@Mapping(source = "cliente.pessoaId", target = "clientePessoaId")
	@Mapping(source = "transportador.pessoaId", target = "transportadorPessoaId")
	@Mapping(source = "status", target = "status", qualifiedByName = "statusServicoToString")
	OrdemServicoResponse toResponse(OrdemServico entity);

	/**
	 * Mapeamento de Request DTO (com String) para Entidade (com Enum).
	 */
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "cliente", ignore = true)
	@Mapping(target = "transportador", ignore = true)
	@Mapping(target = "dataCriacao", ignore = true)
	@Mapping(source = "status", target = "status", qualifiedByName = "stringToStatusServico")
	OrdemServico toEntity(OrdemServicoRequest request);

	// --- Métodos de Conversão Customizados (para mapeamento de Enum <-> String)
	// ---

	/**
	 * Converte o Enum StatusServico para String, usando a descrição amigável.
	 */
	@Named("statusServicoToString")
	default String statusServicoToString(StatusServico status) {
		// CORREÇÃO: Usar a descrição (ex: "Pendente") em vez do nome da constante
		// (ex: "PENDENTE").
		return status != null ? status.getDescricao() : null;
	}

	/**
	 * Converte String (Descrição) para o Enum StatusServico (Procura pelo campo
	 * 'descricao').
	 */
	@Named("stringToStatusServico")
	default StatusServico stringToStatusServico(String status) {
		if (status == null || status.trim().isEmpty())
			return null;

		String statusLowerCase = status.trim().toLowerCase();

		// CORREÇÃO: Em vez de usar valueOf(), percorrer o Enum para encontrar a
		// correspondência pela descrição.
		for (StatusServico s : StatusServico.values()) {
			if (s.getDescricao().equalsIgnoreCase(statusLowerCase)) {
				return s;
			}
		}

		// Fallback: Tentar pelo nome da constante em maiúsculas (se o usuário enviou o
		// nome).
		try {
			return StatusServico.valueOf(status.trim().toUpperCase());
		} catch (IllegalArgumentException e) {
			System.err.println("Valor de status inválido: " + status);
			return null;
		}
	}
}
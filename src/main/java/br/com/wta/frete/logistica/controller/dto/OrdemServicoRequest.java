package br.com.wta.frete.logistica.controller.dto;

import java.time.LocalDate;
import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * DTO de Requisição (Record) para criar ou atualizar uma Ordem de Serviço
 * (logistica.ordens_servico).
 * Usa o padrão Record para imutabilidade e concisão.
 */
public record OrdemServicoRequest(

		// FK para o Cliente que está solicitando
		@NotNull(message = "O ID do Cliente é obrigatório") Long clienteSolicitanteId,

		// FK opcional para o Transportador (se já atribuído, pode ser null)
		Long transportadorDesignadoId,

		// Data prevista para coleta
		@NotNull(message = "A data prevista para coleta é obrigatória") LocalDate dataPrevistaColeta,

		// Localização
		@NotBlank(message = "O endereço de coleta é obrigatório") String enderecoColeta,
		@NotBlank(message = "O CEP de coleta é obrigatório") @Size(min = 8, max = 8, message = "O CEP deve ter 8 dígitos") String cepColeta,
		@NotBlank(message = "O CEP de destino é obrigatório") @Size(min = 8, max = 8, message = "O CEP deve ter 8 dígitos") String cepDestino,

		// Detalhe de Precificação
		/**
		 * Nome da Modalidade de Frete desejada (ex: ROD_PESADO, FRACIONADO).
		 * Campo obrigatório para que o FreteService possa criar o frete com
		 * flexibilidade.
		 */
		@NotBlank(message = "A Modalidade de Frete é obrigatória") String nomeModalidadeFrete, // <--- NOVO CAMPO

		// Itens
		@NotNull(message = "Pelo menos um item de frete é obrigatório.") @Size(min = 1, message = "A Ordem de Serviço deve ter pelo menos um item.") List<ItemFreteRequest> itensFrete) {
}
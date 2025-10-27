package br.com.wta.frete.core.controller.dto;

/**
 * DTO de Resposta para a entidade Perfil (core.perfis). Representa os dados de
 * um perfil de acesso ou função (Lookup).
 */
public record PerfilResponse(
		// Chave primária do perfil
		Integer perfilId,

		// Nome único do perfil (ex: CLIENTE, TRANSPORTADOR, ADMIN)
		String nomePerfil,

		// Descrição detalhada do perfil
		String descricao) {
	// Nota: O campo 'id' no código original foi renomeado para 'perfilId'
	// para melhor clareza e consistência com o nome da tabela 'perfis'.
}
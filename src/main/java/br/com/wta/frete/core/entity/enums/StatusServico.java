package br.com.wta.frete.core.entity.enums;

/**
 * Representa o tipo ENUM logistica.status_servico do banco de dados. Define os
 * possíveis estados de uma Ordem de Serviço (OS).
 */
public enum StatusServico {

	PENDENTE("Pendente"), CONFIRMADO("Confirmado"), COLETADO("Coletado"), EM_TRANSPORTE("Em Transporte"),
	ENTREGUE("Entregue"), CANCELADO("Cancelado");

	private final String descricao;

	StatusServico(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}
}
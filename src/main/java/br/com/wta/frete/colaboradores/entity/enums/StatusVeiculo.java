// src/main/java/br/com/wta/frete/api/entity/colaboradores/enums/StatusVeiculo.java
package br.com.wta.frete.colaboradores.entity.enums;

/**
 * Define o status de disponibilidade de um Veículo, substituindo o campo String
 * 'status_veiculo' na entidade Veiculo.
 */
public enum StatusVeiculo {
	DISPONIVEL("Disponível"), EM_MANUTENCAO("Em Manutenção"), EM_VIAGEM("Em Viagem"), INATIVO("Inativo");

	private final String descricao;

	StatusVeiculo(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}
}
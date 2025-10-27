// src/main/java/br/com/wta/frete/api/entity/colaboradores/enums/TipoVeiculo.java
package br.com.wta.frete.colaboradores.entity.enums;

/**
 * Define os tipos de veículos utilizados pelos Transportadores, substituindo o
 * campo String 'tipo_veiculo' na entidade Veiculo.
 */
public enum TipoVeiculo {
	CAMINHAO_BAU("Caminhão Baú"), CAMINHAO_CACAMBA("Caminhão Caçamba"), VAN("Van"), CARRETA("Carreta"),
	UTILITARIO("Utilitário");

	private final String descricao;

	TipoVeiculo(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}
}
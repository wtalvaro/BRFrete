// src/main/java/br/com/wta/frete/api/entity/core/enums/TipoCliente.java
package br.com.wta.frete.core.entity.enums;

/**
 * Define os possíveis tipos de cliente (PF, PJ, Cooperativa), substituindo o
 * campo String 'tipo_cliente' na entidade DetalheCliente.
 */
public enum TipoCliente {
	PESSOA_FISICA("Pessoa Física"), PESSOA_JURIDICA("Pessoa Jurídica"), COOPERATIVA("Cooperativa"),
	ASSOCIACAO("Associação");

	private final String descricao;

	TipoCliente(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}
}
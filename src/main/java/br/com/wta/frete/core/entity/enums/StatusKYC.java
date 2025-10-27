// src/main/java/br/com/wta/frete/api/entity/core/enums/StatusKYC.java
package br.com.wta.frete.core.entity.enums;

/**
 * Define os possíveis status do processo KYC (Know Your Customer) da conta
 * digital, substituindo o campo String 'status_kyc' na entidade ContaDigital.
 */
public enum StatusKYC {
	PENDENTE("Pendente"), EM_REVISAO("Em Revisão"), APROVADO("Aprovado"), REPROVADO("Reprovado");

	private final String descricao;

	StatusKYC(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}
}
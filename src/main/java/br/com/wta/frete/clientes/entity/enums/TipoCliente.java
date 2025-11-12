package br.com.wta.frete.clientes.entity.enums;

/**
 * Define os possíveis tipos de cliente (PF, PJ, Cooperativa, Governo),
 * substituindo o campo String 'tipo_cliente' na entidade DetalheCliente.
 * Alinhado com o ENUM 'clientes.tipo_cliente_enum' do PostgreSQL.
 */
public enum TipoCliente {
	PESSOA_FISICA("Pessoa Física"),
	PESSOA_JURIDICA("Pessoa Jurídica"),
	COOPERATIVA("Cooperativa"),
	GOVERNO("Governo"); // CORRIGIDO: Alinhamento com o ENUM do DB

	private final String descricao;

	TipoCliente(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}
}
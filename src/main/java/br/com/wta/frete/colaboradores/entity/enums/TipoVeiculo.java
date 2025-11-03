package br.com.wta.frete.colaboradores.entity.enums;

/**
 * Define os tipos de veículos utilizados pelos Transportadores, mapeando
 * a taxonomia completa utilizada no ENUM colaboradores.tipo_veiculo_enum do DB.
 */
public enum TipoVeiculo {
	// VEÍCULOS LEVES E URBANOS
	VEICULO_UTILITARIO("Veículo Utilitário (Pequeno Porte)"),
	VUC("Veículo Urbano de Carga (Caminhão 3/4)"),
	FURGAO("Furgão / Van de Carga"),

	// CAMINHÕES RÍGIDOS (POR EIXO)
	TOCO_2_EIXOS("Caminhão Toco (2 Eixos, Semi-pesado)"),
	TRUCK_3_EIXOS("Caminhão Truck (3 Eixos, Pesado)"),
	BITRUCK_4_EIXOS("Caminhão Bitruck (4 Eixos)"),

	// CARRETAS E COMBINAÇÕES SIMPLES
	CAVALO_MECANICO_SIMPLES("Cavalo Mecânico Simples (4x2)"),
	CARRETA_2_EIXOS("Carreta Simples (2 Eixos no Semirreboque)"),
	CARRETA_3_EIXOS("Carreta Simples (3 Eixos no Semirreboque)"),
	CARRETA_LS("Carreta com Cavalo Trucado (6x2/6x4)"),

	// COMBINAÇÕES ARTICULADAS DE MAIOR PORTE
	BITREM_7_EIXOS("Combinação Bitrem (7 Eixos, 2 Articulações)"),
	RODOTREM_9_EIXOS("Combinação Rodotrem (9 Eixos ou mais, 3 Articulações)"),
	VANDERLEIA_3_EIXOS("Semirreboque Vanderléia (3 Eixos distantes)"),

	// VEÍCULOS POR CARROCERIA (Especializados)
	BAU_SECO("Baú Fechado (Carga Seca)"),
	BAU_FRIGORIFICO("Baú Frigorífico / Refrigerado"),
	CACAMBA_BASICA("Caçamba (Basculante, para Granel)"),
	TANQUE("Caminhão Tanque (Líquidos / Gases)"),
	CEGONHA("Cegonha (Transporte de Veículos)"),
	PORTA_CONTAINER("Chassi Porta-Container"),
	GRADE_BAIXA("Grade Baixa / Caminhão Plataforma"),
	GRANELEIRO("Graneleiro (Para Grãos e Produtos a Granel)"),

	// CATEGORIA GENÉRICA
	ESPECIAL("Veículo Especial / Fora de Padrão");

	private final String descricao;

	TipoVeiculo(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}
}
package br.com.wta.frete.marketplace.entity.enums;

import lombok.Getter;

/**
 * Representa o ENUM marketplace.unidade_medida_enum do banco de dados.
 * Define as unidades de medida válidas para os produtos no marketplace.
 */
@Getter
public enum UnidadeMedidaEnum {
    // CONTÁGEM (COUNT)
    UN("Unidade"), // Unidade
    PC("Pacote/Peça"), // Pacote/Peça
    DZ("Dúzia"), // Dúzia
    CX("Caixa"), // Caixa
    KIT("Kit"), // Kit

    // MASSA/PESO (WEIGHT)
    KG("Quilograma"), // Quilograma
    GR("Grama"), // Grama
    TON("Tonelada"), // Tonelada
    LB("Libra"), // Libra

    // COMPRIMENTO (LENGTH)
    M("Metro"), // Metro
    CM("Centímetro"), // Centímetro
    MM("Milímetro"), // Milímetro
    KM("Quilômetro"), // Quilômetro

    // ÁREA (AREA)
    M2("Metro Quadrado"), // Metro Quadrado
    CM2("Centímetro Quadrado"), // Centímetro Quadrado

    // VOLUME (VOLUME)
    M3("Metro Cúbico"), // Metro Cúbico
    L("Litro"), // Litro
    ML("Mililitro"), // Mililitro
    GAL("Galão"), // Galão

    // TEMPO/SERVIÇO (TIME/SERVICE)
    H("Hora"), // Hora
    DIA("Dia"); // Dia

    private final String descricao;

    UnidadeMedidaEnum(String descricao) {
        this.descricao = descricao;
    }

    /**
     * Retorna a descrição completa da unidade de medida.
     * 
     * @return a descrição da unidade.
     */
    public String getDescricao() {
        return descricao;
    }

}
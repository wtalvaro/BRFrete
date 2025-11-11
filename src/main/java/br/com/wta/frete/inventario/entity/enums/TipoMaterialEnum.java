package br.com.wta.frete.inventario.entity.enums;

import lombok.Getter;

/**
 * Define os tipos de materiais de sucata/reciclagem aceitos no estoque,
 * alinhado com o ENUM 'inventario.tipo_material_enum' do banco de dados.
 */
@Getter
public enum TipoMaterialEnum {
    ALUMINIO("Alumínio"),
    COBRE("Cobre"),
    FERRO("Ferro"),
    ACO("Aço"),
    PLASTICO_PET("Plástico (PET)"),
    PAPELAO("Papelão"),
    VIDRO("Vidro"),
    MADEIRA("Madeira"),
    BORRACHA("Borracha"),
    OUTROS("Outros Materiais");

    private final String descricao;

    TipoMaterialEnum(String descricao) {
        this.descricao = descricao;
    }

    /**
     * Retorna a descrição completa do tipo de material.
     * * @return a descrição.
     */
    public String getDescricao() {
        return descricao;
    }
}
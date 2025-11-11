package br.com.wta.frete.marketplace.entity.enums;

/**
 * Define os tipos gerais de categoria conforme o ENUM do banco de dados
 * (marketplace.tipo_geral_enum).
 *
 * NOTA: Os valores do ENUM Java devem coincidir com os valores em PostgreSQL
 * (Ex: 'RECICLAVEL').
 */
public enum TipoGeralEnum {
    RECICLAVEL,
    ELETRONICO,
    CONSTRUCAO,
    DOACAO,
    GERAL,
    AUTOMOTIVO
}
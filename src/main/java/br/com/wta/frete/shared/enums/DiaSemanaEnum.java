// Local sugerido: br.com.wta.frete.shared.enums
package br.com.wta.frete.shared.enums;

import lombok.Getter;

/**
 * Enum que mapeia o tipo colaboradores.dia_semana_enum do PostgreSQL.
 * O nome da constante deve ser o mesmo usado no banco de dados
 * para que o JPA/Hibernate consiga mapear corretamente
 * com @Enumerated(EnumType.STRING).
 */
@Getter
public enum DiaSemanaEnum {
    DOMINGO("Domingo", 1),
    SEGUNDA("Segunda-feira", 2),
    TERCA("Terça-feira", 3),
    QUARTA("Quarta-feira", 4),
    QUINTA("Quinta-feira", 5),
    SEXTA("Sexta-feira", 6),
    SABADO("Sábado", 7);

    private final String descricao;
    private final int valorNumerico; // Mantido para compatibilidade e conversão, se necessário.

    DiaSemanaEnum(String descricao, int valorNumerico) {
        this.descricao = descricao;
        this.valorNumerico = valorNumerico;
    }
}
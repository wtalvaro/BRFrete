package br.com.wta.frete.social.entity;

import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * Define a chave primária composta para a tabela 'social.seguidores'.
 */
@Embeddable
@Data
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class SeguidorId implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

	/**
	 * Chave estrangeira para a Pessoa que segue.
	 */
	private Long seguidorId;

	/**
	 * Chave estrangeira para a Pessoa que é seguida.
	 */
	private Long seguidoId;
}
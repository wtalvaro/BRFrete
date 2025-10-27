package br.com.wta.frete.core.entity;

import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.io.Serial;
import java.io.Serializable;

/**
 * Define a chave primária composta para a tabela 'core.pessoa_perfil'.
 */
@Embeddable
@Data // Inclui getters, setters, e é crucial para JPA:
@EqualsAndHashCode // Implementa equals e hashCode para chaves compostas
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PessoaPerfilId implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

	/**
	 * Chave estrangeira para Pessoa (core.pessoas.pessoa_id).
	 */
	private Long pessoaId;

	/**
	 * Chave estrangeira para Perfil (core.perfis.perfil_id).
	 */
	private Integer perfilId;
}
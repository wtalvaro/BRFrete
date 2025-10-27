package br.com.wta.frete.core.entity;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Column;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

@Embeddable
@Data
@EqualsAndHashCode
public class ParticipanteConversaId implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

	@Column(name = "conversa_id")
	private Long conversaId;

	@Column(name = "pessoa_id")
	private Long pessoaId;
}
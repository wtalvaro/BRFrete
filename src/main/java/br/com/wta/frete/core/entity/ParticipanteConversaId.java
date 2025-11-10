package br.com.wta.frete.core.entity;

import java.io.Serial;
import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.EqualsAndHashCode;

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
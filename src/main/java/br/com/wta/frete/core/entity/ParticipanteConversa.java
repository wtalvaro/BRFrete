package br.com.wta.frete.core.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "participantes_conversa", schema = "core")
@Data
public class ParticipanteConversa {

	@EmbeddedId
	private ParticipanteConversaId id;

	// Relacionamento com Conversa
	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("conversaId") // Mapeia a FK para o campo 'conversaId' da chave composta
	@JoinColumn(name = "conversa_id", nullable = false)
	private Conversa conversa;

	// Relacionamento com Pessoa
	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("pessoaId") // Mapeia a FK para o campo 'pessoaId' da chave composta
	@JoinColumn(name = "pessoa_id", nullable = false)
	private Pessoa pessoa;

	@Column(name = "data_entrada")
	private LocalDateTime dataEntrada = LocalDateTime.now();
}
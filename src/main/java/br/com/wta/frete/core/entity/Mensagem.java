package br.com.wta.frete.core.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "mensagens", schema = "core")
@Data
public class Mensagem {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "mensagem_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "conversa_id", nullable = false)
	private Conversa conversa;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "autor_id", nullable = false)
	private Pessoa autor; // Usa a sua Entity Pessoa existente

	@Column(name = "conteudo", columnDefinition = "TEXT", nullable = false)
	private String conteudo;

	@Column(name = "data_envio")
	private LocalDateTime dataEnvio = LocalDateTime.now();

	@Column(name = "is_lida", nullable = false)
	private Boolean isLida = false;
}
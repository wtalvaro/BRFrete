package br.com.wta.frete.core.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

import br.com.wta.frete.core.entity.enums.TipoConversa;

@Entity
@Table(name = "conversas", schema = "core")
@Data
public class Conversa {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "conversa_id")
	private Long id;

	/**
	 * Tipo da conversa (PRIVADA, GRUPO, SUPORTE).
	 * 
	 * @Enumerated(EnumType.STRING) garante que o Enum seja persistido como VARCHAR.
	 */
	@Enumerated(EnumType.STRING)
	@Column(name = "tipo_conversa", length = 20, nullable = false)
	private TipoConversa tipoConversa;

	@Column(name = "data_criacao")
	private LocalDateTime dataCriacao = LocalDateTime.now();

	@Column(name = "ultima_mensagem_em")
	private LocalDateTime ultimaMensagemEm;
}
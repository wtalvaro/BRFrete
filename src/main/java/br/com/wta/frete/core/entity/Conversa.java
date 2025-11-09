package br.com.wta.frete.core.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

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
	@JdbcTypeCode(SqlTypes.NAMED_ENUM) // Mapeia o ENUM corretamente para o PostgreSQL
	@Column(name = "tipo_conversa", length = 20, nullable = false)
	private TipoConversa tipoConversa;

	@Column(name = "data_criacao")
	private LocalDateTime dataCriacao = LocalDateTime.now();

	@Column(name = "ultima_mensagem_em")
	private LocalDateTime ultimaMensagemEm;
}
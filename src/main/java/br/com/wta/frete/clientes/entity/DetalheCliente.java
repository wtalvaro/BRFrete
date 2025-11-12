package br.com.wta.frete.clientes.entity;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import br.com.wta.frete.clientes.entity.enums.TipoCliente;
import br.com.wta.frete.core.entity.Pessoa;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * Mapeia a tabela 'clientes.detalhes'. Especialização de Pessoa para clientes.
 * Relacionamento 1:1 com Pessoa via chave compartilhada (@MapsId).
 */
@Entity
@Table(name = "detalhes", schema = "clientes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetalheCliente {

	/**
	 * Chave primária (pessoa_id BIGINT). Herda a chave primária da entidade Pessoa.
	 */
	@Id
	@Column(name = "pessoa_id")
	private Long pessoaId;

	/**
	 * Relacionamento Um-para-Um com Pessoa.
	 */
	@OneToOne(fetch = FetchType.LAZY)
	@MapsId
	@JoinColumn(name = "pessoa_id", nullable = false)
	private Pessoa pessoa;

	/**
	 * Tipo de cliente (e.g., PF, PJ, Cooperativa, Governo).
	 */
	@Enumerated(EnumType.STRING)
	@JdbcTypeCode(SqlTypes.NAMED_ENUM) // Mapeia o ENUM corretamente para o PostgreSQL
	@Column(name = "tipo_cliente", nullable = false)
	private TipoCliente tipoCliente;

	/**
	 * Preferências específicas para coleta (TEXT).
	 */
	@Column(name = "preferencias_coleta", columnDefinition = "TEXT")
	private String preferenciasColeta;
}
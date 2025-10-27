package br.com.wta.frete.clientes.entity;

import br.com.wta.frete.core.entity.Pessoa;
import br.com.wta.frete.core.entity.enums.TipoCliente;
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
	 * Tipo de cliente (e.g., PF, PJ, Cooperativa) (VARCHAR(20) NOT NULL).
	 */
	@Enumerated(EnumType.STRING) // <<< NOVO: Mapeia o Enum como String
	@Column(name = "tipo_cliente", nullable = false, length = 20)
	private TipoCliente tipoCliente; // <<< NOVO: Tipo de dado alterado para Enum

	/**
	 * Preferências específicas para coleta (TEXT).
	 */
	@Column(name = "preferencias_coleta", columnDefinition = "TEXT")
	private String preferenciasColeta;
}
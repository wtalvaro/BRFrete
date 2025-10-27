package br.com.wta.frete.colaboradores.entity;

import br.com.wta.frete.core.entity.Pessoa;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * Mapeia a tabela 'colaboradores.transportadores'. Especialização de Pessoa
 * para transportadores. Relacionamento 1:1 com Pessoa via chave compartilhada
 * (@MapsId).
 */
@Entity
@Table(name = "transportadores", schema = "colaboradores")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transportador {

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
	 * Número da licença de transporte (VARCHAR(100)).
	 */
	@Column(name = "licenca_transporte", length = 100)
	private String licencaTransporte;
}
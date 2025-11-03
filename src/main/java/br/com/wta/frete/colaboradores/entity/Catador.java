package br.com.wta.frete.colaboradores.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import br.com.wta.frete.core.entity.Pessoa;

/**
 * Mapeia a tabela 'colaboradores.catadores'. Especialização de Pessoa para
 * catadores. Relacionamento 1:1 com Pessoa via chave compartilhada (@MapsId).
 */
@Entity
@Table(name = "catadores", schema = "colaboradores")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Catador {

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
	 * ID da associação/cooperativa à qual o catador pertence (INTEGER). NOTA: Este
	 * campo é uma FK lógica, mas está sem restrição explícita no SQL, então o
	 * mapeamos como um campo simples (Integer) por enquanto.
	 */
	@Column(name = "associacao_id")
	private Integer associacaoId;

	/**
	 * Descrição da área geográfica de atuação (TEXT).
	 */
	@Column(name = "area_atuacao_geografica", columnDefinition = "TEXT")
	private String areaAtuacaoGeografica;
}
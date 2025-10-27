package br.com.wta.frete.colaboradores.entity;

import br.com.wta.frete.core.entity.Pessoa;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * Mapeia a tabela 'colaboradores.sucateiros'. Especialização de Pessoa para
 * sucateiros (empresas de reciclagem/pátios). Relacionamento 1:1 com Pessoa via
 * chave compartilhada (@MapsId).
 */
@Entity
@Table(name = "sucateiros", schema = "colaboradores")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Sucateiro {

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
	 * Razão social (VARCHAR(255) NOT NULL).
	 */
	@Column(name = "razao_social", nullable = false, length = 255)
	private String razaoSocial;

	/**
	 * CNPJ Secundário ou Inscrição Estadual (VARCHAR(18)).
	 */
	@Column(name = "cnpj_secundario", length = 18)
	private String cnpjSecundario;

	/**
	 * Número da licença ambiental (VARCHAR(100)).
	 */
	@Column(name = "licenca_ambiental", length = 100)
	private String licencaAmbiental;

	/**
	 * Endereço físico do pátio de sucata (TEXT NOT NULL).
	 */
	@Column(name = "endereco_patio", nullable = false, columnDefinition = "TEXT")
	private String enderecoPatio;
}
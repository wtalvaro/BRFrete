package br.com.wta.frete.colaboradores.entity;

import br.com.wta.frete.core.entity.Pessoa;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * [cite_start]Mapeia a tabela 'colaboradores.lojistas'[cite: 8]. Especialização
 * de Pessoa para vendedores/lojistas no marketplace. [cite_start]Relacionamento
 * 1:1 com Pessoa via chave compartilhada (@MapsId)[cite: 22].
 */
@Entity
@Table(name = "lojistas", schema = "colaboradores")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Lojista {

	/**
	 * Chave primária (pessoa_id BIGINT). [cite_start]Herda a chave primária da
	 * entidade Pessoa[cite: 22].
	 */
	@Id
	@Column(name = "pessoa_id")
	private Long pessoaId;

	/**
	 * [cite_start]Relacionamento Um-para-Um com Pessoa[cite: 22].
	 */
	@OneToOne(fetch = FetchType.LAZY)
	@MapsId
	@JoinColumn(name = "pessoa_id", nullable = false)
	private Pessoa pessoa;

	/**
	 * [cite_start]Nome da loja ou fantasia (VARCHAR(255) NOT NULL)[cite: 22].
	 */
	@Column(name = "nome_loja", nullable = false, length = 255)
	private String nomeLoja;

	/**
	 * [cite_start]Endereço principal para coleta/entrega da loja (TEXT NOT
	 * NULL)[cite: 22].
	 */
	@Column(name = "endereco_coleta", nullable = false, columnDefinition = "TEXT")
	private String enderecoColeta;

	/**
	 * [cite_start]Horário de atendimento ou funcionamento (VARCHAR(100))[cite: 22].
	 */
	@Column(name = "horario_atendimento", length = 100)
	private String horarioAtendimento;
}
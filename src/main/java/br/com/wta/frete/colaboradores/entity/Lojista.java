package br.com.wta.frete.colaboradores.entity;

import br.com.wta.frete.core.entity.Pessoa;
// 💡 Importação Adicional!
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Mapeia a tabela 'colaboradores.lojistas'. Especialização de Pessoa
 * para vendedores/lojistas no marketplace. Relacionamento 1:1 com Pessoa via
 * chave compartilhada (@MapsId).
 *
 * 💡 ATUALIZAÇÃO: Campo 'horarioAtendimento' removido para usar a entidade
 * HorarioOperacao generalizada.
 */
@Entity
@Table(name = "lojistas", schema = "colaboradores")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Lojista {

	/**
	 * Chave primária (pessoa_id BIGINT). Herda a chave primária da entidade Pessoa
	 * .
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
	 * Nome da loja ou fantasia (VARCHAR(255) NOT NULL).
	 */
	@Column(name = "nome_loja", nullable = false, length = 255)
	private String nomeLoja;

	/**
	 * Endereço principal para coleta/entrega da loja (TEXT NOT NULL).
	 */
	@Column(name = "endereco_coleta", nullable = false, columnDefinition = "TEXT")
	private String enderecoColeta;

	// =========================================================================
	// 🛠️ NOVO CAMPO: Controle de Concorrência Otimista
	// =========================================================================

	/**
	 * Otimistic Locking: Campo gerenciado pelo JPA para controle de concorrência.
	 * O Hibernate incrementa esta coluna automaticamente a cada update.
	 * O nome da coluna no banco será 'versao'.
	 */
	@Version
	@Column(name = "versao")
	private Integer versao;
}
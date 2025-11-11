package br.com.wta.frete.marketplace.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime; // Importação correta para TIMESTAMP WITH TIME ZONE
import java.util.List;

// Assumindo a existência da entidade Pessoa para a chave estrangeira autor_id
import br.com.wta.frete.core.entity.Pessoa;

/**
 * Mapeia a tabela 'marketplace.perguntas_produto'. Representa uma pergunta
 * feita por um usuário sobre um Produto ou uma Resposta a uma pergunta.
 * * * ATUALIZAÇÃO: Alinhamento com o modelo de threading do SQL.
 * - Campo 'resposta' removido.
 * - Adicionado auto-relacionamento (perguntaPai e respostas) via
 * pergunta_pai_id.
 * - Campo 'textoConteudo' mapeado para 'texto_conteudo'.
 * - Campo 'dataPublicacao' (ZonedDateTime) mapeado para 'data_publicacao'.
 * - Adicionado 'isPublica'.
 */
@Entity
@Table(name = "perguntas_produto", schema = "marketplace")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PerguntaProduto {

	/**
	 * Chave primária (BIGSERIAL). Mapeado para Long.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "pergunta_id")
	private Long id;

	// --- Relacionamentos de Chave Estrangeira ---

	/**
	 * Chave estrangeira para o Produto (marketplace.produtos.produto_id).
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "produto_id", nullable = false)
	private Produto produto;

	/**
	 * Chave estrangeira para o Autor da pergunta/resposta (core.pessoas.pessoa_id).
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "autor_id", nullable = false)
	private Pessoa autor;

	// --- Relacionamento de Threading (Auto-Referência) ---

	/**
	 * Se esta for uma resposta, aponta para a Pergunta/Comentário pai
	 * (pergunta_pai_id). É NULO para perguntas principais.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "pergunta_pai_id")
	private PerguntaProduto perguntaPai;

	/**
	 * Lista de Respostas/Comentários a esta Pergunta.
	 */
	@OneToMany(mappedBy = "perguntaPai", cascade = CascadeType.ALL, orphanRemoval = true)
	@OrderBy("dataPublicacao ASC") // Ordem cronológica das respostas
	private List<PerguntaProduto> respostas;

	// --- Dados ---

	/**
	 * Conteúdo da pergunta ou da resposta (texto_conteudo TEXT NOT NULL).
	 * Antigamente 'textoPergunta' na sua Entity.
	 */
	@Column(name = "texto_conteudo", nullable = false, columnDefinition = "TEXT")
	private String textoConteudo;

	/**
	 * Data e hora da publicação (data_publicacao TIMESTAMP WITH TIME ZONE).
	 */
	@Column(name = "data_publicacao", columnDefinition = "TIMESTAMP WITH TIME ZONE")
	private ZonedDateTime dataPublicacao = ZonedDateTime.now();

	/**
	 * Status de visibilidade (is_publica BOOLEAN NOT NULL DEFAULT true).
	 */
	@Column(name = "is_publica", nullable = false)
	private Boolean isPublica = true;

	// O campo 'resposta' e 'dataPergunta' foram removidos para aderir ao modelo de
	// threading.
}
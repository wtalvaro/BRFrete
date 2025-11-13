package br.com.wta.frete.social.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.ZonedDateTime;
import java.util.List;

import br.com.wta.frete.core.entity.Pessoa;
import br.com.wta.frete.marketplace.entity.Produto;

/**
 * Mapeia a tabela 'social.comentarios'. Permite comentários em um Produto e
 * auto-referência para respostas.
 */
@Entity
@Table(name = "comentarios", schema = "social")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comentario {

	/**
	 * Chave primária (BIGSERIAL). Mapeado para Long.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "comentario_id")
	private Long id;

	// --- Relacionamentos (Chaves Estrangeiras) ---

	/**
	 * Autor do comentário (autor_id BIGINT NOT NULL). Mapeado para a entidade
	 * Pessoa.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "autor_id", nullable = false)
	private Pessoa autor;

	/**
	 * Produto que está sendo comentado (produto_id INTEGER NOT NULL). Mapeado para
	 * a entidade Produto.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "produto_id", nullable = false)
	private Produto produto;

	// --- Auto-Referência (Respostas) ---

	/**
	 * Comentário pai (comentario_pai_id BIGINT). Pode ser NULL. Mapeamento
	 * Many-to-One para o próprio Comentario. Se for NULL, é um comentário
	 * principal. Se não for, é uma resposta.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "comentario_pai_id", nullable = true)
	private Comentario comentarioPai;

	/**
	 * Lista de respostas a este comentário. Mapeamento One-to-Many. 'mappedBy =
	 * "comentarioPai"' indica que a coluna de chave estrangeira está na outra ponta
	 * (neste Comentario), no campo 'comentarioPai'.
	 */
	@OneToMany(mappedBy = "comentarioPai", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Comentario> respostas;

	// --- Dados do Comentário ---

	/**
	 * O conteúdo do comentário (TEXT NOT NULL).
	 * **CORREÇÃO: Mapeado para a coluna 'texto_conteudo' do SQL.**
	 */
	@Column(name = "texto_conteudo", nullable = false, columnDefinition = "TEXT") // <--- AJUSTADO
	private String textoComentario;

	/**
	 * Data e hora do comentário (TIMESTAMP WITH TIME ZONE DEFAULT
	 * CURRENT_TIMESTAMP).
	 */
	@Column(name = "data_comentario", columnDefinition = "TIMESTAMP WITH TIME ZONE")
	private ZonedDateTime dataComentario = ZonedDateTime.now();
}
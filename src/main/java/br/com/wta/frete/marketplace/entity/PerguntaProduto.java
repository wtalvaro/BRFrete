package br.com.wta.frete.marketplace.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import br.com.wta.frete.core.entity.Pessoa;

/**
 * Mapeia a tabela 'marketplace.perguntas_produto'. Representa uma pergunta
 * feita por um usuário sobre um Produto específico.
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

	// Relacionamentos (Foreign Keys)

	/**
	 * Chave estrangeira para o Produto (marketplace.produtos.produto_id).
	 * Relacionamento Many-to-One (Muitas perguntas para um produto).
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "produto_id", nullable = false)
	private Produto produto;

	/**
	 * Chave estrangeira para o Autor da pergunta (core.pessoas.pessoa_id).
	 * Relacionamento Many-to-One (Muitas perguntas podem ter o mesmo autor).
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "autor_id", nullable = false)
	private Pessoa autor;

	// Campos de Dados

	/**
	 * Conteúdo da pergunta (TEXT NOT NULL).
	 */
	@Column(name = "texto_pergunta", nullable = false, columnDefinition = "TEXT")
	private String textoPergunta;

	/**
	 * Resposta do Lojista/Dono do Produto (TEXT). Pode ser nulo.
	 */
	@Column(name = "resposta", columnDefinition = "TEXT")
	private String resposta;

	/**
	 * Data e hora da pergunta (TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP).
	 */
	@Column(name = "data_pergunta", nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
	private LocalDateTime dataPergunta = LocalDateTime.now();

	/**
	 * Data e hora da resposta (TIMESTAMP WITH TIME ZONE). Pode ser nulo.
	 */
	@Column(name = "data_resposta", columnDefinition = "TIMESTAMP WITH TIME ZONE")
	private LocalDateTime dataResposta;
}
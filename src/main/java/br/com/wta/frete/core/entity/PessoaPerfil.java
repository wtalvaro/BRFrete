package br.com.wta.frete.core.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Mapeia a tabela de associação 'core.pessoa_perfil'. Chave primária composta
 * (pessoa_id, perfil_id).
 */
@Entity
@Table(name = "pessoa_perfil", schema = "core")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PessoaPerfil {

	/**
	 * Chave composta incorporada.
	 */
	@EmbeddedId
	private PessoaPerfilId id;

	/**
	 * Relacionamento Many-to-One para Pessoa. @MapsId("pessoaId") mapeia este
	 * relacionamento para o campo 'pessoaId' na chave composta.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("pessoaId")
	@JoinColumn(name = "pessoa_id", nullable = false)
	private Pessoa pessoa;

	/**
	 * Relacionamento Many-to-One para Perfil. @MapsId("perfilId") mapeia este
	 * relacionamento para o campo 'perfilId' na chave composta.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("perfilId")
	@JoinColumn(name = "perfil_id", nullable = false)
	private Perfil perfil;

	// NOVO: Construtor de Conveniência para inicializar a chave composta
	/**
	 * Documentação: Construtor que inicializa a chave composta (this.id)
	 * antes do Hibernate tentar preencher seus campos via @MapsId.
	 * 
	 * @param pessoa A entidade Pessoa.
	 * @param perfil A entidade Perfil.
	 */
	public PessoaPerfil(Pessoa pessoa, Perfil perfil) {
		// Inicializa o ID composto ANTES de ser gerenciado pelo Hibernate
		this.id = new PessoaPerfilId();
		this.pessoa = pessoa;
		this.perfil = perfil;
	}
}
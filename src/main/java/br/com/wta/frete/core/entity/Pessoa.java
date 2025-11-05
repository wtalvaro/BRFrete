package br.com.wta.frete.core.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Entidade JPA: Pessoa Mapeia a tabela 'core.pessoas' e representa a identidade
 * base de um usuário. Estrutura Simplificada: Remove os campos de token
 * temporário, delegando a expiração ao Redis.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "pessoas", schema = "core") // Mapeia para core.pessoas
public class Pessoa {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "pessoa_id")
	private Long id;

	@Column(name = "nome", nullable = false)
	private String nome;

	@Column(name = "documento", unique = true, nullable = true, length = 18)
	private String documento;

	// NOVO CAMPO para IDs de Provedores Sociais
	@Column(name = "social_id", length = 255, unique = true)
	private String socialId; // Este campo armazenará o ID do Google (sub)

	@Column(name = "email", unique = true, nullable = false, length = 100)
	private String email;

	@Column(name = "telefone", length = 20)
	private String telefone;

	@Column(name = "senha", nullable = false)
	private String senha;

	/**
	 * Data de nascimento (DATE).
	 */
	@Column(name = "data_nascimento")
	private LocalDate dataNascimento;

	// Data de cadastro, default now() no SQL
	@Column(name = "data_cadastro", columnDefinition = "TIMESTAMP WITHOUT TIME ZONE")
	private LocalDateTime dataCadastro;

	// Flag de Ativação: O campo que é alterado pelo processo de validação via Redis
	@Column(name = "ativo", nullable = false)
	private boolean ativo;

	// ATENÇÃO: Os campos 'token_ativacao' e 'expiracao_token_ativacao' foram
	// removidos aqui,
	// pois a expiração é agora gerida exclusivamente pelo Redis.

	@Column(name = "is_colaborador", nullable = false)
	private boolean isColaborador;

	@Column(name = "is_cliente", nullable = false)
	private boolean isCliente;

	// Relação M:M com Perfis (Mapeamento padrão)
	@ToString.Exclude
	@OneToMany(mappedBy = "pessoa", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Set<PessoaPerfil> perfis = new HashSet<>();

	// Construtor usado no Mapeamento (Exemplo)
	public Pessoa(String nome, String documento, String email, String senha, String telefone) {
		this.nome = nome;
		this.documento = documento;
		this.email = email;
		this.senha = senha;
		this.telefone = telefone;
		this.ativo = false; // Garante que a entidade começa como inativa
		this.dataCadastro = LocalDateTime.now();
		this.isColaborador = false;
		this.isCliente = true; // ALTERADO: Agora, o padrão é ser cliente.;
	}

	/**
	 * Método auxiliar para adicionar um perfil à coleção.
	 * 
	 * @param perfil O perfil a ser adicionado.
	 */
	public void adicionarPerfil(Perfil perfil) {
		PessoaPerfil pessoaPerfil = new PessoaPerfil(this, perfil);
		this.perfis.add(pessoaPerfil);
	}
}
package br.com.wta.frete.core.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

import br.com.wta.frete.core.entity.enums.StatusKYC;

/**
 * Mapeia a tabela 'core.contas_digitais'. Representa a conta digital/wallet de
 * um usuário. Relacionamento 1:1 com Pessoa, usando a chave estrangeira como
 * chave primária (@MapsId).
 */
@Entity
@Table(name = "contas_digitais", schema = "core")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContaDigital {

	/**
	 * Chave primária (pessoa_id BIGINT). Usa o mesmo valor da chave primária da
	 * entidade Pessoa (@MapsId).
	 */
	@Id
	@Column(name = "pessoa_id")
	private Long pessoaId; // Representa a chave primária e estrangeira

	/**
	 * Relacionamento Um-para-Um com Pessoa.
	 * 
	 * @MapsId: Indica que o valor da chave primária desta entidade será obtido da
	 *          entidade relacionada (Pessoa).
	 * @JoinColumn(name = "pessoa_id"): Especifica a coluna FK.
	 */
	@OneToOne
	@MapsId // Mapeia a chave primária (pessoa_id) para a PK de Pessoa
	@JoinColumn(name = "pessoa_id", nullable = false)
	private Pessoa pessoa;

	/**
	 * Identificador único da conta (VARCHAR(64) UNIQUE NOT NULL).
	 */
	@Column(name = "conta_uuid", nullable = false, length = 64, unique = true)
	private String contaUuid;

	/**
	 * Status de verificação do cliente (Know Your Customer - KYC) (VARCHAR(20) NOT
	 * NULL DEFAULT 'PENDENTE').
	 */
	@Enumerated(EnumType.STRING) // <<< NOVO: Mapeia o Enum como String
	@Column(name = "status_kyc", nullable = false, length = 20)
	private StatusKYC statusKyc = StatusKYC.PENDENTE; // <<< NOVO: Tipo de dado e default alterados

	/**
	 * Data de abertura da conta (TIMESTAMP WITHOUT TIME ZONE DEFAULT now()).
	 */
	@Column(name = "data_abertura", columnDefinition = "TIMESTAMP WITHOUT TIME ZONE")
	private LocalDateTime dataAbertura = LocalDateTime.now();
}
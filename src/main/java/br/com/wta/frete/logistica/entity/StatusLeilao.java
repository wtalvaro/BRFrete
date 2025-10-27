package br.com.wta.frete.logistica.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * Mapeia a tabela 'logistica.status_leilao'. Define os possíveis status de um
 * processo de leilão de frete.
 */
@Entity
@Table(name = "status_leilao", schema = "logistica", uniqueConstraints = {
		@UniqueConstraint(columnNames = "nome_status") })
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatusLeilao {

	/**
	 * Chave primária (SERIAL). Mapeado para Integer.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "status_id")
	private Integer id;

	/**
	 * Nome único do status (VARCHAR(50) UNIQUE NOT NULL).
	 */
	@Column(name = "nome_status", nullable = false, length = 50)
	private String nomeStatus;
}
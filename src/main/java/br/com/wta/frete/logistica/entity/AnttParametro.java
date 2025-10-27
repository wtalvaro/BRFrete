package br.com.wta.frete.logistica.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Mapeia a tabela 'logistica.antt_parametros'. Armazena parâmetros de
 * regulamentação (como ANTT - Agência Nacional de Transportes Terrestres).
 */
@Entity
@Table(name = "antt_parametros", schema = "logistica", uniqueConstraints = { @UniqueConstraint(columnNames = "chave") })
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnttParametro {

	/**
	 * Chave primária (SERIAL). Mapeado para Integer.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "parametro_id")
	private Integer id;

	/**
	 * Chave única para o parâmetro (VARCHAR(100) UNIQUE NOT NULL).
	 */
	@Column(name = "chave", nullable = false, length = 100)
	private String chave;

	/**
	 * Valor numérico do parâmetro (NUMERIC(10, 4) NOT NULL).
	 */
	@Column(name = "valor", nullable = false, precision = 10, scale = 4)
	private BigDecimal valor;

	/**
	 * Descrição do que o parâmetro representa (TEXT).
	 */
	@Column(name = "descricao", columnDefinition = "TEXT")
	private String descricao;

	/**
	 * Data de início da vigência do parâmetro (DATE DEFAULT now()).
	 */
	@Column(name = "data_vigencia", columnDefinition = "DATE")
	private LocalDate dataVigencia = LocalDate.now();
}
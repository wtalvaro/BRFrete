// Caminho: src/main/java/br/com/wta/frete/logistica/entity/Lance.java
package br.com.wta.frete.logistica.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import br.com.wta.frete.colaboradores.entity.Transportador;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Mapeia a tabela 'logistica.lances'. Registra a proposta de preço
 * de um Transportador para um Frete (Leilão Reverso).
 */
@Entity
@Table(name = "lances", schema = "logistica")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Lance {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "lance_id")
	private Long id;

	/**
	 * Referência ao Frete que está sendo leiloado.
	 * CORREÇÃO: Coluna ajustada para 'frete_id'.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "frete_id", nullable = false)
	private Frete frete;

	/**
	 * Referência ao Transportador que submeteu o lance.
	 * CORREÇÃO: Coluna ajustada para 'transportador_id'.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "transportador_id", nullable = false)
	private Transportador transportador;

	/**
	 * Valor monetário proposto pelo transportador (NUMERIC(10, 2)).
	 * CORREÇÃO: Campo e coluna ajustados para 'valor_lance'.
	 */
	@Column(name = "valor_lance", precision = 10, scale = 2, nullable = false)
	private BigDecimal valorLance;

	/**
	 * Data e hora da submissão/atualização do lance.
	 */
	@Column(name = "data_lance")
	private LocalDateTime dataLance;

	/**
	 * Indica se este lance foi o vencedor do leilão (is_vencedor BOOLEAN).
	 */
	@Column(name = "is_vencedor")
	private boolean vencedor;

	/**
	 * Motivo do cancelamento (TEXT). NOVO CAMPO
	 */
	@Column(name = "motivo_cancelamento", columnDefinition = "TEXT")
	private String motivoCancelamento;
}
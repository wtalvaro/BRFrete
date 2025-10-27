package br.com.wta.frete.colaboradores.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;

import br.com.wta.frete.colaboradores.entity.enums.StatusVeiculo;
import br.com.wta.frete.colaboradores.entity.enums.TipoVeiculo;

/**
 * Mapeia a tabela 'colaboradores.veiculos'. Representa os veículos utilizados
 * pelos Transportadores.
 */
@Entity
@Table(name = "veiculos", schema = "colaboradores", uniqueConstraints = {
		@UniqueConstraint(columnNames = "matricula") })
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Veiculo {

	/**
	 * Chave primária (SERIAL). Mapeado para Integer.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "veiculo_id")
	private Integer id;

	/**
	 * Chave estrangeira para o Transportador (transportador_pessoa_id BIGINT NOT
	 * NULL). Relacionamento Many-to-One: Muitos Veículos para Um Transportador.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "transportador_pessoa_id", nullable = false)
	private Transportador transportador; // Mapeia para a entidade Transportador

	/**
	 * Placa ou matrícula do veículo (VARCHAR(20) UNIQUE NOT NULL).
	 */
	@Column(name = "matricula", nullable = false, length = 20)
	private String matricula;

	/**
	 * Tipo do veículo (e.g., caminhão baú, caçamba, van) (VARCHAR(50) NOT NULL).
	 */
	@Enumerated(EnumType.STRING) // <<< NOVO: Mapeia o Enum como String
	@Column(name = "tipo_veiculo", nullable = false, length = 50)
	private TipoVeiculo tipoVeiculo; // <<< NOVO: Tipo de dado alterado

	/**
	 * Capacidade máxima de peso em KG (NUMERIC(10, 2) NOT NULL).
	 */
	@Column(name = "capacidade_peso_kg", nullable = false, precision = 10, scale = 2)
	private BigDecimal capacidadePesoKg;

	/**
	 * Capacidade máxima de volume em M3 (NUMERIC(10, 2)).
	 */
	@Column(name = "capacidade_volume_m3", precision = 10, scale = 2)
	private BigDecimal capacidadeVolumeM3;

	/**
	 * Status de disponibilidade (VARCHAR(20) NOT NULL DEFAULT 'DISPONIVEL').
	 */
	@Enumerated(EnumType.STRING) // <<< NOVO: Mapeia o Enum como String
	@Column(name = "status_veiculo", nullable = false, length = 20)
	private StatusVeiculo statusVeiculo = StatusVeiculo.DISPONIVEL; // <<< NOVO: Tipo de dado e default alterados
}
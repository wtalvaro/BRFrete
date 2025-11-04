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
 * CORREÇÃO: Entidade atualizada para cobrir todos os campos da definição SQL.
 */
@Entity
@Table(name = "veiculos", schema = "colaboradores", uniqueConstraints = {
		@UniqueConstraint(columnNames = "placa"), // CORREÇÃO 1: Restrição de unicidade para 'placa'
		@UniqueConstraint(columnNames = "renavam") // NOVO CAMPO: Adicionado o renavam à unicidade
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Veiculo {

	/**
	 * Chave primária (BIGSERIAL). Mapeado para Integer.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "veiculo_id")
	private Integer id;

	/**
	 * Chave estrangeira para o Transportador (transportador_id BIGINT NOT
	 * NULL). Relacionamento Many-to-One.
	 * CORREÇÃO: O nome da JoinColumn foi ajustado para 'transportador_id' (da
	 * tabela SQL).
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "transportador_id", nullable = false)
	private Transportador transportador;

	/**
	 * Placa do veículo (VARCHAR(10) UNIQUE NOT NULL).
	 * CORREÇÃO: O nome do campo foi ajustado para 'placa' e o length para 10.
	 */
	@Column(name = "placa", nullable = false, length = 10)
	private String placa; // CORREÇÃO: Renomeado de 'matricula' para 'placa'

	/**
	 * NOVO CAMPO: Renavam do veículo (VARCHAR(11) UNIQUE NOT NULL).
	 */
	@Column(name = "renavam", nullable = false, length = 11)
	private String renavam;

	/**
	 * Tipo do veículo (colaboradores.tipo_veiculo_enum NOT NULL).
	 */
	@Enumerated(EnumType.STRING)
	@Column(name = "tipo_veiculo", nullable = false, length = 50)
	private TipoVeiculo tipoVeiculo;

	/**
	 * NOVO CAMPO: Ano de fabricação (INTEGER).
	 */
	@Column(name = "ano_fabricacao")
	private Integer anoFabricacao;

	/**
	 * Capacidade máxima de peso em KG (NUMERIC(10, 2)).
	 * CORREÇÃO: O nome da coluna foi ajustado para 'capacidade_kg' (da tabela SQL).
	 */
	@Column(name = "capacidade_kg", nullable = false, precision = 10, scale = 2)
	private BigDecimal capacidadeKg; // CORREÇÃO: Renomeado de 'capacidadePesoKg' para 'capacidadeKg'

	/**
	 * Capacidade máxima de volume em M3 (NUMERIC(10, 2)).
	 * CORREÇÃO: O nome da coluna foi ajustado para 'capacidade_m3' (da tabela SQL).
	 */
	@Column(name = "capacidade_m3", precision = 10, scale = 2)
	private BigDecimal capacidadeM3; // CORREÇÃO: Renomeado de 'capacidadeVolumeM3' para 'capacidadeM3'

	/**
	 * NOVO CAMPO: Indica se o veículo possui rastreador (BOOLEAN DEFAULT FALSE).
	 */
	@Column(name = "possui_rastreador", nullable = false)
	private boolean possuiRastreador = false; // Valor default no Java

	/**
	 * Status de disponibilidade (colaboradores.status_veiculo_enum NOT NULL DEFAULT
	 * 'DISPONIVEL').
	 */
	@Enumerated(EnumType.STRING)
	@Column(name = "status_veiculo", nullable = false, length = 20)
	private StatusVeiculo statusVeiculo = StatusVeiculo.DISPONIVEL;
}
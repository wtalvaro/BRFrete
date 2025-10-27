package br.com.wta.frete.logistica.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

/**
 * Mapeia a tabela 'logistica.cotacoes_materiais'. Armazena as cotações de preço
 * de materiais de sucata/reciclagem no mercado.
 */
@Entity
@Table(name = "cotacoes_materiais", schema = "logistica", uniqueConstraints = {
		@UniqueConstraint(columnNames = "material_nome") })
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CotacaoMaterial {

	/**
	 * Chave primária (SERIAL). Mapeado para Integer.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "cotacao_id")
	private Integer id;

	/**
	 * Nome único do material (VARCHAR(100) UNIQUE NOT NULL).
	 */
	@Column(name = "material_nome", nullable = false, length = 100)
	private String materialNome;

	/**
	 * Preço médio por quilograma (NUMERIC(10, 2) NOT NULL).
	 */
	@Column(name = "preco_medio_kg", nullable = false, precision = 10, scale = 2)
	private BigDecimal precoMedioKg;

	/**
	 * Unidade de medida (VARCHAR(10) NOT NULL DEFAULT 'KG').
	 */
	@Column(name = "unidade_medida", nullable = false, length = 10)
	private String unidadeMedida = "KG";

	/**
	 * Data e hora da última atualização do preço (TIMESTAMP WITH TIME ZONE DEFAULT
	 * CURRENT_TIMESTAMP).
	 */
	@Column(name = "data_atualizacao", columnDefinition = "TIMESTAMP WITH TIME ZONE")
	private ZonedDateTime dataAtualizacao = ZonedDateTime.now();
}
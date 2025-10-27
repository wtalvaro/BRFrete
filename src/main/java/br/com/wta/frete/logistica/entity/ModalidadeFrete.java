package br.com.wta.frete.logistica.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * [cite_start]Mapeia a tabela 'logistica.modalidades_frete'. [cite: 27]
 * [cite_start]Define os tipos de modalidade de frete disponíveis (e.g.,
 * Rodoviário, Ferroviário). [cite: 27]
 */
@Entity
@Table(name = "modalidades_frete", schema = "logistica", uniqueConstraints = {
		@UniqueConstraint(columnNames = "nome_modalidade") })
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ModalidadeFrete {

	/**
	 * Chave primária (SERIAL). [cite_start]Mapeado para Integer. [cite: 27]
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "modalidade_id")
	private Integer id;

	/**
	 * [cite_start]Nome único da modalidade (VARCHAR(50) UNIQUE NOT NULL). [cite:
	 * 27]
	 */
	@Column(name = "nome_modalidade", nullable = false, length = 50)
	private String nomeModalidade;
}
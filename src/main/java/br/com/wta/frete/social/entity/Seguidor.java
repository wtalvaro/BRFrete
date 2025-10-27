package br.com.wta.frete.social.entity;

import br.com.wta.frete.core.entity.Pessoa;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * Mapeia a tabela de associação 'social.seguidores'. Relacionamento
 * Muitos-para-Muitos entre duas Pessoas (Seguidor e Seguido).
 */
@Entity
@Table(name = "seguidores", schema = "social")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Seguidor {

	/**
	 * Chave composta incorporada.
	 */
	@EmbeddedId
	private SeguidorId id;

	/**
	 * Relacionamento Many-to-One para a Pessoa que está seguindo (seguidor_id).
	 * 
	 * @MapsId mapeia o campo 'seguidorId' da chave composta para esta FK.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("seguidorId")
	@JoinColumn(name = "seguidor_id", nullable = false)
	private Pessoa seguidor;

	/**
	 * Relacionamento Many-to-One para a Pessoa que está sendo seguida (seguido_id).
	 * 
	 * @MapsId mapeia o campo 'seguidoId' da chave composta para esta FK.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("seguidoId")
	@JoinColumn(name = "seguido_id", nullable = false)
	private Pessoa seguido;
}
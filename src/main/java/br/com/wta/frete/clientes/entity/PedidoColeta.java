package br.com.wta.frete.clientes.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.ZonedDateTime;

/**
 * Mapeia a tabela 'clientes.pedidos_coleta'[cite: 26]. Registra a solicitação
 * inicial de coleta feita por um cliente.
 */
@Entity
@Table(name = "pedidos_coleta", schema = "clientes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PedidoColeta {

	/**
	 * Chave primária (SERIAL). Mapeado para Integer[cite: 26].
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "pedido_id")
	private Integer id;

	/**
	 * Chave estrangeira para o Cliente (cliente_id BIGINT NOT NULL)[cite: 26].
	 * Relacionamento Many-to-One: Muitos Pedidos para Um Cliente[cite: 26].
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cliente_id", nullable = false)
	private DetalheCliente cliente;

	/**
	 * Descrição detalhada do pedido (TEXT NOT NULL)[cite: 26].
	 */
	@Column(name = "descricao_pedido", nullable = false, columnDefinition = "TEXT")
	private String descricaoPedido;

	/**
	 * Data e hora da solicitação (TIMESTAMP WITH TIME ZONE DEFAULT
	 * CURRENT_TIMESTAMP)[cite: 26]. Usamos ZonedDateTime para mapear TIMESTAMP WITH
	 * TIME ZONE.
	 */
	@Column(name = "data_solicitacao", columnDefinition = "TIMESTAMP WITH TIME ZONE")
	private ZonedDateTime dataSolicitacao = ZonedDateTime.now();
}
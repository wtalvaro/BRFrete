package br.com.wta.frete.logistica.entity;

import br.com.wta.frete.clientes.entity.DetalheCliente;
import br.com.wta.frete.colaboradores.entity.Transportador;
import br.com.wta.frete.core.entity.enums.StatusServico;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.ZonedDateTime;
import java.math.BigDecimal;

/**
 * Mapeia a tabela 'logistica.ordens_servico'. Entidade central para rastrear a
 * execução de um serviço de frete/logística.
 */
@Entity
@Table(name = "ordens_servico", schema = "logistica")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrdemServico {

	/**
	 * Chave primária (BIGSERIAL). Mapeado para Long.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ordem_id")
	private Long id;

	// --- Relacionamentos (Chaves Estrangeiras) ---

	/**
	 * Cliente que solicitou a Ordem de Serviço (cliente_pessoa_id BIGINT NOT NULL).
	 * Relacionamento Many-to-One com DetalheCliente.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cliente_pessoa_id", nullable = false)
	private DetalheCliente cliente;

	/**
	 * Transportador responsável por executar o serviço (transportador_pessoa_id
	 * BIGINT). Pode ser nulo no início (NULL). Relacionamento Many-to-One com
	 * Transportador.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "transportador_pessoa_id")
	private Transportador transportador;

	// --- Dados do Serviço ---

	/**
	 * Local de origem da coleta (TEXT NOT NULL).
	 */
	@Column(name = "local_origem", nullable = false, columnDefinition = "TEXT")
	private String localOrigem;

	/**
	 * Local de destino da entrega (TEXT NOT NULL).
	 */
	@Column(name = "local_destino", nullable = false, columnDefinition = "TEXT")
	private String localDestino;

	/**
	 * Distância calculada para o frete (NUMERIC(10, 2)).
	 */
	@Column(name = "distancia_km", precision = 10, scale = 2)
	private BigDecimal distanciaKm;

	/**
	 * Data e hora limite para a coleta (TIMESTAMP WITH TIME ZONE).
	 */
	@Column(name = "prazo_coleta", columnDefinition = "TIMESTAMP WITH TIME ZONE")
	private ZonedDateTime prazoColeta;

	/**
	 * Data e hora da criação da ordem de serviço (TIMESTAMP WITH TIME ZONE DEFAULT
	 * CURRENT_TIMESTAMP).
	 */
	@Column(name = "data_criacao", columnDefinition = "TIMESTAMP WITH TIME ZONE")
	private ZonedDateTime dataCriacao = ZonedDateTime.now();

	/**
	 * Status da Ordem de Serviço (logistica.status_servico ENUM NOT NULL DEFAULT
	 * 'PENDENTE'). Mapeado para o Enum Java.
	 * 
	 * @Enumerated(EnumType.STRING) garante que o valor seja armazenado como string
	 *                              no DB.
	 */
	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false, length = 20)
	private StatusServico status = StatusServico.PENDENTE;
}
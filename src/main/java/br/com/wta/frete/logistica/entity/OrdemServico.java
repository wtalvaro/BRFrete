package br.com.wta.frete.logistica.entity;

import java.time.LocalDate;
import java.time.ZonedDateTime;

import br.com.wta.frete.clientes.entity.DetalheCliente;
import br.com.wta.frete.colaboradores.entity.Transportador;
import br.com.wta.frete.core.entity.enums.StatusServico;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Mapeia a tabela 'logistica.ordens_servico'. Entidade central para rastrear a
 * execução de um serviço de frete/logística.
 * Mapeamento alinhado com o script SQL V1__Initial_Schema.sql.
 */
@Entity
@Table(name = "ordens_servico", schema = "logistica")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrdemServico {

	/**
	 * Chave primária (ordem_id BIGSERIAL).
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ordem_id")
	private Long id;

	// --- Relacionamentos (Chaves Estrangeiras) ---

	/**
	 * Cliente que solicitou a Ordem de Serviço (cliente_solicitante_id BIGINT NOT
	 * NULL).
	 * CORREÇÃO: Nome da FK e do campo ajustados para o SQL.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cliente_solicitante_id", nullable = false)
	private DetalheCliente clienteSolicitante; // Nome de campo mais claro

	/**
	 * Transportador responsável por executar o serviço (transportador_designado_id
	 * BIGINT).
	 * CORREÇÃO: Nome da FK e do campo ajustados para o SQL.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "transportador_designado_id")
	private Transportador transportadorDesignado; // Nome de campo mais claro

	// --- Dados do Serviço ---

	/**
	 * Data e hora da criação da ordem de serviço (data_solicitacao TIMESTAMP
	 * WITHOUT TIME ZONE).
	 */
	@Column(name = "data_solicitacao", columnDefinition = "TIMESTAMP WITHOUT TIME ZONE")
	private ZonedDateTime dataSolicitacao = ZonedDateTime.now();

	/**
	 * Data prevista para a coleta (data_prevista_coleta DATE). Usando LocalDate
	 * para mapear SQL DATE.
	 */
	@Column(name = "data_prevista_coleta")
	private LocalDate dataPrevistaColeta;

	/**
	 * Endereço completo de coleta (endereco_coleta TEXT NOT NULL).
	 */
	@Column(name = "endereco_coleta", nullable = false, columnDefinition = "TEXT")
	private String enderecoColeta;

	/**
	 * CEP do local de origem da coleta (cep_coleta VARCHAR(8) NOT NULL).
	 * CORREÇÃO: Campo adicionado.
	 */
	@Column(name = "cep_coleta", nullable = false, length = 8)
	private String cepColeta;

	/**
	 * CEP do local de destino da entrega (cep_destino VARCHAR(8) NOT NULL).
	 * CORREÇÃO: Campo adicionado.
	 */
	@Column(name = "cep_destino", nullable = false, length = 8)
	private String cepDestino;

	/**
	 * Status da Ordem de Serviço (status logistica.status_servico ENUM NOT NULL
	 * DEFAULT 'PENDENTE').
	 */
	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false, length = 20)
	private StatusServico status = StatusServico.PENDENTE;
}
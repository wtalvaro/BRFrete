package br.com.wta.frete.logistica.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime; // Alterado para LocalDateTime para compatibilidade com FreteService
import java.time.ZonedDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Mapeia a tabela 'logistica.fretes'. Representa o leilão ou a proposta de
 * frete para uma Ordem de Serviço.
 */
@Entity
@Table(name = "fretes", schema = "logistica")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Frete {

	/**
	 * Chave primária (ordem_servico_id BIGINT). Relacionamento 1:1 com
	 * OrdemServico, usando a chave compartilhada (@MapsId).
	 */
	@Id
	@Column(name = "ordem_servico_id")
	private Long ordemServicoId;

	/**
	 * Relacionamento Um-para-Um com OrdemServico.
	 */
	@OneToOne(fetch = FetchType.LAZY)
	@MapsId // Mapeia a chave primária (ordem_servico_id) para a PK de OrdemServico
	@JoinColumn(name = "ordem_servico_id", nullable = false)
	private OrdemServico ordemServico;

	// --- Relacionamentos (Chaves Estrangeiras) ---

	/**
	 * Modalidade de frete selecionada (modalidade_id INTEGER NOT NULL).
	 * Relacionamento Many-to-One com ModalidadeFrete.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "modalidade_id", nullable = false)
	private ModalidadeFrete modalidade;

	/**
	 * Status atual do leilão ou cotação (status_leilao_id INTEGER NOT NULL).
	 * Relacionamento Many-to-One com StatusLeilao.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "status_leilao_id", nullable = false)
	private StatusLeilao statusLeilao;

	// --- Detalhes do Frete ---

	/**
	 * Prazo para o encerramento do leilão/propostas (TIMESTAMP WITH TIME ZONE NOT
	 * NULL).
	 */
	@Column(name = "prazo_encerramento", nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
	private ZonedDateTime prazoEncerramento;

	/**
	 * Valor inicial sugerido pelo cliente (NUMERIC(10, 2)).
	 */
	@Column(name = "valor_inicial_proposto", precision = 10, scale = 2)
	private BigDecimal valorInicialProposto;

	/**
	 * Valor final do frete aceito (NUMERIC(10, 2)). Será preenchido após a
	 * aceitação de um lance.
	 */
	@Column(name = "valor_final_aceito", precision = 10, scale = 2)
	private BigDecimal valorFinalAceito;

	/**
	 * Informação adicional sobre o tipo de embalagem, se aplicável (VARCHAR(50)).
	 */
	@Column(name = "tipo_embalagem", length = 50)
	private String tipoEmbalagem;

	// --- NOVOS CAMPOS ADICIONADOS (Correção para FreteService) ---

	/**
	 * Distância rodoviária em Km (NUMERIC(8, 2)).
	 */
	@Column(name = "distancia_km", precision = 8, scale = 2)
	private BigDecimal distanciaKm;

	/**
	 * Piso mínimo de frete sugerido pela ANTT (NUMERIC(10, 2)).
	 */
	@Column(name = "antt_piso_minimo", precision = 10, scale = 2)
	private BigDecimal anttPisoMinimo;

	/**
	 * Preço sugerido de mercado para o frete (NUMERIC(10, 2)).
	 */
	@Column(name = "preco_sugerido", precision = 10, scale = 2)
	private BigDecimal precoSugerido;

	/**
	 * Custo base de mercado calculado (NUMERIC(10, 2)).
	 */
	@Column(name = "custo_base_mercado", precision = 10, scale = 2)
	private BigDecimal custoBaseMercado;

	/**
	 * Data e hora de expiração da negociação/leilão (TIMESTAMP WITH TIME ZONE).
	 */
	@Column(name = "data_expiracao_negociacao", columnDefinition = "TIMESTAMP WITH TIME ZONE")
	private LocalDateTime dataExpiracaoNegociacao; // Usando LocalDateTime para compatibilidade com o setter no
													// FreteService
}
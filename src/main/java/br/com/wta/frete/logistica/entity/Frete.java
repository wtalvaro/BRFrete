package br.com.wta.frete.logistica.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import br.com.wta.frete.colaboradores.entity.Transportador; // NOVO IMPORT
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
import lombok.Builder; // Adicionado para uso no Service
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Mapeia a tabela 'logistica.fretes'. Representa o leilão ou a proposta de
 * frete. A chave primária agora é gerada automaticamente (frete_id).
 */
@Entity
@Table(name = "fretes", schema = "logistica")
@Data
@Builder // Útil para criar instâncias no FreteService
@NoArgsConstructor
@AllArgsConstructor
public class Frete {

	/**
	 * Chave primária autoincrementada (frete_id SERIAL PRIMARY KEY).
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "frete_id")
	private Long freteId;

	// --- RELACIONAMENTOS (Foreign Keys) ---

	/**
	 * Ordem de Serviço à qual este frete está ligado (ordem_servico_id BIGINT NOT
	 * NULL).
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ordem_servico_id", nullable = false)
	private OrdemServico ordemServico;

	/**
	 * Modalidade do frete (modalidade_id INTEGER NOT NULL).
	 * Nota: O nome do campo foi ajustado para 'modalidade' para consistência.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "modalidade_id", nullable = false)
	private ModalidadeFrete modalidade;

	/**
	 * Status atual do leilão (status_leilao_id INTEGER NOT NULL).
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "status_leilao_id", nullable = false)
	private StatusLeilao statusLeilao;

	/**
	 * Transportador que ganhou o leilão (transportador_selecionado_id BIGINT).
	 * Este campo é NULO se o leilão ainda não foi finalizado ou não teve vencedor.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "transportador_selecionado_id", nullable = true)
	private Transportador transportadorSelecionado;

	// --- CAMPOS DE DADOS DA NEGOCIAÇÃO ---

	/**
	 * Data e hora de expiração da negociação (TIMESTAMP WITH TIME ZONE).
	 */
	@Column(name = "data_expiracao_negociacao")
	private LocalDateTime dataExpiracaoNegociacao;

	/**
	 * Distância rodoviária em Km (NUMERIC(10, 2)).
	 */
	@Column(name = "distancia_km", precision = 10, scale = 2)
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
	 * Peso total da carga em Kg (Necessário para cálculo ANTT).
	 */
	@Column(name = "peso_total_kg", precision = 10, scale = 2)
	private BigDecimal pesoTotalKg;

	/**
	 * Valor inicial proposto pelo cliente/sistema (Para referência).
	 */
	@Column(name = "valor_inicial_proposto", precision = 10, scale = 2)
	private BigDecimal valorInicialProposto;

	/**
	 * Valor final do frete aceito (NUMERIC(10, 2)). Será o valor do lance vencedor.
	 */
	@Column(name = "valor_final_aceito", precision = 10, scale = 2)
	private BigDecimal valorFinalAceito;

	/**
	 * Informação adicional sobre o tipo de embalagem (VARCHAR(50)).
	 */
	@Column(name = "tipo_embalagem", length = 50)
	private String tipoEmbalagem;

	// --- MÉTODOS AUXILIARES ---

	/**
	 * Getter auxiliar para o ID da Ordem de Serviço, muito usado no FreteService.
	 */
	public Long getOrdemServicoId() {
		return this.ordemServico != null ? this.ordemServico.getId() : null;
	}
}
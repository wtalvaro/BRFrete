package br.com.wta.frete.colaboradores.entity;

import java.math.BigDecimal;

import br.com.wta.frete.colaboradores.entity.enums.TipoVeiculo;
import br.com.wta.frete.logistica.entity.ModalidadeFrete;
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
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Mapeia a tabela 'colaboradores.metricas_transportador'. Representa um
 * catálogo
 * de parâmetros de precificação customizados, definidos pelo Transportador para
 * ser usado em veículos e tipos de carga específicos.
 */
@Entity
@Table(name = "metricas_transportador", schema = "colaboradores", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "transportador_pessoa_id",
                "nome_metrica" }, name = "uk_metrica_nome_transportador")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MetricaTransportadorFrete {

    /**
     * Chave Primária (metrica_id BIGSERIAL).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "metrica_id")
    private Long metricaId;

    /**
     * Relacionamento Muitos-para-Um com o Transportador (dono da métrica).
     * Mapeia para a FK 'transportador_pessoa_id'.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transportador_pessoa_id", nullable = false)
    private Transportador transportador;

    /**
     * Nome descritivo da métrica (Ex: "Caminhão Toco - Custo Padrão Sucata Leve").
     */
    @Column(name = "nome_metrica", nullable = false, length = 100)
    private String nomeMetrica;

    // --- CAMPOS DE FILTRO PARA PRECIFICAÇÃO DINÂMICA ---

    /**
     * Tipo de Carga/Material (Ex: "Ferro", "Plástico", "Carga Geral").
     * Permite 'NULL' se for uma métrica geral que se aplica a qualquer carga.
     */
    @Column(name = "tipo_carga_material", length = 100)
    private String tipoCargaMaterial;

    /**
     * Tipo de Veículo para o qual a métrica se aplica.
     * Permite 'NULL' se a métrica puder ser usada por qualquer veículo do
     * transportador.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_veiculo")
    private TipoVeiculo tipoVeiculo;

    /**
     * Modalidade de Frete (Ex: Lotação, Fracionado).
     * Permite 'NULL' se for uma métrica geral de modalidade.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "modalidade_frete_id")
    private ModalidadeFrete modalidadeFrete;

    // --- Parâmetros de Custo Personalizados ---

    /**
     * Custo Fixo por Viagem (R$) (NUMERIC(10, 2)).
     */
    @Column(name = "custo_fixo_viagem", nullable = false, precision = 10, scale = 2)
    private BigDecimal custoFixoViagem = BigDecimal.ZERO;

    /**
     * Custo por quilômetro rodado (R$/km) (NUMERIC(10, 4)).
     */
    @Column(name = "custo_por_km", nullable = false, precision = 10, scale = 4)
    private BigDecimal custoPorKm = BigDecimal.ZERO;

    /**
     * Margem de lucro percentual (0.0 a 1.0) (NUMERIC(5, 4)).
     */
    @Column(name = "margem_lucro", precision = 5, scale = 4)
    private BigDecimal margemLucro = new BigDecimal("0.1000");

    /**
     * Custo por hora de espera/carga/descarga (R$/h) (NUMERIC(10, 2)).
     */
    @Column(name = "custo_hora_espera", precision = 10, scale = 2)
    private BigDecimal custoHoraEspera = BigDecimal.ZERO;

    // =========================================================================
    // 🛠️ Campo: Controle de Concorrência Otimista
    // =========================================================================

    @Version
    @Column(name = "versao", nullable = false)
    private Integer versao = 0;
}
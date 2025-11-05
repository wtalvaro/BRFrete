package br.com.wta.frete.colaboradores.entity;

import br.com.wta.frete.core.entity.Pessoa;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalTime;

/**
 * Entidade que mapeia a tabela 'colaboradores.horarios_operacao'.
 * Armazena os horários de funcionamento (abertura e fechamento) de qualquer
 * colaborador que opere em um local fixo (Lojista, Sucateiro, etc.).
 */
@Entity
@Table(name = "horarios_operacao", schema = "colaboradores", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "pessoa_id", "dia_semana", "hora_abertura" }, name = "uk_horarios_operacao")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HorarioOperacao {

    /**
     * Chave primária auto-gerada (horario_id BIGSERIAL).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "horario_id")
    private Long horarioId;

    /**
     * Chave estrangeira para a Pessoa/Colaborador que define o horário.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pessoa_id", nullable = false)
    private Pessoa pessoa;

    /**
     * Dia da semana (SMALLINT NOT NULL).
     * Recomenda-se o uso de um ENUM ou constante na aplicação (ex: 1=Domingo,
     * 2=Segunda).
     */
    @Column(name = "dia_semana", nullable = false)
    private Short diaSemana;

    /**
     * Horário de abertura (TIME WITHOUT TIME ZONE NOT NULL).
     */
    @Column(name = "hora_abertura", nullable = false, columnDefinition = "TIME WITHOUT TIME ZONE")
    private LocalTime horaAbertura;

    /**
     * Horário de fechamento (TIME WITHOUT TIME ZONE NOT NULL).
     */
    @Column(name = "hora_fechamento", nullable = false, columnDefinition = "TIME WITHOUT TIME ZONE")
    private LocalTime horaFechamento;
}
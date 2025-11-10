package br.com.wta.frete.core.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Mapeia a tabela 'core.perfis'.
 * Define os diferentes perfis de acesso/função no sistema (e.g., ADMIN, TRANSPORTADOR).
 */
@Entity
@Table(
    name = "perfis", 
    schema = "core",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = "nome_perfil")
    }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Perfil {

    /**
     * Chave primária (SERIAL). Mapeado para Integer.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "perfil_id")
    private Integer id;

    /**
     * Nome único do perfil (VARCHAR(50) UNIQUE NOT NULL).
     */
    @Column(name = "nome_perfil", nullable = false, length = 50)
    private String nomePerfil;

    /**
     * Descrição detalhada do perfil (TEXT).
     */
    @Column(name = "descricao", columnDefinition = "TEXT")
    private String descricao;
    
    // NOTA: O relacionamento Muitos-para-Muitos (M:M) com Pessoa
    // será definido na classe PessoaPerfil.
}
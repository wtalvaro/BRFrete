package br.com.wta.frete.colaboradores.repository;

import br.com.wta.frete.colaboradores.entity.MetricaTransportadorFrete;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositório JPA para a entidade MetricaTransportadorFrete.
 * Gerencia a persistência das métricas de preço personalizadas dos
 * transportadores.
 *
 * NOTA: Métodos básicos como save(), findById(), findAll() e deleteById() são
 * herdados de JpaRepository e não precisam ser declarados aqui.
 */
@Repository
public interface MetricaTransportadorFreteRepository extends JpaRepository<MetricaTransportadorFrete, Long> {

    /**
     * Busca uma métrica de frete pelo seu ID. (Herdado, mas útil para consulta)
     */
    @SuppressWarnings("null")
    Optional<MetricaTransportadorFrete> findById(Long id);

    /**
     * Método básico, mas crucial: busca todas as métricas pertencentes a um
     * transportador.
     * O Spring Data JPA gera esta query automaticamente com base no nome do método,
     * utilizando a propriedade 'transportador.pessoaId' na Entidade.
     */
    List<MetricaTransportadorFrete> findByTransportadorPessoaId(Long transportadorId);

    // *************************************************************************
    // O método findMatchingMetrics (lógica complexa de precificação) será
    // adicionado quando integrarmos o FreteService.
    // *************************************************************************
}
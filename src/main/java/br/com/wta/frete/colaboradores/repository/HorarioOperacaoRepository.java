package br.com.wta.frete.colaboradores.repository;

import br.com.wta.frete.colaboradores.entity.HorarioOperacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositório JPA para a entidade HorarioOperacao.
 * Permite operações CRUD e consultas específicas para gerenciar os horários
 * de funcionamento dos colaboradores (Lojistas e Sucateiros).
 */
@Repository
public interface HorarioOperacaoRepository extends JpaRepository<HorarioOperacao, Long> {

    /**
     * Documentação: Encontra todos os horários de operação para uma
     * Pessoa/Colaborador.
     * * @param pessoaId O ID da Pessoa (Lojista/Sucateiro).
     * 
     * @return Uma lista de HorarioOperacao.
     */
    List<HorarioOperacao> findByPessoaId(Long pessoaId);

    /**
     * Documentação: Encontra todos os horários de operação para um determinado
     * dia da semana e Pessoa.
     * * @param pessoaId O ID da Pessoa.
     * 
     * @param diaSemana O dia da semana (ex: 1 a 7).
     * @return Uma lista de HorarioOperacao.
     */
    List<HorarioOperacao> findByPessoaIdAndDiaSemana(Long pessoaId, Short diaSemana);
}
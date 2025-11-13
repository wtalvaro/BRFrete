package br.com.wta.frete.core.service;

import java.util.List;

// Os imports são drasticamente reduzidos!
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.wta.frete.core.controller.dto.CadastroSimplificadoRequest;
import br.com.wta.frete.core.controller.dto.OAuth2UserRequestDTO;
import br.com.wta.frete.core.controller.dto.PessoaRequest;
import br.com.wta.frete.core.entity.Pessoa;
import br.com.wta.frete.core.repository.PessoaRepository;
import br.com.wta.frete.shared.exception.ResourceNotFoundException;

/**
 * Service de Orquestração (Fachada) para a entidade Pessoa.
 * Contém a lógica CORE de gestão de estado (Ativação) e delega
 * as lógicas de Cadastro e Login Social para serviços especializados.
 */
@Service
public class PessoaService {

    private final PessoaRepository pessoaRepository;
    private final CadastroPessoaService cadastroPessoaService;
    private final PessoaSocialService pessoaSocialService; // NOVO: Injeção do serviço social

    // Injeção de Dependências
    public PessoaService(
            PessoaRepository pessoaRepository,
            CadastroPessoaService cadastroPessoaService,
            PessoaSocialService pessoaSocialService) {
        this.pessoaRepository = pessoaRepository;
        this.cadastroPessoaService = cadastroPessoaService;
        this.pessoaSocialService = pessoaSocialService;
    }

    // =================================================================
    // MÉTODOS DELEAGADOS PARA CADASTRO PRIMÁRIO
    // =================================================================

    /**
     * Documentação: Delega a lógica de cadastro completo para
     * CadastroPessoaService.
     */
    public Pessoa cadastrarPessoa(PessoaRequest request) {
        return cadastroPessoaService.cadastrarPessoa(request);
    }

    /**
     * Documentação: Delega a lógica de cadastro simplificado para
     * CadastroPessoaService.
     */
    public Pessoa cadastrarPessoaSimplificado(CadastroSimplificadoRequest request) {
        return cadastroPessoaService.cadastrarPessoaSimplificado(request);
    }

    // =================================================================
    // MÉTODOS DELEAGADOS PARA LOGIN/CADASTRO SOCIAL
    // =================================================================

    /**
     * Documentação: Delega a lógica de login/cadastro social para
     * PessoaSocialService.
     */
    public Pessoa cadastrarOuObterPessoaSocial(OAuth2UserRequestDTO request) {
        return pessoaSocialService.cadastrarOuObterPessoaSocial(request);
    }

    // =================================================================
    // CRUD: READ (Leitura)
    // =================================================================

    /**
     * Busca uma Pessoa pelo ID (Chave Primária).
     * @param pessoaId O ID da Pessoa.
     * @return A entidade Pessoa.
     * @throws ResourceNotFoundException Se a Pessoa não for encontrada.
     */
    @SuppressWarnings("null")
    public Pessoa buscarPorId(Long pessoaId) {
        return pessoaRepository.findById(pessoaId)
                .orElseThrow(() -> new ResourceNotFoundException(
                    String.format("Pessoa não encontrada para o ID: '%d'", pessoaId),
                    "PESSOA_NAO_ENCONTRADA_ID"
                ));
    }

    /**
     * Lista todas as Pessoas cadastradas.
     * @return Uma lista de entidades Pessoa.
     */
    public List<Pessoa> listarTodas() {
        return pessoaRepository.findAll();
    }

    // =================================================================
    // CRUD: UPDATE (Atualização)
    // =================================================================

    /**
     * Atualiza os dados básicos de uma Pessoa existente (nome, telefone).
     * * @param pessoaId O ID da Pessoa a ser atualizada.
     * @param request O DTO com os novos dados (PessoaRequest).
     * @return A entidade Pessoa atualizada.
     * @throws ResourceNotFoundException Se a Pessoa não for encontrada.
     */
    @Transactional
    public Pessoa atualizarPessoa(Long pessoaId, PessoaRequest request) {
        Pessoa pessoaExistente = buscarPorId(pessoaId);

        // Regra de Negócio: Campos que podem ser alterados pelo usuário via PUT
        pessoaExistente.setNome(request.nomeCompleto());

        // CORREÇÃO: Deve usar request.telefone() e NÃO request.senha()
        pessoaExistente.setTelefone(request.telefone()); // <-- CORRIGIDO AQUI!

        // Se houver mais campos que você queira garantir que não estão sendo usados
        // pessoaExistente.setSenha(pessoaExistente.getSenha()); // Senha não deve ser
        // alterada aqui.

        return pessoaRepository.save(pessoaExistente);
    }

    // =================================================================
    // CRUD: DELETE (Deletar)
    // =================================================================

    /**
     * Deleta uma Pessoa pelo ID.
     *
     * @param pessoaId O ID da Pessoa a ser deletada.
     * @throws ResourceNotFoundException Se a Pessoa não for encontrada.
     */
    @SuppressWarnings("null")
    @Transactional
    public void deletarPessoa(Long pessoaId) {
        Pessoa pessoa = buscarPorId(pessoaId); // Garante que a pessoa existe
        
        // A deleção CASCADE (se configurada no banco) ou o CascadeType.ALL nas relações JPA 
        // tratará entidades dependentes (como PessoaPerfil).
        
        pessoaRepository.delete(pessoa);
    }

    // =================================================================
    // MÉTODOS CORE (Gerenciamento de Estado) - Originalmente no Service
    // =================================================================

    /**
     * Ativa a conta da Pessoa no PostgreSQL.
     * Esta é uma responsabilidade CORE de gestão de estado do utilizador.
     * * @param pessoaId O ID da Pessoa a ser ativada.
     */
    @Transactional
    public void ativarPessoa(Long pessoaId) {
        // CORRIGIDO: Usa buscarPorId() para lançar ResourceNotFoundException consistente.
        Pessoa pessoa = buscarPorId(pessoaId); 
        
        // Se a pessoa já estiver ativa, não faz nada
        if (pessoa.isAtivo()) {
            return;
        }

        pessoa.setAtivo(true);
        // Lógica opcional de mudança de perfil poderia estar aqui.

        pessoaRepository.save(pessoa);
        System.out.println("Conta da Pessoa ID " + pessoaId + " ativada no banco de dados.");
    }
}
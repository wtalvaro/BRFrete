package br.com.wta.frete.core.service;

// Os imports são drasticamente reduzidos!
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.wta.frete.core.controller.dto.PessoaRequest;
import br.com.wta.frete.core.controller.dto.CadastroSimplificadoRequest;
import br.com.wta.frete.core.controller.dto.OAuth2UserRequestDTO;
import br.com.wta.frete.core.entity.Pessoa;
import br.com.wta.frete.core.repository.PessoaRepository;

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
    // MÉTODOS CORE (Gerenciamento de Estado)
    // =================================================================

    /**
     * Documentação: Ativa a conta da Pessoa no PostgreSQL.
     * Esta é uma responsabilidade CORE de gestão de estado do utilizador.
     * * @param pessoaId O ID da Pessoa a ser ativada.
     */
    @Transactional
    public void ativarPessoa(Long pessoaId) {
        Pessoa pessoa = pessoaRepository.findById(pessoaId)
                .orElseThrow(() -> new IllegalArgumentException("Pessoa não encontrada para ativação."));

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
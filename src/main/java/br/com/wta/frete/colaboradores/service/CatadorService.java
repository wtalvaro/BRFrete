package br.com.wta.frete.colaboradores.service;

import br.com.wta.frete.colaboradores.controller.dto.CatadorResponse;
import br.com.wta.frete.colaboradores.entity.Catador;
import br.com.wta.frete.colaboradores.repository.CatadorRepository;
import br.com.wta.frete.colaboradores.service.mapper.CatadorMapper;
import br.com.wta.frete.core.entity.Pessoa;
import br.com.wta.frete.core.entity.Perfil;
import br.com.wta.frete.core.entity.PessoaPerfil;
import br.com.wta.frete.core.entity.PessoaPerfilId;
import br.com.wta.frete.core.repository.PessoaRepository;
import br.com.wta.frete.core.repository.PerfilRepository;
import br.com.wta.frete.core.repository.PessoaPerfilRepository;
import br.com.wta.frete.shared.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service dedicado à lógica de cadastro, obtenção e gerenciamento do perfil
 * Catador.
 * Garante que a entidade Catador seja criada e que o perfil de acesso seja
 * associado à Pessoa.
 */
@Service
public class CatadorService {

    private final CatadorRepository catadorRepository;
    private final CatadorMapper catadorMapper;
    private final PessoaRepository pessoaRepository;
    private final PerfilRepository perfilRepository;
    private final PessoaPerfilRepository pessoaPerfilRepository;

    // Injeção de Dependências
    public CatadorService(CatadorRepository catadorRepository,
            CatadorMapper catadorMapper,
            PessoaRepository pessoaRepository,
            PerfilRepository perfilRepository,
            PessoaPerfilRepository pessoaPerfilRepository) {
        this.catadorRepository = catadorRepository;
        this.catadorMapper = catadorMapper;
        this.pessoaRepository = pessoaRepository;
        this.perfilRepository = perfilRepository;
        this.pessoaPerfilRepository = pessoaPerfilRepository;
    }

    /**
     * Adiciona o perfil de Catador a uma Pessoa existente.
     * Cria a entidade Catador (se não existir) e associa o perfil 'CATADOR' à
     * Pessoa.
     *
     * @param pessoaId O ID da Pessoa que receberá o perfil.
     * @return O DTO de resposta do Catador.
     */
    @SuppressWarnings("null")
    @Transactional
    public CatadorResponse adicionarPerfilCatador(Long pessoaId) {

        // 1. Busca a Pessoa
        Pessoa pessoa = pessoaRepository.findById(pessoaId)
                .orElseThrow(() -> new ResourceNotFoundException("Pessoa não encontrada com ID: " + pessoaId));

        // 2. Busca o Perfil 'CATADOR'
        // Assume que este perfil foi inserido via script V1__Initial_Schema.sql
        Perfil perfilCatador = perfilRepository.findByNomePerfil("CATADOR")
                .orElseThrow(() -> new IllegalStateException(
                        "Perfil 'CATADOR' não encontrado. Verifique a inicialização de dados."));

        // 3. Cria ou Obtém a Entidade Catador
        Optional<Catador> catadorExistente = catadorRepository.findByPessoaId(pessoaId);

        // Se não existir, cria e salva a nova entidade Catador, linkando-a à Pessoa.
        Catador catador = catadorExistente.orElseGet(() -> {
            Catador novoCatador = new Catador();
            novoCatador.setPessoa(pessoa);
            // Catador não tem campos específicos adicionais
            return catadorRepository.save(novoCatador);
        });

        // 4. Adiciona/Atualiza o Perfil de Catador à Pessoa
        PessoaPerfilId pessoaPerfilId = new PessoaPerfilId(pessoaId, perfilCatador.getId()); // Cria chave composta

        // Tentamos carregar a associação existente ou criamos uma nova
        Optional<PessoaPerfil> associacaoExistente = pessoaPerfilRepository.findById(pessoaPerfilId);

        if (associacaoExistente.isEmpty()) {
            PessoaPerfil novaPessoaPerfil = new PessoaPerfil(pessoa, perfilCatador);
            pessoaPerfilRepository.save(novaPessoaPerfil);
        }

        // 5. Atualiza o flag isColaborador na Pessoa
        if (!pessoa.isColaborador()) {
            pessoa.setColaborador(true);
            pessoaRepository.save(pessoa);
        }

        // 6. Mapeia e retorna a resposta
        return catadorMapper.toResponse(catador);
    }
}
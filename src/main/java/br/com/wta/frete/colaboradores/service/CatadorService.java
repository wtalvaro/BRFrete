package br.com.wta.frete.colaboradores.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.wta.frete.colaboradores.controller.dto.CatadorResponse;
import br.com.wta.frete.colaboradores.entity.Catador;
import br.com.wta.frete.colaboradores.repository.CatadorRepository;
import br.com.wta.frete.colaboradores.service.mapper.CatadorMapper;
import br.com.wta.frete.core.entity.Pessoa;

/**
 * Service dedicado à lógica de cadastro, obtenção e gerenciamento do perfil
 * Catador.
 * O gerenciamento do perfil e da Pessoa agora é delegado ao PerfilServiceComum.
 */
@Service
public class CatadorService {

    private final CatadorRepository catadorRepository;
    private final CatadorMapper catadorMapper;
    private final PerfilAssociacaoService perfilServiceComum; // NOVO: Injeção do Service Comum

    // Construtor ATUALIZADO
    public CatadorService(CatadorRepository catadorRepository,
            CatadorMapper catadorMapper,
            PerfilAssociacaoService perfilServiceComum) { // Apenas dependências essenciais
        this.catadorRepository = catadorRepository;
        this.catadorMapper = catadorMapper;
        this.perfilServiceComum = perfilServiceComum;
    }

    /**
     * Adiciona o perfil de Catador a uma Pessoa existente.
     * Usa o Service Comum para gerenciar a associação de perfil e o flag
     * isColaborador.
     *
     * @param pessoaId O ID da Pessoa que receberá o perfil.
     * @return O DTO de resposta do Catador.
     */
    @Transactional
    public CatadorResponse adicionarPerfilCatador(Long pessoaId) {

        // 1. UTILIZA O SERVIÇO COMUM (Substitui os passos 1, 2, 4 e 5 originais)
        Pessoa pessoa = perfilServiceComum.associarPerfilColaborador(pessoaId, "CATADOR");

        // 2. Cria ou Obtém a Entidade Catador (Lógica Específica)
        Optional<Catador> catadorExistente = catadorRepository.findByPessoaId(pessoaId);

        // Se não existir, cria e salva a nova entidade Catador, linkando-a à Pessoa.
        Catador catador = catadorExistente.orElseGet(() -> {
            Catador novoCatador = new Catador();
            novoCatador.setPessoa(pessoa);
            // Catador não tem campos específicos adicionais
            return catadorRepository.save(novoCatador);
        });

        // 3. Mapeia e retorna a resposta
        return catadorMapper.toResponse(catador);
    }
}
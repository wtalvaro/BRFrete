package br.com.wta.frete.colaboradores.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.wta.frete.colaboradores.controller.dto.LojistaRequest;
import br.com.wta.frete.colaboradores.controller.dto.LojistaResponse;
import br.com.wta.frete.colaboradores.entity.Lojista;
import br.com.wta.frete.colaboradores.repository.LojistaRepository;
import br.com.wta.frete.colaboradores.service.mapper.LojistaMapper;
// Removidas as importações de Perfil, PessoaPerfil, PessoaPerfilId, PessoaRepository, PerfilRepository e PessoaPerfilRepository
import br.com.wta.frete.core.entity.Pessoa;

/**
 * Service dedicado à lógica de cadastro, obtenção e gerenciamento do perfil
 * Lojista.
 * O gerenciamento do perfil e da Pessoa agora é delegado ao PerfilServiceComum.
 */
@Service
public class LojistaService {

    private final LojistaRepository lojistaRepository;
    private final LojistaMapper lojistaMapper;
    private final PerfilAssociacaoService perfilServiceComum; // NOVO: Injeção do Service Comum

    // Construtor ATUALIZADO
    public LojistaService(LojistaRepository lojistaRepository, LojistaMapper lojistaMapper,
            PerfilAssociacaoService perfilServiceComum) { // Apenas dependências essenciais
        this.lojistaRepository = lojistaRepository;
        this.lojistaMapper = lojistaMapper;
        this.perfilServiceComum = perfilServiceComum;
    }

    /**
     * Adiciona ou atualiza o perfil 'LOJISTA' a uma Pessoa existente.
     *
     * @param request O DTO com o ID da pessoa e os dados do Lojista.
     * @return O DTO de resposta do Lojista.
     */
    @SuppressWarnings("null")
    @Transactional
    public LojistaResponse adicionarPerfilLojista(LojistaRequest request) {
        Long pessoaId = request.pessoaId();

        // 1. UTILIZA O SERVIÇO COMUM (Substitui os passos 1, 2, 4 e 5 originais)
        // Valida Pessoa, busca Perfil 'LOJISTA' e faz a associação/atualização do
        // isColaborador.
        Pessoa pessoa = perfilServiceComum.associarPerfilColaborador(pessoaId, "LOJISTA");

        // 2. Cria OU Atualiza a Entidade Lojista (Lógica Específica)
        Optional<Lojista> lojistaExistente = lojistaRepository.findByPessoaId(pessoaId);

        Lojista lojista;

        if (lojistaExistente.isPresent()) {
            // Caminho de ATUALIZAÇÃO
            lojista = lojistaExistente.get();
            lojistaMapper.updateEntityFromRequest(request, lojista);
            lojista = lojistaRepository.save(lojista);
        } else {
            // Caminho de CRIAÇÃO
            Lojista novoLojista = lojistaMapper.toEntity(request);
            novoLojista.setPessoa(pessoa);
            lojista = lojistaRepository.save(novoLojista);
        }

        // 3. Mapeia e retorna a resposta
        return lojistaMapper.toResponse(lojista);
    }
}
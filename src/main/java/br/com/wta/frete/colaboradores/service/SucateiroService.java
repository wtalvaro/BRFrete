package br.com.wta.frete.colaboradores.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.wta.frete.colaboradores.controller.dto.SucateiroRequest;
import br.com.wta.frete.colaboradores.controller.dto.SucateiroResponse;
import br.com.wta.frete.colaboradores.entity.Sucateiro;
import br.com.wta.frete.colaboradores.repository.SucateiroRepository;
import br.com.wta.frete.colaboradores.service.mapper.SucateiroMapper;
import br.com.wta.frete.core.entity.Pessoa;
import jakarta.persistence.EntityManager; // Importação ainda necessária para o fluxo de persistência específico

@Service
public class SucateiroService {

    private final SucateiroRepository sucateiroRepository;
    private final SucateiroMapper sucateiroMapper;
    private final EntityManager entityManager; // Mantido para o tratamento específico do @MapsId
    private final PerfilAssociacaoService perfilServiceComum; // NOVO: Injeção do Service Comum

    // Construtor atualizado
    public SucateiroService(SucateiroRepository sucateiroRepository,
            SucateiroMapper sucateiroMapper, EntityManager entityManager,
            PerfilAssociacaoService perfilServiceComum) { // Apenas dependências essenciais
        this.sucateiroRepository = sucateiroRepository;
        this.sucateiroMapper = sucateiroMapper;
        this.entityManager = entityManager;
        this.perfilServiceComum = perfilServiceComum;
    }

    /**
     * Adiciona o perfil 'SUCATEIRO' a uma Pessoa existente. Se a entidade
     * Sucateiro não existir, ela é criada e preenchida com os dados da requisição.
     *
     * @param pessoaId O ID da Pessoa a ser atualizada.
     * @param request  Os dados obrigatórios para a entidade Sucateiro.
     * @return Um DTO de resposta do Sucateiro.
     */
    @Transactional
    public SucateiroResponse adicionarPerfilSucateiro(Long pessoaId, SucateiroRequest request) {

        // 1. UTILIZA O SERVIÇO COMUM (Substitui os passos 1, 2, 4 e 5 originais)
        // Valida Pessoa, busca Perfil 'SUCATEIRO' e faz a associação/atualização do
        // isColaborador.
        Pessoa pessoa = perfilServiceComum.associarPerfilColaborador(pessoaId, "SUCATEIRO");

        // 2. Cria ou Obtém a Entidade Sucateiro (Lógica Específica)
        Sucateiro sucateiro = sucateiroRepository.findByPessoaId(pessoaId)
                // Se o Sucateiro já existir, apenas o mapeamos com os novos dados.
                .map(s -> {
                    sucateiroMapper.updateEntity(request, s);
                    return s;
                })
                // Se o Sucateiro não existir, criamos e o salvamos usando PERSIST (tratamento
                // de @MapsId).
                .orElseGet(() -> {
                    Sucateiro novoSucateiro = sucateiroMapper.toEntity(request);
                    novoSucateiro.setPessoa(pessoa);
                    novoSucateiro.setPessoaId(pessoaId);

                    // Ação crítica: Usamos persist para forçar um INSERT.
                    entityManager.persist(novoSucateiro);
                    entityManager.flush();

                    return novoSucateiro;
                });

        // 3. Mapeia e retorna a resposta
        return sucateiroMapper.toResponse(sucateiro);
    }
}
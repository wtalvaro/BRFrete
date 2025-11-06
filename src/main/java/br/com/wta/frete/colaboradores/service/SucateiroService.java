package br.com.wta.frete.colaboradores.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.wta.frete.colaboradores.controller.dto.SucateiroRequest;
import br.com.wta.frete.colaboradores.controller.dto.SucateiroResponse;
import br.com.wta.frete.colaboradores.entity.Sucateiro;
import br.com.wta.frete.colaboradores.repository.SucateiroRepository;
import br.com.wta.frete.colaboradores.service.mapper.SucateiroMapper;
import br.com.wta.frete.core.entity.Perfil;
import br.com.wta.frete.core.entity.Pessoa;
import br.com.wta.frete.core.entity.PessoaPerfil;
import br.com.wta.frete.core.entity.PessoaPerfilId;
import br.com.wta.frete.core.repository.PerfilRepository;
import br.com.wta.frete.core.repository.PessoaPerfilRepository;
import br.com.wta.frete.core.repository.PessoaRepository;
import br.com.wta.frete.shared.exception.ResourceNotFoundException;
import jakarta.persistence.EntityManager; // Importação necessária

@Service
public class SucateiroService {

    private final SucateiroRepository sucateiroRepository;
    private final PessoaRepository pessoaRepository;
    private final PerfilRepository perfilRepository;
    private final PessoaPerfilRepository pessoaPerfilRepository;
    private final SucateiroMapper sucateiroMapper;
    private final EntityManager entityManager; // Novo campo

    // Construtor atualizado para injetar EntityManager
    public SucateiroService(SucateiroRepository sucateiroRepository, PessoaRepository pessoaRepository,
            PerfilRepository perfilRepository, PessoaPerfilRepository pessoaPerfilRepository,
            SucateiroMapper sucateiroMapper, EntityManager entityManager) {
        this.sucateiroRepository = sucateiroRepository;
        this.pessoaRepository = pessoaRepository;
        this.perfilRepository = perfilRepository;
        this.pessoaPerfilRepository = pessoaPerfilRepository;
        this.sucateiroMapper = sucateiroMapper;
        this.entityManager = entityManager; // Injeção
    }

    /**
     * Adiciona o perfil 'SUCATEIRO' a uma Pessoa existente. Se a entidade
     * Sucateiro não existir, ela é criada e preenchida com os dados da requisição.
     *
     * @param pessoaId O ID da Pessoa a ser atualizada.
     * @param request  Os dados obrigatórios para a entidade Sucateiro.
     * @return Um DTO de resposta do Sucateiro.
     */
    @SuppressWarnings("null")
    @Transactional
    public SucateiroResponse adicionarPerfilSucateiro(Long pessoaId, SucateiroRequest request) {
        // 1. Valida se a Pessoa existe
        Pessoa pessoa = pessoaRepository.findById(pessoaId)
                .orElseThrow(() -> new ResourceNotFoundException("Pessoa com ID " + pessoaId + " não encontrada."));

        // 2. Obtém o perfil SUCATEIRO
        Perfil perfilSucateiro = perfilRepository.findByNomePerfil("SUCATEIRO")
                .orElseThrow(() -> new IllegalStateException(
                        "Perfil 'SUCATEIRO' não encontrado. Verifique a inicialização de dados."));

        // 3. Cria ou Obtém a Entidade Sucateiro
        Sucateiro sucateiro = sucateiroRepository.findByPessoaId(pessoaId)
                // Se o Sucateiro já existir, apenas o mapeamos com os novos dados.
                .map(s -> {
                    sucateiroMapper.updateEntity(request, s);
                    return s;
                })
                // Se o Sucateiro não existir, criamos e o salvamos usando PERSIST.
                .orElseGet(() -> {
                    Sucateiro novoSucateiro = sucateiroMapper.toEntity(request);
                    novoSucateiro.setPessoa(pessoa);

                    // CORREÇÃO CRÍTICA PARA @MapsId:
                    // Em entidades com Chave Derivada (@MapsId), o JPA/Hibernate pode
                    // interpretar um objeto com ID preenchido como 'detached', tentando
                    // um UPDATE em vez de um INSERT, o que causa o StaleObjectStateException.
                    // Definir explicitamente o ID garante que o Hibernate realize o INSERT
                    // correto para a nova entidade.
                    novoSucateiro.setPessoaId(pessoaId); // Mantemos esta linha.

                    // Ação crítica: Usamos persist para forçar um INSERT.
                    entityManager.persist(novoSucateiro);
                    entityManager.flush(); // Força a escrita no DB, garantindo que o INSERT ocorreu.

                    return novoSucateiro;
                });

        // 4. Adiciona/Atualiza o Perfil de Sucateiro à Pessoa
        PessoaPerfilId pessoaPerfilId = new PessoaPerfilId(pessoaId, perfilSucateiro.getId());
        Optional<PessoaPerfil> associacaoExistente = pessoaPerfilRepository.findById(pessoaPerfilId);

        if (associacaoExistente.isEmpty()) {
            PessoaPerfil novaPessoaPerfil = new PessoaPerfil(pessoa, perfilSucateiro);
            pessoaPerfilRepository.save(novaPessoaPerfil);
        }

        // 5. Atualiza o flag isColaborador na Pessoa
        if (!pessoa.isColaborador()) {
            pessoa.setColaborador(true);
            pessoaRepository.save(pessoa);
        }

        // 6. Mapeia e retorna a resposta
        return sucateiroMapper.toResponse(sucateiro);
    }
}
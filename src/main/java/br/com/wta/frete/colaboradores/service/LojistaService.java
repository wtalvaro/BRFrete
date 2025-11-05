package br.com.wta.frete.colaboradores.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.wta.frete.colaboradores.controller.dto.LojistaRequest;
import br.com.wta.frete.colaboradores.controller.dto.LojistaResponse;
import br.com.wta.frete.colaboradores.entity.Lojista;
import br.com.wta.frete.colaboradores.repository.LojistaRepository;
import br.com.wta.frete.colaboradores.service.mapper.LojistaMapper;
import br.com.wta.frete.core.entity.Perfil;
import br.com.wta.frete.core.entity.Pessoa;
import br.com.wta.frete.core.entity.PessoaPerfil;
import br.com.wta.frete.core.entity.PessoaPerfilId;
import br.com.wta.frete.core.repository.PerfilRepository;
import br.com.wta.frete.core.repository.PessoaPerfilRepository;
import br.com.wta.frete.core.repository.PessoaRepository;
import br.com.wta.frete.shared.exception.ResourceNotFoundException;

/**
 * Service dedicado à lógica de cadastro, obtenção e gerenciamento do perfil
 * Lojista.
 * Garante que a entidade Lojista seja criada e que o perfil de acesso seja
 * associado à Pessoa.
 */
@Service
public class LojistaService {

    private final LojistaRepository lojistaRepository;
    private final LojistaMapper lojistaMapper;
    private final PessoaRepository pessoaRepository;
    private final PerfilRepository perfilRepository;
    private final PessoaPerfilRepository pessoaPerfilRepository;

    public LojistaService(LojistaRepository lojistaRepository, LojistaMapper lojistaMapper,
            PessoaRepository pessoaRepository, PerfilRepository perfilRepository,
            PessoaPerfilRepository pessoaPerfilRepository) {
        this.lojistaRepository = lojistaRepository;
        this.lojistaMapper = lojistaMapper;
        this.pessoaRepository = pessoaRepository;
        this.perfilRepository = perfilRepository;
        this.pessoaPerfilRepository = pessoaPerfilRepository;
    }

    /**
     * Documentação: Adiciona ou atualiza o perfil 'LOJISTA' a uma Pessoa existente.
     * Implementa idempotência por meio do fluxo explícito de criação/atualização.
     *
     * @param request O DTO com o ID da pessoa e os dados do Lojista.
     * @return O DTO de resposta do Lojista.
     */
    @SuppressWarnings("null")
    @Transactional
    public LojistaResponse adicionarPerfilLojista(LojistaRequest request) {
        Long pessoaId = request.pessoaId();

        // 1. Valida se a Pessoa existe
        Pessoa pessoa = pessoaRepository.findById(pessoaId)
                .orElseThrow(() -> new ResourceNotFoundException("Pessoa com ID " + pessoaId + " não encontrada."));

        // 2. Obtém a Entidade Perfil 'LOJISTA'
        Perfil perfilLojista = perfilRepository.findByNomePerfil("LOJISTA")
                .orElseThrow(() -> new IllegalStateException(
                        "Perfil 'LOJISTA' não encontrado. Verifique a inicialização de dados."));

        // 3. Cria OU Atualiza a Entidade Lojista (Fluxo Idempotente)
        Optional<Lojista> lojistaExistente = lojistaRepository.findByPessoaId(pessoaId);

        Lojista lojista;

        if (lojistaExistente.isPresent()) {
            // Caminho de ATUALIZAÇÃO: Se já existe, atualiza e salva.
            lojista = lojistaExistente.get();
            // Uso do mapper com @MappingTarget para atualizar o objeto gerenciado
            lojistaMapper.updateEntityFromRequest(request, lojista);
            // Salva explicitamente o lojista ATUALIZADO
            lojista = lojistaRepository.save(lojista);
        } else {
            // Caminho de CRIAÇÃO: Se não existe, cria, associa a Pessoa e salva.
            Lojista novoLojista = lojistaMapper.toEntity(request);
            novoLojista.setPessoa(pessoa);
            // Salva explicitamente o novo lojista
            lojista = lojistaRepository.save(novoLojista);
        }

        // 4. Adiciona/Atualiza o Perfil de Lojista à Pessoa (Lógica de M:M)
        PessoaPerfilId pessoaPerfilId = new PessoaPerfilId(pessoaId, perfilLojista.getId());

        // Verifica se a associação já existe antes de tentar criá-la (Idempotência)
        if (pessoaPerfilRepository.findById(pessoaPerfilId).isEmpty()) {
            PessoaPerfil novaPessoaPerfil = new PessoaPerfil(pessoa, perfilLojista);
            pessoaPerfilRepository.save(novaPessoaPerfil);
        }

        // 5. Atualiza o flag isColaborador na Pessoa (se já não for colaborador)
        // Isso deve ser feito APENAS se houver uma mudança de estado
        if (!pessoa.isColaborador()) {
            pessoa.setColaborador(true);
            pessoaRepository.save(pessoa);
        }

        // 6. Mapeia e retorna a resposta
        return lojistaMapper.toResponse(lojista);
    }
}
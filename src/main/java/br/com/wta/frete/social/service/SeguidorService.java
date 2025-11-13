package br.com.wta.frete.social.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.wta.frete.core.repository.PessoaRepository; // Assumido para validação
import br.com.wta.frete.social.controller.dto.SeguidorRequest;
import br.com.wta.frete.social.controller.dto.SeguidorResponse;
import br.com.wta.frete.social.entity.Seguidor;
import br.com.wta.frete.social.entity.SeguidorId;
import br.com.wta.frete.social.repository.SeguidorRepository;
import br.com.wta.frete.social.service.mapper.SeguidorMapper;
import lombok.RequiredArgsConstructor;

/**
 * Serviço responsável pela lógica de negócio da entidade Seguidor
 * (relacionamentos de seguir/deixar de seguir).
 */
@Service
@RequiredArgsConstructor
public class SeguidorService {

    private final SeguidorRepository seguidorRepository;
    private final SeguidorMapper seguidorMapper;
    private final PessoaRepository pessoaRepository; // Necessário para buscar as entidades Pessoa

    /**
     * 🚀 Estabelece uma nova relação de "Seguir".
     *
     * @param request DTO com os IDs do seguidor e do seguido.
     * @return O DTO de resposta confirmando a relação.
     */
    @Transactional
    public SeguidorResponse seguirPessoa(SeguidorRequest request) {
        Long seguidorId = request.seguidorId();
        Long seguidoId = request.seguidoId();

        // 1. Regra de Negócio: Não pode seguir a si mesmo (Embora haja um CHECK no SQL,
        // é bom validar na aplicação)
        if (seguidorId.equals(seguidoId)) {
            throw new IllegalArgumentException("Uma pessoa não pode seguir a si mesma.");
        }

        SeguidorId id = new SeguidorId(seguidorId, seguidoId);

        // 2. Regra de Negócio: Verifica se a relação já existe
        if (seguidorRepository.existsById(id)) {
            throw new IllegalArgumentException("A relação de 'seguir' já existe.");
        }

        // 3. Validação de Existência (Melhoria futura: Adicionar uma exceção
        // customizada se Pessoa não for encontrada)
        // NOTE: No seu mapeamento, a Entidade Seguidor precisa dos objetos Pessoa
        // completos.
        Seguidor seguidorEntity = new Seguidor();
        seguidorEntity.setId(id);
        seguidorEntity.setSeguidor(pessoaRepository.getReferenceById(seguidorId));
        seguidorEntity.setSeguido(pessoaRepository.getReferenceById(seguidoId));

        // 4. Salva a nova relação
        Seguidor novoSeguidor = seguidorRepository.save(seguidorEntity);

        // 5. Converte para DTO de Resposta
        SeguidorResponse response = seguidorMapper.toResponse(novoSeguidor);

        // 6. Adiciona mensagem de status
        return new SeguidorResponse(
                response.seguidorId(),
                response.seguidoId(),
                "Relacionamento de seguir estabelecido com sucesso.");
    }

    /**
     * 🚫 Remove uma relação de "Seguir" (Deixar de Seguir).
     *
     * @param seguidorId O ID da pessoa que estava seguindo.
     * @param seguidoId  O ID da pessoa que estava sendo seguida.
     */
    @Transactional
    public void deixarDeSeguir(Long seguidorId, Long seguidoId) {
        SeguidorId id = new SeguidorId(seguidorId, seguidoId);

        if (!seguidorRepository.existsById(id)) {
            // Em vez de um erro fatal, podemos apenas logar ou retornar um status de
            // sucesso/não encontrado.
            return;
        }

        seguidorRepository.deleteById(id);
    }

    /**
     * 👥 Busca a lista de pessoas que uma determinada pessoa está seguindo.
     * * @param seguidorPessoaId O ID da pessoa cujos seguidos queremos listar.
     * 
     * @return Lista de DTOs com as relações de seguidor/seguido.
     */
    @Transactional(readOnly = true)
    public List<SeguidorResponse> buscarSeguidosPorPessoa(Long seguidorPessoaId) {
        List<Seguidor> seguidos = seguidorRepository.findByIdSeguidorId(seguidorPessoaId);
        return seguidorMapper.toResponseList(seguidos);
    }

    /**
     * 🤩 Busca a lista de seguidores de uma determinada pessoa.
     * * @param seguidoPessoaId O ID da pessoa cujos seguidores queremos listar.
     * 
     * @return Lista de DTOs com as relações de seguidor/seguido.
     */
    @Transactional(readOnly = true)
    public List<SeguidorResponse> buscarSeguidoresDePessoa(Long seguidoPessoaId) {
        List<Seguidor> seguidores = seguidorRepository.findByIdSeguidoId(seguidoPessoaId);
        return seguidorMapper.toResponseList(seguidores);
    }
}
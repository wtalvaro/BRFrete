package br.com.wta.frete.social.service;

import br.com.wta.frete.core.repository.PessoaRepository; // Assumido
import br.com.wta.frete.marketplace.repository.ProdutoRepository; // Assumido
import br.com.wta.frete.social.controller.dto.ComentarioRequest;
import br.com.wta.frete.social.controller.dto.ComentarioResponse;
import br.com.wta.frete.social.entity.Comentario;
import br.com.wta.frete.social.repository.ComentarioRepository;
import br.com.wta.frete.social.service.mapper.ComentarioMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Serviço responsável pela lógica de negócio da entidade Comentario
 * (social.comentarios).
 * Lida com a criação de comentários e respostas, e a recuperação das threads.
 */
@Service
@RequiredArgsConstructor
public class ComentarioService {

    private final ComentarioRepository comentarioRepository;
    private final ComentarioMapper comentarioMapper;

    // Repositórios assumidos para preenchimento de referências
    private final PessoaRepository pessoaRepository;
    private final ProdutoRepository produtoRepository;

    /**
     * ✍️ Posta um novo comentário ou uma resposta.
     *
     * @param request DTO com os dados do comentário (incluindo o ID do pai, se for
     *                resposta).
     * @return O DTO de resposta do comentário salvo.
     */
    @SuppressWarnings("null")
    @Transactional
    public ComentarioResponse postarComentario(ComentarioRequest request) {

        // 1. Converte o DTO para Entidade
        Comentario comentario = comentarioMapper.toEntity(request);

        // 2. Preenche as referências essenciais (Autor e Produto)

        // Nota: Assumimos que 'getReferenceById' lançará uma exceção se o ID não
        // existir
        comentario.setAutor(pessoaRepository.getReferenceById(request.autorId()));
        comentario.setProduto(produtoRepository.getReferenceById(request.produtoId()));

        // 3. Verifica e preenche a referência para o Comentário Pai (se for uma
        // resposta)
        if (request.comentarioPaiId() != null) {

            // Busca o Comentário Pai para garantir que ele exista.
            Optional<Comentario> comentarioPai = comentarioRepository.findById(request.comentarioPaiId());

            if (comentarioPai.isEmpty()) {
                throw new IllegalArgumentException(
                        "Comentário pai não encontrado para o ID: " + request.comentarioPaiId());
            }

            // Anexa a referência do comentário pai
            comentario.setComentarioPai(comentarioPai.get());
        }

        // 4. Salva o Comentário (ou Resposta)
        Comentario salvo = comentarioRepository.save(comentario);

        // 5. Converte e retorna o DTO de Resposta
        return comentarioMapper.toResponse(salvo);
    }

    /**
     * 📂 Busca todos os comentários principais (nível superior, sem pai) para um
     * Produto.
     * Esta consulta é otimizada para carregar a estrutura de respostas aninhadas.
     *
     * @param produtoId O ID do produto.
     * @return Uma lista de ComentarioResponse, onde cada item pode conter uma lista
     *         de 'respostas'.
     */
    @Transactional(readOnly = true)
    public List<ComentarioResponse> buscarComentariosPrincipaisPorProduto(Integer produtoId) {

        // Utiliza o método do repositório que busca apenas comentários sem pai
        List<Comentario> comentariosPrincipais = comentarioRepository.findByProdutoIdAndComentarioPaiIsNull(produtoId);

        // O Mapper (MapStruct) irá tratar automaticamente o mapeamento recursivo da
        // lista 'respostas'
        return comentarioMapper.toResponseList(comentariosPrincipais);
    }

    /**
     * 🔄 Busca respostas diretas para um Comentário específico.
     * (Método secundário, geralmente as respostas já são carregadas via
     * 'buscarComentariosPrincipaisPorProduto')
     *
     * @param comentarioPaiId O ID do comentário pai.
     * @return Uma lista de ComentarioResponse que são respostas diretas.
     */
    @Transactional(readOnly = true)
    public List<ComentarioResponse> buscarRespostasPorComentarioPai(Long comentarioPaiId) {
        List<Comentario> respostas = comentarioRepository.findByComentarioPaiId(comentarioPaiId);
        return comentarioMapper.toResponseList(respostas);
    }
}
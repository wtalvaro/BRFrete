package br.com.wta.frete.marketplace.service;

import br.com.wta.frete.core.entity.Pessoa;
import br.com.wta.frete.core.repository.PessoaRepository;
import br.com.wta.frete.marketplace.controller.dto.PerguntaProdutoRequest;
import br.com.wta.frete.marketplace.controller.dto.PerguntaProdutoResponse;
import br.com.wta.frete.marketplace.entity.PerguntaProduto;
import br.com.wta.frete.marketplace.entity.Produto;
import br.com.wta.frete.marketplace.repository.PerguntaProdutoRepository;
import br.com.wta.frete.marketplace.repository.ProdutoRepository;
import br.com.wta.frete.marketplace.service.mapper.PerguntaProdutoMapper;
import br.com.wta.frete.shared.exception.InvalidDataException;
import br.com.wta.frete.shared.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service dedicado à gestão de Perguntas e Respostas (Q&A) de produtos do
 * Marketplace.
 * Implementa a lógica de threading (pergunta principal vs. resposta).
 */
@Service
public class PerguntaProdutoService {

    private final PerguntaProdutoRepository perguntaProdutoRepository;
    private final PerguntaProdutoMapper perguntaProdutoMapper;
    private final ProdutoRepository produtoRepository;
    private final PessoaRepository pessoaRepository;

    public PerguntaProdutoService(PerguntaProdutoRepository perguntaProdutoRepository,
            PerguntaProdutoMapper perguntaProdutoMapper,
            ProdutoRepository produtoRepository,
            PessoaRepository pessoaRepository) {
        this.perguntaProdutoRepository = perguntaProdutoRepository;
        this.perguntaProdutoMapper = perguntaProdutoMapper;
        this.produtoRepository = produtoRepository;
        this.pessoaRepository = pessoaRepository;
    }

    /**
     * Documentação: Registra uma nova Pergunta (se perguntaPaiId for nulo)
     * ou uma Resposta (se perguntaPaiId for preenchido).
     *
     * @param request O DTO de requisição com os dados da pergunta/resposta.
     * @return O DTO de resposta da PerguntaProduto criada.
     */
    @SuppressWarnings("null")
    @Transactional
    public PerguntaProdutoResponse criarPerguntaOuResposta(PerguntaProdutoRequest request) {

        // 1. Valida Produto
        Produto produto = produtoRepository.findById(request.produtoId())
                .orElseThrow(
                        () -> new ResourceNotFoundException("Produto não encontrado com ID: " + request.produtoId()));

        // 2. Valida Autor
        Pessoa autor = pessoaRepository.findById(request.autorId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Pessoa (Autor) não encontrada com ID: " + request.autorId()));

        PerguntaProduto entidade = perguntaProdutoMapper.toEntity(request);
        entidade.setProduto(produto);
        entidade.setAutor(autor);
        entidade.setDataPublicacao(ZonedDateTime.now());

        // 3. Trata a Lógica de Threading (perguntaPai)
        if (request.perguntaPaiId() != null) {
            // Se for uma resposta, verifica e anexa à pergunta principal
            PerguntaProduto perguntaPai = perguntaProdutoRepository.findById(request.perguntaPaiId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Pergunta/Comentário pai não encontrado com ID: " + request.perguntaPaiId()));

            // Regra de Negócio: Não permitir resposta a uma resposta (simplificação para 2
            // níveis)
            if (perguntaPai.getPerguntaPai() != null) {
                // CORREÇÃO do Erro 1: Usando o construtor InvalidDataException(message,
                // reasonCode, field)
                throw new InvalidDataException(
                        "Não é permitido responder a um comentário que já é uma resposta.",
                        "RESPOSTA_A_RESPOSTA_NAO_PERMITIDA",
                        "perguntaPaiId");
            }

            entidade.setPerguntaPai(perguntaPai);
        } else {
            // Se perguntaPaiId é nulo, é uma nova pergunta principal.
            entidade.setPerguntaPai(null);
        }

        // 4. Salva e Mapeia
        PerguntaProduto salva = perguntaProdutoRepository.save(entidade);
        return perguntaProdutoMapper.toResponse(salva);
    }

    /**
     * Documentação: Busca todas as perguntas principais (perguntaPai nulo) e suas
     * respectivas respostas para um dado produto.
     *
     * @param produtoId O ID do Produto.
     * @return Uma lista de DTOs de PerguntaProdutoResponse.
     */
    @SuppressWarnings("null")
    @Transactional(readOnly = true)
    public List<PerguntaProdutoResponse> buscarPerguntasPorProduto(Integer produtoId) {

        // 1. Garante que o produto existe (opcional, mas boa prática)
        if (!produtoRepository.existsById(produtoId)) {
            throw new ResourceNotFoundException("Produto não encontrado com ID: " + produtoId);
        }

        // 2. Busca todas as perguntas principais (onde perguntaPai é nulo)
        // CORREÇÃO do Erro 2: Usando o nome completo do método definido no Repository
        List<PerguntaProduto> perguntasPrincipais = perguntaProdutoRepository
                .findByProdutoIdAndPerguntaPaiIsNullOrderByDataPublicacaoDesc(produtoId);

        // 3. Mapeia para DTOs (o Mapper se encarrega de mapear as respostas filhas)
        return perguntasPrincipais.stream()
                .map(perguntaProdutoMapper::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Documentação: Deleta uma pergunta ou resposta pelo ID.
     *
     * @param id O ID da PerguntaProduto a ser deletada.
     */
    @SuppressWarnings("null")
    @Transactional
    public void deletar(Long id) {
        if (!perguntaProdutoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Pergunta/Comentário não encontrado com ID: " + id);
        }
        perguntaProdutoRepository.deleteById(id);
    }
}
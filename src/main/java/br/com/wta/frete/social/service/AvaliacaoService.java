package br.com.wta.frete.social.service;

import br.com.wta.frete.core.repository.PessoaRepository;
import br.com.wta.frete.logistica.repository.OrdemServicoRepository;
import br.com.wta.frete.marketplace.repository.ProdutoRepository;
import br.com.wta.frete.social.controller.dto.AvaliacaoRequest;
import br.com.wta.frete.social.controller.dto.AvaliacaoResponse;
import br.com.wta.frete.social.entity.Avaliacao;
import br.com.wta.frete.social.repository.AvaliacaoRepository;
import br.com.wta.frete.social.service.mapper.AvaliacaoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * Serviço responsável pela lógica de negócio da entidade Avaliacao.
 * Implementa a validação da chave polimórfica (OrdemServico OU Produto).
 */
@Service
@RequiredArgsConstructor
public class AvaliacaoService {

    private final AvaliacaoRepository avaliacaoRepository;
    private final AvaliacaoMapper avaliacaoMapper;

    // Repositórios assumidos para validação de FKs e preenchimento da Entidade
    private final PessoaRepository pessoaRepository;
    private final OrdemServicoRepository ordemServicoRepository;
    private final ProdutoRepository produtoRepository;

    /**
     * ✍️ Registra uma nova avaliação no sistema.
     *
     * @param request DTO com os dados da avaliação e a chave polimórfica.
     * @return O DTO de resposta da avaliação salva.
     */
    @SuppressWarnings("null")
    @Transactional
    public AvaliacaoResponse registrarAvaliacao(AvaliacaoRequest request) {

        // A regra de XOR (exatamente um ID preenchido) já é validada no DTO
        // AvaliacaoRequest.java.
        // Se a validação falhar, uma exceção (IllegalArgumentException) será lançada
        // antes de chegar aqui.

        // 1. Converte o DTO para Entidade (sem os objetos de relacionamento
        // preenchidos)
        Avaliacao avaliacao = avaliacaoMapper.toEntity(request);

        // 2. Preenche os objetos de relacionamento (Entidades)

        // Pessoas (Avaliado e Avaliador)
        avaliacao.setAvaliador(pessoaRepository.getReferenceById(request.avaliadorId()));
        avaliacao.setAvaliado(pessoaRepository.getReferenceById(request.avaliadoId()));

        // Entidade Polimórfica (OrdemServico OU Produto)
        if (request.ordemServicoId() != null) {
            // Se o ID da Ordem de Serviço estiver presente, busca e anexa a referência
            avaliacao.setOrdemServico(ordemServicoRepository.getReferenceById(request.ordemServicoId()));
            // Nota: Se a Ordem de Serviço não existir, getReferenceById lançará uma
            // EntityNotFoundException (Lazy loading).
        } else if (request.produtoId() != null) {
            // Se o ID do Produto estiver presente, busca e anexa a referência
            avaliacao.setProduto(produtoRepository.getReferenceById(request.produtoId()));
        }

        // 3. Define o timestamp (Embora a Entidade já defina ZonedDateTime.now() por
        // default,
        // é uma prática comum garantir na camada Service/JPA lifecycle methods)
        avaliacao.setDataAvaliacao(ZonedDateTime.now());

        // 4. Salva a Avaliação
        Avaliacao salva = avaliacaoRepository.save(avaliacao);

        // 5. Converte e retorna o DTO de Resposta
        return avaliacaoMapper.toResponse(salva);
    }

    /**
     * 👥 Busca todas as avaliações recebidas por uma Pessoa (avaliado_id).
     */
    @Transactional(readOnly = true)
    public List<AvaliacaoResponse> buscarAvaliacoesRecebidas(Long avaliadoId) {
        List<Avaliacao> avaliacoes = avaliacaoRepository.findByAvaliadoId(avaliadoId);
        return avaliacaoMapper.toResponseList(avaliacoes);
    }

    /**
     * 🚚 Busca todas as avaliações feitas para uma Ordem de Serviço específica.
     */
    @Transactional(readOnly = true)
    public List<AvaliacaoResponse> buscarAvaliacoesPorOrdemServico(Long ordemServicoId) {
        List<Avaliacao> avaliacoes = avaliacaoRepository.findByOrdemServicoId(ordemServicoId);
        return avaliacaoMapper.toResponseList(avaliacoes);
    }

    /**
     * 📦 Busca todas as avaliações feitas para um Produto específico.
     */
    @Transactional(readOnly = true)
    public List<AvaliacaoResponse> buscarAvaliacoesPorProduto(Integer produtoId) {
        List<Avaliacao> avaliacoes = avaliacaoRepository.findByProdutoId(produtoId);
        return avaliacaoMapper.toResponseList(avaliacoes);
    }
}
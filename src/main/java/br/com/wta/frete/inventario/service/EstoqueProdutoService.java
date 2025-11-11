package br.com.wta.frete.inventario.service;

import br.com.wta.frete.inventario.controller.dto.EstoqueProdutoRequest;
import br.com.wta.frete.inventario.controller.dto.EstoqueProdutoResponse;
import br.com.wta.frete.inventario.entity.EstoqueProduto;
import br.com.wta.frete.inventario.repository.EstoqueProdutoRepository;
import br.com.wta.frete.inventario.service.mapper.EstoqueProdutoMapper;
import br.com.wta.frete.marketplace.entity.Produto;
import br.com.wta.frete.marketplace.repository.ProdutoRepository;
import br.com.wta.frete.shared.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.Optional;

/**
 * Service dedicado à lógica de gerenciamento de estoque para produtos do
 * Marketplace.
 * Trata a criação e atualização de EstoqueProduto, que utiliza chave derivada
 * (@MapsId)
 * do Produto. Este serviço é essencial para ser chamado pelo ProdutoService
 * após a
 * criação de um novo Produto.
 */
@Service
public class EstoqueProdutoService {

    private final EstoqueProdutoRepository estoqueProdutoRepository;
    // Necessário para buscar o objeto Produto que será mapeado para a chave
    private final ProdutoRepository produtoRepository;
    private final EstoqueProdutoMapper estoqueProdutoMapper;

    public EstoqueProdutoService(EstoqueProdutoRepository estoqueProdutoRepository,
            ProdutoRepository produtoRepository,
            EstoqueProdutoMapper estoqueProdutoMapper) {
        this.estoqueProdutoRepository = estoqueProdutoRepository;
        this.produtoRepository = produtoRepository;
        this.estoqueProdutoMapper = estoqueProdutoMapper;
    }

    /**
     * Documentação: Cria um novo registro de estoque para um Produto, ou o atualiza
     * se já existir.
     * Trata corretamente a lógica da chave derivada (@MapsId) para criar o registro
     * de estoque
     * com o ID do Produto.
     *
     * @param produtoId O ID do Produto (que será a chave primária do
     *                  EstoqueProduto).
     * @param request   O DTO com a quantidade, ponto de reposição e localização.
     * @return O DTO de resposta com os dados do EstoqueProduto.
     * @throws ResourceNotFoundException se o Produto Base não for encontrado.
     */
    @SuppressWarnings("null")
    @Transactional
    public EstoqueProdutoResponse criarOuAtualizarEstoque(Integer produtoId, EstoqueProdutoRequest request) {

        // 1. Valida se o Produto Base existe
        Produto produto = produtoRepository.findById(produtoId)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado com ID: " + produtoId));

        // 2. Tenta encontrar o registro de estoque existente
        Optional<EstoqueProduto> estoqueExistente = estoqueProdutoRepository.findById(produtoId);

        EstoqueProduto estoque;

        if (estoqueExistente.isPresent()) {
            // Caminho de ATUALIZAÇÃO
            estoque = estoqueExistente.get();
            // Aplica as atualizações via Mapper
            estoqueProdutoMapper.updateEntityFromRequest(request, estoque);

            // Atualiza a data
            estoque.setUltimaAtualizacao(ZonedDateTime.now());

            // Salva o objeto gerenciado
            estoque = estoqueProdutoRepository.save(estoque);

        } else {
            // Caminho de CRIAÇÃO
            // Cria a entidade a partir do Request
            EstoqueProduto novoEstoque = estoqueProdutoMapper.toEntity(request);

            // CORREÇÃO CRÍTICA para @MapsId:
            // 1. Vincula o objeto Produto (relacionamento @OneToOne)
            // 2. Define o ID explicitamente (necessário para o @MapsId em criação)
            novoEstoque.setProduto(produto);
            novoEstoque.setProdutoId(produtoId);
            novoEstoque.setUltimaAtualizacao(ZonedDateTime.now());

            // Salva o novo registro (INSERT)
            estoque = estoqueProdutoRepository.save(novoEstoque);
        }

        // 3. Mapeia e retorna a resposta
        return estoqueProdutoMapper.toResponse(estoque);
    }

    /**
     * Documentação: Busca a entidade EstoqueProduto pelo ID do Produto.
     *
     * @param produtoId O ID do Produto.
     * @return A Entidade EstoqueProduto.
     * @throws ResourceNotFoundException se o EstoqueProduto não for encontrado.
     */
    @SuppressWarnings("null")
    @Transactional(readOnly = true)
    public EstoqueProduto buscarEntidadePorProdutoId(Integer produtoId) {
        return estoqueProdutoRepository.findById(produtoId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Estoque de Produto não encontrado para o ID do Produto: " + produtoId));
    }
}
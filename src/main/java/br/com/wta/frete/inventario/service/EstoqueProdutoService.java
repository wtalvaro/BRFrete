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

@Service
public class EstoqueProdutoService {

    private final EstoqueProdutoRepository estoqueProdutoRepository;
    private final ProdutoRepository produtoRepository;
    private final EstoqueProdutoMapper estoqueProdutoMapper;

    public EstoqueProdutoService(EstoqueProdutoRepository estoqueProdutoRepository,
            ProdutoRepository produtoRepository,
            EstoqueProdutoMapper estoqueProdutoMapper) {
        this.estoqueProdutoRepository = estoqueProdutoRepository;
        this.produtoRepository = produtoRepository;
        this.estoqueProdutoMapper = estoqueProdutoMapper;
    }

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
            estoqueProdutoMapper.updateEntityFromRequest(request, estoque);
            estoque.setUltimaAtualizacao(ZonedDateTime.now());
            estoque = estoqueProdutoRepository.save(estoque);
        } else {
            // Caminho de CRIAÇÃO
            EstoqueProduto novoEstoque = estoqueProdutoMapper.toEntity(request);
            novoEstoque.setProduto(produto);
            novoEstoque.setProdutoId(produtoId);
            novoEstoque.setUltimaAtualizacao(ZonedDateTime.now());
            estoque = estoqueProdutoRepository.save(novoEstoque);
        }

        // 3. Mapeia e retorna a resposta
        return estoqueProdutoMapper.toResponse(estoque);
    }

    /**
     * Documentação: Busca a entidade EstoqueProduto pelo ID do Produto.
     */
    @SuppressWarnings("null")
    @Transactional(readOnly = true)
    public EstoqueProduto buscarEntidadePorProdutoId(Integer produtoId) {
        return estoqueProdutoRepository.findById(produtoId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Estoque de Produto não encontrado para o ID do Produto: " + produtoId));
    }

    // NOVO MÉTODO: Retorna o DTO, utilizado pelo Controller
    @Transactional(readOnly = true)
    public EstoqueProdutoResponse buscarPorId(Integer produtoId) {
        EstoqueProduto estoque = buscarEntidadePorProdutoId(produtoId);
        return estoqueProdutoMapper.toResponse(estoque);
    }

    // NOVO MÉTODO: Deleta o estoque
    @SuppressWarnings("null")
    @Transactional
    public void deletarEstoque(Integer produtoId) {
        if (!estoqueProdutoRepository.existsById(produtoId)) {
            throw new ResourceNotFoundException(
                    "Estoque de Produto não encontrado com ID: " + produtoId + " para exclusão.");
        }
        estoqueProdutoRepository.deleteById(produtoId);
    }
}
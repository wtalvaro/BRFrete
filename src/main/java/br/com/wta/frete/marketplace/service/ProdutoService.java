package br.com.wta.frete.marketplace.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.wta.frete.colaboradores.entity.Lojista;
import br.com.wta.frete.colaboradores.repository.LojistaRepository;
import br.com.wta.frete.inventario.controller.dto.EstoqueProdutoRequest;
import br.com.wta.frete.inventario.service.EstoqueProdutoService;
import br.com.wta.frete.marketplace.controller.dto.ProdutoRequest;
import br.com.wta.frete.marketplace.controller.dto.ProdutoResponse;
import br.com.wta.frete.marketplace.entity.Categoria;
import br.com.wta.frete.marketplace.entity.Produto;
import br.com.wta.frete.marketplace.repository.ProdutoRepository;
import br.com.wta.frete.marketplace.service.mapper.ProdutoMapper;
import br.com.wta.frete.shared.exception.InvalidDataException;
import br.com.wta.frete.shared.exception.ResourceNotFoundException;

@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final ProdutoMapper produtoMapper;
    private final LojistaRepository lojistaRepository;
    private final CategoriaService categoriaService;
    private final EstoqueProdutoService estoqueProdutoService;

    public ProdutoService(ProdutoRepository produtoRepository,
            ProdutoMapper produtoMapper,
            LojistaRepository lojistaRepository,
            CategoriaService categoriaService,
            EstoqueProdutoService estoqueProdutoService) {
        this.produtoRepository = produtoRepository;
        this.produtoMapper = produtoMapper;
        this.lojistaRepository = lojistaRepository;
        this.categoriaService = categoriaService;
        this.estoqueProdutoService = estoqueProdutoService;
    }

    @Transactional
    public ProdutoResponse criar(ProdutoRequest request) {

        // 1. Validações de Entidades de Domínio
        Lojista lojista = lojistaRepository.findByPessoaId(request.lojistaPessoaId()) // CORRIGIDO
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Lojista não encontrado com ID: " + request.lojistaPessoaId())); // CORRIGIDO

        Categoria categoria = categoriaService.buscarEntidadePorId(request.categoriaId());

        // 2. Validação de Unicidade: SKU deve ser único por Vendedor
        if (produtoRepository.findByVendedorPessoaIdAndSku(request.lojistaPessoaId(), request.sku()).isPresent()) { // CORRIGIDO
            throw new InvalidDataException(
                    "O SKU '" + request.sku() + "' já existe para este lojista.",
                    "SKU_DUPLICADO",
                    "sku");
        }

        // 3. Mapeamento e Persistência do Produto
        Produto produto = produtoMapper.toEntity(request);
        produto.setVendedor(lojista); // CORRIGIDO: O setter deve ser setVendedor
        produto.setCategoria(categoria);

        Produto produtoSalvo = produtoRepository.save(produto);

        // 4. CRIAÇÃO DO ESTOQUE INICIAL (INTEGRAÇÃO COM OUTRO MÓDULO)

        EstoqueProdutoRequest estoqueRequest = new EstoqueProdutoRequest(
                produtoSalvo.getId(), // CORRIGIDO: usando getId()
                request.quantidade(), // CORRIGIDO
                request.pontoReposicao(), // CORRIGIDO
                request.localizacao() // CORRIGIDO
        );

        estoqueProdutoService.criarOuAtualizarEstoque(produtoSalvo.getId(), estoqueRequest); // CORRIGIDO

        // 5. Retorno
        return produtoMapper.toResponse(produtoSalvo);
    }

    // ... (buscarPorId, buscarTodos permanecem iguais, confiando no getId())

    @SuppressWarnings("null")
    @Transactional(readOnly = true)
    public ProdutoResponse buscarPorId(Integer id) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado com ID: " + id));
        return produtoMapper.toResponse(produto);
    }

    @SuppressWarnings("null")
    @Transactional(readOnly = true)
    public Page<ProdutoResponse> buscarTodos(Pageable pageable) {
        return produtoRepository.findAll(pageable)
                .map(produtoMapper::toResponse);
    }

    @Transactional
    public ProdutoResponse atualizar(Integer id, ProdutoRequest request) {
        @SuppressWarnings("null")
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado com ID: " + id));

        // Validação de unicidade de SKU em caso de alteração
        if (!produto.getSku().equals(request.sku())) {
            // CORRIGIDO: Usando getId() e acessor correto do DTO
            if (produtoRepository.findByVendedorPessoaIdAndSku(produto.getVendedor().getPessoaId(), request.sku())
                    .isPresent()) {
                throw new InvalidDataException(
                        "O SKU '" + request.sku() + "' já existe para este lojista.",
                        "SKU_DUPLICADO",
                        "sku");
            }
        }

        Categoria novaCategoria = categoriaService.buscarEntidadePorId(request.categoriaId());

        // CORRIGIDO: Chamando o método do Mapper com o nome corrigido
        // (updateProdutoFromRequest)
        produtoMapper.updateEntity(request, produto);
        produto.setCategoria(novaCategoria);

        Produto produtoAtualizado = produtoRepository.save(produto);

        // Atualização da quantidade no Estoque
        EstoqueProdutoRequest estoqueRequest = new EstoqueProdutoRequest(
                produtoAtualizado.getId(), // CORRIGIDO
                request.quantidade(), // CORRIGIDO
                request.pontoReposicao(), // CORRIGIDO
                request.localizacao() // CORRIGIDO
        );

        estoqueProdutoService.criarOuAtualizarEstoque(produtoAtualizado.getId(), estoqueRequest); // CORRIGIDO

        return produtoMapper.toResponse(produtoAtualizado);
    }

    @SuppressWarnings("null")
    @Transactional
    public void deletar(Integer id) {
        if (!produtoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Produto não encontrado com ID: " + id);
        }
        produtoRepository.deleteById(id);
    }
}
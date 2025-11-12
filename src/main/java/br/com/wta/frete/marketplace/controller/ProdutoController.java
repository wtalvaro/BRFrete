package br.com.wta.frete.marketplace.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.com.wta.frete.marketplace.controller.dto.ProdutoRequest;
import br.com.wta.frete.marketplace.controller.dto.ProdutoResponse;
import br.com.wta.frete.marketplace.service.ProdutoService;
import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

/**
 * Controller REST para gerenciar a entidade Produto (marketplace.produtos).
 */
@RestController
@RequestMapping("/api/v1/marketplace/produtos")
@RequiredArgsConstructor
public class ProdutoController {

    private final ProdutoService produtoService;

    /**
     * POST /v1/marketplace/produtos
     * Cria um novo produto, validando o Lojista, a Categoria e a unicidade do SKU.
     * * @param request DTO com os dados do novo produto.
     * 
     * @return 201 Created com o produto e seu estoque inicial.
     */
    @PostMapping
    public ResponseEntity<ProdutoResponse> criarProduto(
            @RequestBody @Valid ProdutoRequest request) {

        ProdutoResponse response = produtoService.criar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * GET /v1/marketplace/produtos/{id}
     * Busca um produto pelo ID.
     * * @param id O ID do produto.
     * 
     * @return 200 OK com os dados do produto.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProdutoResponse> buscarProdutoPorId(
            @PathVariable Integer id) {

        ProdutoResponse response = produtoService.buscarPorId(id);
        return ResponseEntity.ok(response);
    }

    /**
     * GET /v1/marketplace/produtos?page=...&size=...
     * Busca todos os produtos com paginação.
     * * @param pageable Parâmetros de paginação do Spring.
     * 
     * @return 200 OK com a página de produtos.
     */
    @GetMapping
    public ResponseEntity<Page<ProdutoResponse>> buscarTodosProdutos(
            Pageable pageable) {

        Page<ProdutoResponse> response = produtoService.buscarTodos(pageable);
        return ResponseEntity.ok(response);
    }

    /**
     * PUT /v1/marketplace/produtos/{id}
     * Atualiza um produto existente.
     * * @param id O ID do produto a ser atualizado.
     * 
     * @param request DTO com os novos dados.
     * @return 200 OK com o produto atualizado.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProdutoResponse> atualizarProduto(
            @PathVariable Integer id,
            @RequestBody @Valid ProdutoRequest request) {

        ProdutoResponse response = produtoService.atualizar(id, request);
        return ResponseEntity.ok(response);
    }

    /**
     * DELETE /v1/marketplace/produtos/{id}
     * Deleta um produto pelo ID.
     * * @param id O ID do produto.
     * 
     * @return 204 No Content.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarProduto(
            @PathVariable Integer id) {

        produtoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
package br.com.wta.frete.inventario.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.wta.frete.inventario.controller.dto.EstoqueProdutoRequest;
import br.com.wta.frete.inventario.controller.dto.EstoqueProdutoResponse;
// REMOVIDO: import br.com.wta.frete.inventario.entity.EstoqueProduto; (Não é mais necessário no Controller)
import br.com.wta.frete.inventario.service.EstoqueProdutoService;
import jakarta.validation.Valid;

/**
 * Controller REST para gerenciar o Estoque de Produtos do Marketplace
 * (inventario.estoque_produto).
 * O recurso é identificado pelo ID do Produto (produtoId), que também é a chave
 * primária.
 */
@RestController
@RequestMapping("/api/v1/inventario/produto")
public class EstoqueProdutoController {

    private final EstoqueProdutoService estoqueProdutoService;

    // Injeção de dependência via construtor
    public EstoqueProdutoController(EstoqueProdutoService estoqueProdutoService) {
        this.estoqueProdutoService = estoqueProdutoService;
    }

    /**
     * Endpoint: POST/PUT /api/v1/inventario/produto/{produtoId}
     * Documentação: Cria ou Atualiza o registro de estoque de um Produto.
     * Usa POST para criação inicial e PUT para atualização completa.
     *
     * @param produtoId O ID do Produto que terá seu estoque criado/atualizado.
     * @param request   DTO com os dados do estoque (quantidade, localização, ponto
     *                  de reposição).
     * @return O item de estoque criado/atualizado e status 201 (CREATED).
     */
    @PostMapping("/{produtoId}")
    @PutMapping("/{produtoId}")
    public ResponseEntity<EstoqueProdutoResponse> criarOuAtualizar(
            @PathVariable Integer produtoId,
            @Valid @RequestBody EstoqueProdutoRequest request) {

        // Chama o método do Service
        EstoqueProdutoResponse response = estoqueProdutoService.criarOuAtualizarEstoque(produtoId, request);

        // Retorna 201 (Created), pois representa a conclusão bem-sucedida da operação.
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Endpoint: GET /api/v1/inventario/produto/{produtoId}
     * Documentação: Busca o registro de estoque de um Produto pelo ID.
     *
     * @param produtoId O ID do Produto/Estoque.
     * @return O DTO do estoque do produto.
     */
    @GetMapping("/{produtoId}")
    public ResponseEntity<EstoqueProdutoResponse> buscarPorId(@PathVariable Integer produtoId) {

        // O Service já retorna o DTO de Resposta (após as correções no Service)
        EstoqueProdutoResponse response = estoqueProdutoService.buscarPorId(produtoId);

        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint: DELETE /api/v1/inventario/produto/{produtoId}
     * Documentação: Deleta o registro de estoque de um Produto pelo ID.
     *
     * @param produtoId O ID do Produto/Estoque a ser deletado.
     * @return Status 204 (No Content).
     */
    @DeleteMapping("/{produtoId}")
    public ResponseEntity<Void> deletar(@PathVariable Integer produtoId) {
        // O Service já tem o método deletarEstoque(produtoId) (após as correções no
        // Service)
        estoqueProdutoService.deletarEstoque(produtoId);
        return ResponseEntity.noContent().build();
    }
}
package br.com.wta.frete.marketplace.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.com.wta.frete.marketplace.controller.dto.PerguntaProdutoRequest;
import br.com.wta.frete.marketplace.controller.dto.PerguntaProdutoResponse;
import br.com.wta.frete.marketplace.service.PerguntaProdutoService;
import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

/**
 * Controller REST para gerenciar a funcionalidade de Perguntas e Respostas
 * (Q&A)
 * da entidade Produto (marketplace.perguntas_produto).
 */
@RestController
@RequestMapping("/api/v1/marketplace/perguntas")
@RequiredArgsConstructor
public class PerguntaProdutoController {

    private final PerguntaProdutoService perguntaProdutoService;

    /**
     * POST /v1/marketplace/perguntas
     * Cria uma nova pergunta (perguntaPaiId nulo) ou uma resposta (perguntaPaiId
     * preenchido).
     * * @param request DTO com os dados da pergunta/resposta.
     * 
     * @return 201 Created com a PerguntaProduto criada.
     */
    @PostMapping
    public ResponseEntity<PerguntaProdutoResponse> criarPerguntaOuResposta(
            @RequestBody @Valid PerguntaProdutoRequest request) {

        PerguntaProdutoResponse response = perguntaProdutoService.criarPerguntaOuResposta(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * GET /v1/marketplace/perguntas/produto/{produtoId}
     * Busca todas as perguntas principais (e suas respostas) para um produto.
     * * @param produtoId O ID do produto.
     * 
     * @return 200 OK com a lista de perguntas/respostas.
     */
    @GetMapping("/produto/{produtoId}")
    public ResponseEntity<List<PerguntaProdutoResponse>> buscarPerguntasPorProduto(
            @PathVariable Integer produtoId) {

        List<PerguntaProdutoResponse> response = perguntaProdutoService.buscarPerguntasPorProduto(produtoId);
        return ResponseEntity.ok(response);
    }

    /**
     * DELETE /v1/marketplace/perguntas/{id}
     * Deleta uma pergunta ou resposta pelo ID.
     * * @param id O ID da PerguntaProduto.
     * 
     * @return 204 No Content.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarPerguntaOuResposta(
            @PathVariable Long id) {

        perguntaProdutoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
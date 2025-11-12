package br.com.wta.frete.marketplace.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.com.wta.frete.marketplace.controller.dto.CategoriaRequest;
import br.com.wta.frete.marketplace.controller.dto.CategoriaResponse;
import br.com.wta.frete.marketplace.service.CategoriaService;
import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

/**
 * Controller REST para gerenciar a entidade mestra Categoria
 * (marketplace.categorias).
 */
@RestController
@RequestMapping("/api/v1/marketplace/categorias")
@RequiredArgsConstructor
public class CategoriaController {

    private final CategoriaService categoriaService;

    /**
     * POST /v1/marketplace/categorias
     * Cria uma nova categoria.
     * * @param request DTO com os dados da nova categoria.
     * 
     * @return 201 Created com a categoria criada.
     */
    @PostMapping
    public ResponseEntity<CategoriaResponse> criarCategoria(
            @RequestBody @Valid CategoriaRequest request) {

        CategoriaResponse response = categoriaService.criar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * GET /v1/marketplace/categorias/{id}
     * Busca uma categoria pelo ID.
     * * @param id O ID da categoria.
     * 
     * @return 200 OK com os dados da categoria.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CategoriaResponse> buscarCategoriaPorId(
            @PathVariable Integer id) {

        CategoriaResponse response = categoriaService.buscarPorId(id);
        return ResponseEntity.ok(response);
    }

    /**
     * GET /v1/marketplace/categorias
     * Busca todas as categorias.
     * * @return 200 OK com a lista de categorias.
     */
    @GetMapping
    public ResponseEntity<List<CategoriaResponse>> buscarTodasCategorias() {
        List<CategoriaResponse> response = categoriaService.buscarTodos();
        return ResponseEntity.ok(response);
    }

    /**
     * PUT /v1/marketplace/categorias/{id}
     * Atualiza uma categoria existente.
     * * @param id O ID da categoria a ser atualizada.
     * 
     * @param request DTO com os novos dados.
     * @return 200 OK com a categoria atualizada.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CategoriaResponse> atualizarCategoria(
            @PathVariable Integer id,
            @RequestBody @Valid CategoriaRequest request) {

        CategoriaResponse response = categoriaService.atualizar(id, request);
        return ResponseEntity.ok(response);
    }

    /**
     * DELETE /v1/marketplace/categorias/{id}
     * Deleta uma categoria pelo ID.
     * * @param id O ID da categoria.
     * 
     * @return 204 No Content.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarCategoria(
            @PathVariable Integer id) {

        categoriaService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
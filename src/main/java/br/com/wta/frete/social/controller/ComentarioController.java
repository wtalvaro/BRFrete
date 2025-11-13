package br.com.wta.frete.social.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.wta.frete.social.controller.dto.ComentarioRequest;
import br.com.wta.frete.social.controller.dto.ComentarioResponse;
import br.com.wta.frete.social.service.ComentarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * Controller para gerenciar a criação e recuperação de Comentários e Respostas.
 * Mapeamento: /api/v1/social/comentarios
 */
@RestController
@RequestMapping("/api/v1/social/comentarios") // <-- MUDANÇA AQUI
@RequiredArgsConstructor
public class ComentarioController {

    private final ComentarioService comentarioService;

    /**
     * POST /api/v1/social/comentarios
     */
    @PostMapping
    public ResponseEntity<ComentarioResponse> postarComentario(@RequestBody @Valid ComentarioRequest request) {
        ComentarioResponse response = comentarioService.postarComentario(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * GET /api/v1/social/comentarios/produto/{produtoId}
     */
    @GetMapping("/produto/{produtoId}")
    public ResponseEntity<List<ComentarioResponse>> buscarComentariosPorProduto(@PathVariable Integer produtoId) {
        List<ComentarioResponse> response = comentarioService.buscarComentariosPrincipaisPorProduto(produtoId);
        return ResponseEntity.ok(response);
    }
}
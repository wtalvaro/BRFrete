package br.com.wta.frete.social.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.wta.frete.social.controller.dto.SeguidorRequest;
import br.com.wta.frete.social.controller.dto.SeguidorResponse;
import br.com.wta.frete.social.service.SeguidorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * Controller para gerenciar o relacionamento social de Seguir/Deixar de Seguir.
 * Mapeamento: /api/v1/social/seguidores
 */
@RestController
@RequestMapping("/api/v1/social/seguidores") // <-- MUDANÇA AQUI
@RequiredArgsConstructor
public class SeguidorController {

    private final SeguidorService seguidorService;

    /**
     * POST /api/v1/social/seguidores
     */
    @PostMapping
    public ResponseEntity<SeguidorResponse> seguirPessoa(@RequestBody @Valid SeguidorRequest request) {
        SeguidorResponse response = seguidorService.seguirPessoa(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * DELETE /api/v1/social/seguidores/{seguidorId}/seguindo/{seguidoId}
     */
    @DeleteMapping("/{seguidorId}/seguindo/{seguidoId}")
    public ResponseEntity<Void> deixarDeSeguir(@PathVariable Long seguidorId, @PathVariable Long seguidoId) {
        seguidorService.deixarDeSeguir(seguidorId, seguidoId);
        return ResponseEntity.noContent().build();
    }

    /**
     * GET /api/v1/social/seguidores/{seguidorPessoaId}/seguidos
     */
    @GetMapping("/{seguidorPessoaId}/seguidos")
    public ResponseEntity<List<SeguidorResponse>> buscarSeguidos(@PathVariable Long seguidorPessoaId) {
        List<SeguidorResponse> response = seguidorService.buscarSeguidosPorPessoa(seguidorPessoaId);
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/v1/social/seguidores/{seguidoPessoaId}/seguidores
     */
    @GetMapping("/{seguidoPessoaId}/seguidores")
    public ResponseEntity<List<SeguidorResponse>> buscarSeguidores(@PathVariable Long seguidoPessoaId) {
        List<SeguidorResponse> response = seguidorService.buscarSeguidoresDePessoa(seguidoPessoaId);
        return ResponseEntity.ok(response);
    }
}
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

import br.com.wta.frete.social.controller.dto.AvaliacaoRequest;
import br.com.wta.frete.social.controller.dto.AvaliacaoResponse;
import br.com.wta.frete.social.service.AvaliacaoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * Controller para gerenciar a criação e recuperação de Avaliações.
 * Mapeamento: /api/v1/social/avaliacoes
 */
@RestController
@RequestMapping("/api/v1/social/avaliacoes") // <-- MUDANÇA AQUI
@RequiredArgsConstructor
public class AvaliacaoController {

    private final AvaliacaoService avaliacaoService;

    /**
     * POST /api/v1/social/avaliacoes
     */
    @PostMapping
    public ResponseEntity<AvaliacaoResponse> registrarAvaliacao(@RequestBody @Valid AvaliacaoRequest request) {
        AvaliacaoResponse response = avaliacaoService.registrarAvaliacao(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * GET /api/v1/social/avaliacoes/avaliado/{avaliadoId}
     */
    @GetMapping("/avaliado/{avaliadoId}")
    public ResponseEntity<List<AvaliacaoResponse>> buscarAvaliacoesRecebidas(@PathVariable Long avaliadoId) {
        List<AvaliacaoResponse> response = avaliacaoService.buscarAvaliacoesRecebidas(avaliadoId);
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/v1/social/avaliacoes/ordem-servico/{ordemServicoId}
     */
    @GetMapping("/ordem-servico/{ordemServicoId}")
    public ResponseEntity<List<AvaliacaoResponse>> buscarAvaliacoesPorOrdemServico(@PathVariable Long ordemServicoId) {
        List<AvaliacaoResponse> response = avaliacaoService.buscarAvaliacoesPorOrdemServico(ordemServicoId);
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/v1/social/avaliacoes/produto/{produtoId}
     */
    @GetMapping("/produto/{produtoId}")
    public ResponseEntity<List<AvaliacaoResponse>> buscarAvaliacoesPorProduto(@PathVariable Integer produtoId) {
        List<AvaliacaoResponse> response = avaliacaoService.buscarAvaliacoesPorProduto(produtoId);
        return ResponseEntity.ok(response);
    }
}
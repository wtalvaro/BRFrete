package br.com.wta.frete.colaboradores.controller;

import java.util.List;

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

import br.com.wta.frete.colaboradores.controller.dto.MetricaTransportadorFreteRequest;
import br.com.wta.frete.colaboradores.controller.dto.MetricaTransportadorFreteResponse;
import br.com.wta.frete.logistica.service.MetricaTransportadorFreteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * Controller REST para que o Transportador gerencie seus parâmetros de
 * precificação customizados (Métricas de Frete).
 * Endpoints: /api/v1/colaboradores/metricas-frete
 */
@RestController
@RequestMapping("/api/v1/colaboradores/metricas-frete")
@RequiredArgsConstructor
public class MetricaTransportadorFreteController {

    // Injeta o serviço responsável pela lógica de CRUD e cálculo.
    private final MetricaTransportadorFreteService metricaService;

    /**
     * POST /api/v1/colaboradores/metricas-frete
     * Cria uma nova métrica de precificação customizada.
     *
     * @param request DTO de entrada com os parâmetros de custo.
     * @return 201 Created e a métrica criada.
     */
    @PostMapping
    public ResponseEntity<MetricaTransportadorFreteResponse> criarMetrica(
            @Valid @RequestBody MetricaTransportadorFreteRequest request) {

        // O Service trata a validação, unicidade e persistência.
        MetricaTransportadorFreteResponse response = metricaService.criarMetrica(request);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * GET /api/v1/colaboradores/metricas-frete/{id}
     * Busca uma métrica específica pelo seu ID (PK).
     *
     * @param id ID (metricaId) da métrica.
     * @return 200 OK e o corpo da métrica.
     */
    @GetMapping("/{id}")
    public ResponseEntity<MetricaTransportadorFreteResponse> buscarPorId(@PathVariable Long id) {
        MetricaTransportadorFreteResponse response = metricaService.buscarPorId(id);
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/v1/colaboradores/metricas-frete/transportador/{transportadorId}
     * Lista todas as métricas pertencentes a um transportador específico.
     *
     * @param transportadorId ID da Pessoa/Transportador.
     * @return 200 OK e a lista de métricas.
     */
    @GetMapping("/transportador/{transportadorId}")
    public ResponseEntity<List<MetricaTransportadorFreteResponse>> buscarPorTransportador(
            @PathVariable Long transportadorId) {
        List<MetricaTransportadorFreteResponse> response = metricaService.buscarPorTransportador(transportadorId);
        return ResponseEntity.ok(response);
    }

    /**
     * PUT /api/v1/colaboradores/metricas-frete/{id}
     * Atualiza completamente os dados de uma métrica existente.
     *
     * @param id      ID (metricaId) da métrica a ser atualizada.
     * @param request Os novos dados da métrica.
     * @return 200 OK e a métrica atualizada.
     */
    @PutMapping("/{id}")
    public ResponseEntity<MetricaTransportadorFreteResponse> atualizarMetrica(
            @PathVariable Long id,
            @Valid @RequestBody MetricaTransportadorFreteRequest request) {

        MetricaTransportadorFreteResponse response = metricaService.atualizarMetrica(id, request);
        return ResponseEntity.ok(response);
    }

    /**
     * DELETE /api/v1/colaboradores/metricas-frete/{id}
     * Remove uma métrica de precificação do sistema.
     *
     * @param id ID (metricaId) da métrica a ser removida.
     * @return 204 No Content.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarMetrica(@PathVariable Long id) {
        metricaService.deletarMetrica(id);
        return ResponseEntity.noContent().build();
    }
}
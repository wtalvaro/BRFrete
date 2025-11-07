package br.com.wta.frete.logistica.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.wta.frete.logistica.controller.dto.OrdemServicoRequest;
import br.com.wta.frete.logistica.controller.dto.OrdemServicoResponse;
import br.com.wta.frete.logistica.service.OrdemServicoService;
import jakarta.validation.Valid;

/**
 * Controller REST para gerenciar Ordens de Serviço (Solicitações de Frete).
 * Endpoints: /api/v1/logistica/ordens-servico
 *
 * Responsável por:
 * 1. Receber requisições HTTP e validar o DTO de entrada.
 * 2. Chamar a lógica de negócio encapsulada no OrdemServicoService.
 * 3. Formatar e retornar a resposta HTTP apropriada.
 */
@RestController
@RequestMapping("/api/v1/logistica/ordens-servico")
public class OrdemServicoController {

    private final OrdemServicoService ordemServicoService;

    // Injeção de dependência via construtor (melhor prática do Spring)
    public OrdemServicoController(OrdemServicoService ordemServicoService) {
        this.ordemServicoService = ordemServicoService;
    }

    /**
     * POST /api/v1/logistica/ordens-servico
     * Cria uma nova Ordem de Serviço (solicitação de frete).
     *
     * @param request DTO de entrada com os dados da OS.
     * @return 201 Created e o corpo da OS criada.
     */
    @PostMapping
    public ResponseEntity<OrdemServicoResponse> criarOrdemServico(@Valid @RequestBody OrdemServicoRequest request) {
        // A anotação @Valid utiliza as restrições definidas no OrdemServicoRequest.java
        OrdemServicoResponse response = ordemServicoService.criarOrdemServico(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * GET /api/v1/logistica/ordens-servico/{id}
     * Busca uma Ordem de Serviço específica pelo seu ID.
     *
     * @param id ID da Ordem de Serviço.
     * @return 200 OK e o corpo da OS.
     */
    @GetMapping("/{id}")
    public ResponseEntity<OrdemServicoResponse> buscarPorId(@PathVariable Long id) {
        OrdemServicoResponse response = ordemServicoService.buscarPorId(id);
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/v1/logistica/ordens-servico
     * Lista todas as Ordens de Serviço cadastradas no sistema.
     *
     * @return 200 OK e a lista de OSs.
     */
    @GetMapping
    public ResponseEntity<List<OrdemServicoResponse>> buscarTodas() {
        List<OrdemServicoResponse> response = ordemServicoService.buscarTodas();
        return ResponseEntity.ok(response);
    }
}
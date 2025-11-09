package br.com.wta.frete.logistica.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.wta.frete.logistica.controller.dto.LanceRequest;
import br.com.wta.frete.logistica.controller.dto.LanceResponse;
import br.com.wta.frete.logistica.service.LanceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * Controller responsável por receber as requisições de Lances dos
 * Transportadores.
 * Endpoint: POST /logistica/fretes/{freteId}/lances
 */
@RestController
@RequestMapping("/logistica/fretes/{freteId}/lances")
@RequiredArgsConstructor
public class LanceController {

    private final LanceService lanceService;

    /**
     * Recebe e processa a submissão/atualização de um lance para um frete.
     * Aplica a lógica de Leilão Reverso Dinâmico e as regras anti-spam.
     *
     * @param freteId O ID do Frete (Ordem de Serviço).
     * @param request Os dados do lance (Transportador ID e Valor).
     * @return O Lance recém-criado ou atualizado (Status 201 Created).
     */
    @PostMapping
    public ResponseEntity<LanceResponse> criarLance(@PathVariable Long freteId,
            @Valid @RequestBody LanceRequest request) {

        LanceResponse response = lanceService.criarLance(freteId, request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
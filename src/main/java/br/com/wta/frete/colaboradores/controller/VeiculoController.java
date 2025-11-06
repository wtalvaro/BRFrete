package br.com.wta.frete.colaboradores.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.wta.frete.colaboradores.controller.dto.VeiculoRequest;
import br.com.wta.frete.colaboradores.controller.dto.VeiculoResponse;
import br.com.wta.frete.colaboradores.service.VeiculoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * Controller responsável por receber requisições HTTP para a gestão de Veículos
 * de Transportadores.
 * Endpoint base: /api/v1/transportadores/veiculos
 */
@RestController
@RequestMapping("/api/v1/transportadores/veiculos")
@RequiredArgsConstructor
public class VeiculoController {

    private final VeiculoService veiculoService;

    /**
     * Endpoint para o cadastro de um novo veículo para um Transportador.
     *
     * @param request O DTO VeiculoRequest contendo os dados do novo veículo.
     * @return ResponseEntity com o VeiculoResponse (status 201 Created).
     */
    @PostMapping
    public ResponseEntity<VeiculoResponse> cadastrarVeiculo(
            @Valid @RequestBody VeiculoRequest request) {

        VeiculoResponse response = veiculoService.cadastrarVeiculo(request);

        // Retorna 201 Created conforme boas práticas REST para criação de recursos
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // FUTURAMENTE: Implementar métodos para GET (busca por ID/Transportador), PUT e
    // DELETE.
}
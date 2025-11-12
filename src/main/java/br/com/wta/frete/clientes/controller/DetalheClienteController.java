package br.com.wta.frete.clientes.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.com.wta.frete.clientes.controller.dto.DetalheClienteRequest;
import br.com.wta.frete.clientes.controller.dto.DetalheClienteResponse;
import br.com.wta.frete.clientes.service.DetalheClienteService;
import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

/**
 * Controller REST para gerenciar os detalhes específicos de um cliente
 * (clientes.detalhes).
 * Utiliza o ID da Pessoa como chave principal do recurso.
 */
@RestController
@RequestMapping("/api/v1/clientes/detalhes")
@RequiredArgsConstructor
public class DetalheClienteController {

    private final DetalheClienteService detalheClienteService;

    /**
     * POST /v1/clientes/detalhes
     * Cria um novo registro de DetalheCliente.
     * O ID da Pessoa deve ser fornecido no corpo e já deve existir.
     * * @param request DTO com os dados do detalhe.
     * 
     * @return 201 Created com o detalhe do cliente.
     */
    @PostMapping
    public ResponseEntity<DetalheClienteResponse> criarDetalheCliente(
            @RequestBody @Valid DetalheClienteRequest request) {

        DetalheClienteResponse response = detalheClienteService.criarDetalheCliente(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * GET /v1/clientes/detalhes/{pessoaId}
     * Busca um detalhe de cliente pelo ID da Pessoa.
     * * @param pessoaId O ID da Pessoa/Cliente.
     * 
     * @return 200 OK com o detalhe do cliente.
     */
    @GetMapping("/{pessoaId}")
    public ResponseEntity<DetalheClienteResponse> buscarDetalheCliente(
            @PathVariable Long pessoaId) {

        DetalheClienteResponse response = detalheClienteService.buscarDetalheClientePorId(pessoaId);
        return ResponseEntity.ok(response);
    }

    /**
     * PUT /v1/clientes/detalhes/{pessoaId}
     * Atualiza os detalhes de um cliente existente.
     * * @param pessoaId O ID da Pessoa/Cliente.
     * 
     * @param request DTO com os dados a serem atualizados.
     * @return 200 OK com o detalhe do cliente atualizado.
     */
    @PutMapping("/{pessoaId}")
    public ResponseEntity<DetalheClienteResponse> atualizarDetalheCliente(
            @PathVariable Long pessoaId,
            @RequestBody @Valid DetalheClienteRequest request) {

        DetalheClienteResponse response = detalheClienteService.atualizarDetalheCliente(pessoaId, request);
        return ResponseEntity.ok(response);
    }

    /**
     * DELETE /v1/clientes/detalhes/{pessoaId}
     * Exclui um detalhe de cliente pelo ID da Pessoa.
     * * @param pessoaId O ID da Pessoa/Cliente.
     * 
     * @return 204 No Content.
     */
    @DeleteMapping("/{pessoaId}")
    public ResponseEntity<Void> excluirDetalheCliente(
            @PathVariable Long pessoaId) {

        detalheClienteService.excluirDetalheCliente(pessoaId);
        return ResponseEntity.noContent().build();
    }
}
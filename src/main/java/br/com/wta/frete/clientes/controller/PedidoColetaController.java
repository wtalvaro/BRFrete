package br.com.wta.frete.clientes.controller;

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

import br.com.wta.frete.clientes.controller.dto.PedidoColetaRequest;
import br.com.wta.frete.clientes.controller.dto.PedidoColetaResponse;
import br.com.wta.frete.clientes.service.PedidoColetaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * Controller REST para gerenciar os Pedidos de Coleta
 * (clientes.pedidos_coleta).
 */
@RestController
@RequestMapping("/api/v1/clientes/pedidos")
@RequiredArgsConstructor
public class PedidoColetaController {

    private final PedidoColetaService pedidoColetaService;

    /**
     * POST /v1/clientes/pedidos
     * Cria um novo pedido de coleta para um cliente existente.
     * * @param request DTO com os dados do pedido (inclui clientePessoaId).
     * 
     * @return 201 Created com o pedido de coleta criado.
     */
    @PostMapping
    public ResponseEntity<PedidoColetaResponse> criarPedidoColeta(
            @RequestBody @Valid PedidoColetaRequest request) {

        PedidoColetaResponse response = pedidoColetaService.criarPedidoColeta(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * GET /v1/clientes/pedidos/{pedidoId}
     * Busca um pedido de coleta pelo ID do pedido.
     * * @param pedidoId O ID do pedido (chave primária).
     * 
     * @return 200 OK com o pedido de coleta.
     */
    @GetMapping("/{pedidoId}")
    public ResponseEntity<PedidoColetaResponse> buscarPedidoColeta(
            @PathVariable Integer pedidoId) {

        PedidoColetaResponse response = pedidoColetaService.buscarPedidoColetaPorId(pedidoId);
        return ResponseEntity.ok(response);
    }

    /**
     * GET /v1/clientes/pedidos/cliente/{clientePessoaId}
     * Busca todos os pedidos de coleta feitos por um cliente específico.
     * * @param clientePessoaId O ID da Pessoa/Cliente.
     * 
     * @return 200 OK com a lista de pedidos.
     */
    @GetMapping("/cliente/{clientePessoaId}")
    public ResponseEntity<List<PedidoColetaResponse>> buscarPedidosPorCliente(
            @PathVariable Long clientePessoaId) {

        List<PedidoColetaResponse> response = pedidoColetaService.buscarPedidosPorCliente(clientePessoaId);
        return ResponseEntity.ok(response);
    }

    /**
     * PUT /v1/clientes/pedidos/{pedidoId}
     * Atualiza os dados de um pedido de coleta existente.
     * * @param pedidoId O ID do pedido a ser atualizado.
     * 
     * @param request DTO com os dados a serem atualizados.
     * @return 200 OK com o pedido de coleta atualizado.
     */
    @PutMapping("/{pedidoId}")
    public ResponseEntity<PedidoColetaResponse> atualizarPedidoColeta(
            @PathVariable Integer pedidoId,
            @RequestBody @Valid PedidoColetaRequest request) {

        PedidoColetaResponse response = pedidoColetaService.atualizarPedidoColeta(pedidoId, request);
        return ResponseEntity.ok(response);
    }

    /**
     * DELETE /v1/clientes/pedidos/{pedidoId}
     * Exclui um pedido de coleta.
     * * @param pedidoId O ID do pedido a ser excluído.
     * 
     * @return 204 No Content.
     */
    @DeleteMapping("/{pedidoId}")
    public ResponseEntity<Void> excluirPedidoColeta(
            @PathVariable Integer pedidoId) {

        pedidoColetaService.excluirPedidoColeta(pedidoId);
        return ResponseEntity.noContent().build();
    }
}
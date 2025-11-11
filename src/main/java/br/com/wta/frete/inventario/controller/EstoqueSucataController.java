package br.com.wta.frete.inventario.controller;

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

import br.com.wta.frete.inventario.controller.dto.EstoqueSucataRequest;
import br.com.wta.frete.inventario.controller.dto.EstoqueSucataResponse;
import br.com.wta.frete.inventario.service.EstoqueSucataService;
import jakarta.validation.Valid;

/**
 * Controller REST para gerenciar o Estoque de Sucata (inventario.estoque).
 * A lógica de negócio reside em EstoqueSucataService, que lida com a
 * idempotência (criar ou atualizar).
 */
@RestController
@RequestMapping("/api/v1/inventario/sucata")
public class EstoqueSucataController {

    private final EstoqueSucataService estoqueSucataService;

    // Injeção de dependência via construtor
    public EstoqueSucataController(EstoqueSucataService estoqueSucataService) {
        this.estoqueSucataService = estoqueSucataService;
    }

    /**
     * Endpoint: POST /api/v1/inventario/sucata
     * Documentação: Cria ou Atualiza um item de estoque de sucata.
     * Se já existir um estoque para o (Sucateiro, TipoMaterial), ele será
     * atualizado.
     *
     * @param request DTO com os dados do estoque.
     * @return O item de estoque criado/atualizado e status 201 (CREATED).
     */
    @PostMapping
    public ResponseEntity<EstoqueSucataResponse> criarOuAtualizar(
            @Valid @RequestBody EstoqueSucataRequest request) {

        // O Service cuida da lógica de criação ou atualização.
        EstoqueSucataResponse response = estoqueSucataService.criarOuAtualizarEstoqueSucata(request);

        // Retorna 201 (Created), mesmo que seja uma atualização, pois o recurso
        // resultante está pronto para uso.
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Endpoint: GET /api/v1/inventario/sucata/{id}
     * Documentação: Busca um item de estoque pelo ID.
     *
     * @param id O ID do item de estoque.
     * @return O DTO do item de estoque.
     */
    @GetMapping("/{id}")
    public ResponseEntity<EstoqueSucataResponse> buscarPorId(@PathVariable Long id) {
        EstoqueSucataResponse response = estoqueSucataService.buscarPorId(id);
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint: GET /api/v1/inventario/sucata/sucateiro/{pessoaId}
     * Documentação: Lista todo o estoque de sucata de um Sucateiro específico.
     *
     * @param pessoaId O ID da Pessoa/Sucateiro.
     * @return Lista de DTOs do estoque.
     */
    @GetMapping("/sucateiro/{pessoaId}")
    public ResponseEntity<List<EstoqueSucataResponse>> listarPorSucateiro(
            @PathVariable("pessoaId") Long sucateiroPessoaId) {

        List<EstoqueSucataResponse> responseList = estoqueSucataService.listarEstoquePorSucateiro(sucateiroPessoaId);
        return ResponseEntity.ok(responseList);
    }

    /**
     * Endpoint: DELETE /api/v1/inventario/sucata/{id}
     * Documentação: Deleta um item de estoque pelo ID.
     *
     * @param id O ID do item de estoque a ser deletado.
     * @return Status 204 (No Content).
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        estoqueSucataService.deletarEstoque(id);
        return ResponseEntity.noContent().build();
    }
}
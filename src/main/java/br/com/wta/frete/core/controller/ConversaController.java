package br.com.wta.frete.core.controller;

import br.com.wta.frete.core.controller.dto.ConversaRequest;
import br.com.wta.frete.core.controller.dto.ConversaResponse;
import br.com.wta.frete.core.entity.Conversa;
import br.com.wta.frete.core.service.ConversaService;
import br.com.wta.frete.core.service.mapper.ConversaMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller REST para a gestão de Conversas (Chats) no core da aplicação.
 * Endpoints: /api/v1/conversas
 */
@RestController // Marca como um Controller REST
@RequestMapping("/api/v1/conversas") // URL base para todos os endpoints
public class ConversaController {

    private final ConversaService conversaService;
    private final ConversaMapper mapper;

    // Injeção de dependência via construtor
    public ConversaController(ConversaService conversaService, ConversaMapper mapper) {
        this.conversaService = conversaService;
        this.mapper = mapper;
    }

    // =========================================================================
    // C - CREATE (Criação de Conversa)
    // =========================================================================

    /**
     * POST /api/v1/conversas
     * Inicia uma nova conversa (chat) com o tipo especificado no corpo da
     * requisição.
     *
     * @param request DTO com o TipoConversa desejado.
     * @return ResponseEntity com o DTO da Conversa recém-criada e status 201
     *         CREATED.
     */
    @PostMapping
    public ResponseEntity<ConversaResponse> iniciarConversa(@Valid @RequestBody ConversaRequest request) {
        // Chama o Service para iniciar a conversa
        Conversa novaConversa = conversaService.iniciarNovaConversa(request.tipoConversa());

        // Converte a entidade para o DTO de resposta
        return new ResponseEntity<>(mapper.toResponse(novaConversa), HttpStatus.CREATED);
    }

    // =========================================================================
    // R - READ (Leitura/Busca)
    // =========================================================================

    /**
     * GET /api/v1/conversas/{conversaId}
     * Busca uma conversa pelo ID. Lança 404 se não encontrada (tratado por
     * GlobalExceptionHandler).
     *
     * @param conversaId O ID da Conversa.
     * @return ResponseEntity com o DTO da Conversa e status 200 OK.
     */
    @GetMapping("/{conversaId}")
    public ResponseEntity<ConversaResponse> buscarPorId(@PathVariable Long conversaId) {
        // O Service lança ResourceNotFoundException se não encontrar.
        Conversa conversa = conversaService.buscarPorId(conversaId);
        return ResponseEntity.ok(mapper.toResponse(conversa));
    }

    /**
     * GET /api/v1/conversas
     * Lista todas as conversas cadastradas.
     *
     * @return Lista de DTOs ConversaResponse.
     */
    @GetMapping
    public ResponseEntity<List<ConversaResponse>> listarTodas() {
        List<Conversa> conversas = conversaService.listarTodas();
        // O MapStruct, via ConversaMapper, é usado para converter a lista de Entidades.
        return ResponseEntity.ok(mapper.toResponseList(conversas));
    }

    // =========================================================================
    // D - DELETE (Deletar Conversa)
    // =========================================================================

    /**
     * DELETE /api/v1/conversas/{conversaId}
     * Deleta a conversa pelo ID.
     *
     * @param conversaId O ID da Conversa a ser deletada.
     * @return ResponseEntity sem conteúdo (No Content) e status 204 NO CONTENT.
     */
    @DeleteMapping("/{conversaId}")
    public ResponseEntity<Void> deletarConversa(@PathVariable Long conversaId) {
        conversaService.deletar(conversaId);
        // Retorna 204 NO CONTENT, padrão para deleções bem-sucedidas.
        return ResponseEntity.noContent().build();
    }
}
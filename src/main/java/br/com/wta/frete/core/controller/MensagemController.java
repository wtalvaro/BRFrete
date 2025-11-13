package br.com.wta.frete.core.controller;

import br.com.wta.frete.core.controller.dto.MensagemRequest;
import br.com.wta.frete.core.controller.dto.MensagemResponse;
import br.com.wta.frete.core.entity.Mensagem;
import br.com.wta.frete.core.service.MensagemService;
import br.com.wta.frete.core.service.mapper.MensagemMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller REST para a gestão de Mensagens (core.mensagens).
 * Endpoints: /api/v1/mensagens
 */
@RestController
@RequestMapping("/api/v1/mensagens")
public class MensagemController {

    private final MensagemService mensagemService;
    private final MensagemMapper mapper;

    public MensagemController(MensagemService mensagemService, MensagemMapper mapper) {
        this.mensagemService = mensagemService;
        this.mapper = mapper;
    }

    // =========================================================================
    // C - CREATE (Enviar Mensagem)
    // =========================================================================

    /**
     * POST /api/v1/mensagens
     * Envia uma nova mensagem.
     *
     * @param request DTO de Requisição contendo IDs de Conversa e Autor.
     * @return ResponseEntity com o DTO da Mensagem criada e status 201 CREATED.
     */
    @PostMapping
    public ResponseEntity<MensagemResponse> enviarMensagem(@Valid @RequestBody MensagemRequest request) {

        // 1. Converte DTO para entidade (o Mapper ignora os campos de relacionamento)
        Mensagem mensagem = mapper.toEntity(request);

        // 2. Chama o Service, passando os IDs para que o Service resolva as entidades
        Mensagem mensagemSalva = mensagemService.enviarMensagem(
                mensagem,
                request.conversaId(), // Usa o accessor de Record
                request.autorId() // Usa o accessor de Record
        );

        // 3. Converte e retorna
        return new ResponseEntity<>(mapper.toResponse(mensagemSalva), HttpStatus.CREATED);
    }

    // =========================================================================
    // R - READ (Leitura/Busca)
    // =========================================================================

    /**
     * GET /api/v1/mensagens/{mensagemId}
     * Busca uma mensagem pelo seu ID.
     *
     * @param mensagemId O ID da Mensagem.
     * @return ResponseEntity com o DTO da Mensagem e status 200 OK.
     */
    @GetMapping("/{mensagemId}")
    public ResponseEntity<MensagemResponse> buscarMensagemPorId(@PathVariable Long mensagemId) {
        Mensagem mensagem = mensagemService.buscarPorId(mensagemId);
        return ResponseEntity.ok(mapper.toResponse(mensagem));
    }

    // Este endpoint é o mais importante para um chat: Listar todas as mensagens de
    // uma conversa.
    /**
     * GET /api/v1/mensagens/conversa/{conversaId}
     * Lista todas as mensagens de uma conversa específica.
     *
     * @param conversaId O ID da Conversa.
     * @return Lista de DTOs MensagemResponse.
     */
    @GetMapping("/conversa/{conversaId}")
    public ResponseEntity<List<MensagemResponse>> listarMensagensPorConversa(
            @PathVariable Long conversaId) {

        List<Mensagem> mensagens = mensagemService.listarPorConversaId(conversaId);

        // O ListarTodas deve ser adicionado ao MensagemMapper para funcionar.
        return ResponseEntity.ok(mapper.toResponseList(mensagens));
    }

    // =========================================================================
    // U - UPDATE (Marcar como Lida)
    // =========================================================================

    /**
     * PUT /api/v1/mensagens/{mensagemId}/lida
     * Marca uma mensagem como lida.
     *
     * @param mensagemId O ID da Mensagem a ser marcada.
     * @return MensagemResponse atualizada.
     */
    @PutMapping("/{mensagemId}/lida")
    public ResponseEntity<MensagemResponse> marcarComoLida(@PathVariable Long mensagemId) {
        Mensagem mensagemAtualizada = mensagemService.marcarComoLida(mensagemId);
        return ResponseEntity.ok(mapper.toResponse(mensagemAtualizada));
    }

    // =========================================================================
    // D - DELETE (Deletar Mensagem)
    // =========================================================================

    /**
     * DELETE /api/v1/mensagens/{mensagemId}
     * Deleta uma mensagem pelo ID.
     *
     * @param mensagemId O ID da Mensagem a ser deletada.
     * @return ResponseEntity 204 NO CONTENT.
     */
    @DeleteMapping("/{mensagemId}")
    public ResponseEntity<Void> deletarMensagem(@PathVariable Long mensagemId) {
        mensagemService.deletar(mensagemId);
        return ResponseEntity.noContent().build();
    }
}
package br.com.wta.frete.colaboradores.controller;

import br.com.wta.frete.colaboradores.service.TransportadorService;
import br.com.wta.frete.colaboradores.controller.dto.TransportadorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
// Assumindo que você usa Swagger/OpenAPI:
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/colaboradores/transportadores")
@Tag(name = "Transportador", description = "Endpoints para gerenciamento de Transportadores")
public class TransportadorController {

    private final TransportadorService transportadorService;

    public TransportadorController(TransportadorService transportadorService) {
        this.transportadorService = transportadorService;
    }

    /**
     * Documentação: Endpoint para converter um lead existente em um Transportador.
     * <p>
     * Rota: POST /api/colaboradores/transportadores/converter/{pessoaId}
     *
     * @param pessoaId O ID da Pessoa que será convertida.
     * @return Resposta 201 Created com os dados do novo Transportador.
     */
    @Operation(summary = "Converte um usuário Lead em Transportador", description = "Cria a entidade Transportador e associa o perfil correspondente ('TRANSPORTADOR') à Pessoa existente. Também atualiza o status 'isColaborador' da pessoa para true.")
    @PostMapping("/converter/{pessoaId}")
    public ResponseEntity<TransportadorResponse> converterLeadEmTransportador(@PathVariable Long pessoaId) {
        TransportadorResponse response = transportadorService.converterLeadEmTransportador(pessoaId);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // Você pode adicionar outros endpoints para Transportador aqui
}
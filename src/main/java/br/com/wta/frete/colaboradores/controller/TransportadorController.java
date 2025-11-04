package br.com.wta.frete.colaboradores.controller;

import br.com.wta.frete.colaboradores.service.TransportadorService;
import br.com.wta.frete.colaboradores.controller.dto.TransportadorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
     * Documentação: Endpoint para adicionar o perfil de Transportador a uma Pessoa
     * existente.
     * <p>
     * Rota: POST /api/colaboradores/transportadores/adicionar-perfil/{pessoaId}
     *
     * @param pessoaId O ID da Pessoa que receberá o perfil.
     * @return Resposta 201 Created com os dados do Transportador.
     */
    @Operation(summary = "Adiciona o perfil de Transportador a um usuário existente", description = "Cria a entidade Transportador (se não existir) e associa o perfil 'TRANSPORTADOR' à Pessoa. Permite que um usuário acumule múltiplas funções no sistema.")
    @PostMapping("/adicionar-perfil/{pessoaId}")
    public ResponseEntity<TransportadorResponse> adicionarPerfilTransportador(@PathVariable Long pessoaId) {
        // CHAMADA DO NOVO MÉTODO
        TransportadorResponse response = transportadorService.adicionarPerfilTransportador(pessoaId);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // Você pode adicionar outros endpoints para Transportador aqui
}
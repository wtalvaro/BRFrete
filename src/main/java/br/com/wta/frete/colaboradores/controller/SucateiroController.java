package br.com.wta.frete.colaboradores.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody; // Importação necessária
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.wta.frete.colaboradores.controller.dto.SucateiroRequest; // Importação necessária
import br.com.wta.frete.colaboradores.controller.dto.SucateiroResponse;
import br.com.wta.frete.colaboradores.service.SucateiroService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid; // Importação necessária

@RestController
@RequestMapping("/api/colaboradores/sucateiros")
@Tag(name = "Sucateiro", description = "Endpoints para gerenciamento de Sucateiros")
public class SucateiroController {

    private final SucateiroService sucateiroService;

    public SucateiroController(SucateiroService sucateiroService) {
        this.sucateiroService = sucateiroService;
    }

    /**
     * Documentação: Endpoint para adicionar o perfil de Sucateiro a uma Pessoa
     * existente.
     * <p>
     * Rota: POST /api/colaboradores/sucateiros/adicionar-perfil/{pessoaId}
     *
     * @param pessoaId O ID da Pessoa que receberá o perfil.
     * @param request  Os dados do Sucateiro (razão social, endereço, etc.).
     * @return Resposta 201 Created com os dados do Sucateiro.
     */
    @Operation(summary = "Adiciona o perfil de Sucateiro a um usuário existente", description = "Cria a entidade Sucateiro (se não existir) e associa o perfil 'SUCATEIRO' à Pessoa. Permite que um usuário acumule múltiplas funções no sistema.")
    @PostMapping("/adicionar-perfil/{pessoaId}")
    public ResponseEntity<SucateiroResponse> adicionarPerfilSucateiro(
            @PathVariable Long pessoaId,
            @Valid @RequestBody SucateiroRequest request) { // Adiciona o RequestBody
        SucateiroResponse response = sucateiroService.adicionarPerfilSucateiro(pessoaId, request); // Passa o request
                                                                                                   // para o Service
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
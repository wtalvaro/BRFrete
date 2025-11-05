package br.com.wta.frete.colaboradores.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody; // <--- Importação adicionada
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.wta.frete.colaboradores.controller.dto.LojistaRequest; // <--- Importação adicionada
import br.com.wta.frete.colaboradores.controller.dto.LojistaResponse;
import br.com.wta.frete.colaboradores.service.LojistaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid; // <--- Importação adicionada para validar o corpo

@RestController
@RequestMapping("/api/colaboradores/lojistas")
@Tag(name = "Lojista", description = "Endpoints para gerenciamento de Lojistas")
public class LojistaController {

    private final LojistaService lojistaService;

    public LojistaController(LojistaService lojistaService) {
        this.lojistaService = lojistaService;
    }

    /**
     * Documentação: Endpoint para adicionar o perfil de Lojista a uma Pessoa
     * existente.
     * <p>
     * Rota: POST /api/colaboradores/lojistas/adicionar-perfil
     *
     * 💡 CORREÇÃO APLICADA: Recebe o LojistaRequest no corpo, que contém o pessoaId
     * e os dados obrigatórios (nomeLoja, enderecoPrincipal).
     *
     * @param request O DTO de requisição com o ID da Pessoa e os dados do Lojista.
     * @return Resposta 201 Created com os dados do Lojista.
     */
    @Operation(summary = "Adiciona o perfil de Lojista a um usuário existente", description = "Cria a entidade Lojista (se não existir) e associa o perfil 'LOJISTA' à Pessoa. O corpo da requisição (Request Body) é OBRIGATÓRIO para fornecer Nome da Loja e Endereço.")
    @PostMapping("/adicionar-perfil") // <--- Rota alterada para receber o DTO no corpo
    public ResponseEntity<LojistaResponse> adicionarPerfilLojista(@RequestBody @Valid LojistaRequest request) {
        // CHAMADA DO SERVICE COM O DTO COMPLETO
        LojistaResponse response = lojistaService.adicionarPerfilLojista(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // O endpoint GET ou PUT de atualização deve ser feito em rotas separadas se
    // necessário.
}
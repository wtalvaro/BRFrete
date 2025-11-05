package br.com.wta.frete.colaboradores.controller;

import br.com.wta.frete.colaboradores.service.CatadorService;
import br.com.wta.frete.colaboradores.controller.dto.CatadorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/colaboradores/catadores")
@Tag(name = "Catador", description = "Endpoints para gerenciamento de Catadores")
public class CatadorController {

    private final CatadorService catadorService;

    public CatadorController(CatadorService catadorService) {
        this.catadorService = catadorService;
    }

    /**
     * Documentação: Endpoint para adicionar o perfil de Catador a uma Pessoa
     * existente.
     * <p>
     * Rota: POST /api/colaboradores/catadores/adicionar-perfil/{pessoaId}
     *
     * @param pessoaId O ID da Pessoa que receberá o perfil.
     * @return Resposta 201 Created com os dados do Catador.
     */
    @Operation(summary = "Adiciona o perfil de Catador a um usuário existente", description = "Cria a entidade Catador (se não existir) e associa o perfil 'CATADOR' à Pessoa. Permite que um usuário acumule múltiplas funções no sistema.")
    @PostMapping("/adicionar-perfil/{pessoaId}")
    public ResponseEntity<CatadorResponse> adicionarPerfilCatador(@PathVariable Long pessoaId) {
        CatadorResponse response = catadorService.adicionarPerfilCatador(pessoaId);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
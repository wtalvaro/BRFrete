// Caminho: src/main/java/br/com/wta/frete/core/controller/PessoaController.java
package br.com.wta.frete.core.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.wta.frete.core.controller.dto.PessoaRequest;
import br.com.wta.frete.core.controller.dto.PessoaResponse;
import br.com.wta.frete.core.controller.dto.CadastroSimplificadoRequest;
import br.com.wta.frete.core.entity.Pessoa;
import br.com.wta.frete.core.service.CadastroPessoaService; // <<< CORRIGIDO
import br.com.wta.frete.core.service.mapper.PessoaMapper;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor; // Adicionado para injeção via construtor

/**
 * Documentação: Controller REST para a entidade Pessoa.
 * Responsável por gerenciar as requisições HTTP da API.
 */
@RestController
@RequestMapping("/api/v1/pessoas")
@Tag(name = "Pessoa", description = "Endpoints para o cadastro e gestão de dados básicos de usuários (Pessoas).")
@RequiredArgsConstructor
public class PessoaController {

    // CORREÇÃO: Injeta o serviço correto (CadastroPessoaService)
    private final CadastroPessoaService cadastroPessoaService;
    private final PessoaMapper pessoaMapper;

    /**
     * POST /api/v1/pessoas/cadastro
     * Cadastro completo de pessoa.
     */
    @Operation(summary = "Cadastro completo de um novo usuário", description = "Permite o cadastro completo de todos os dados do usuário.")
    @PostMapping("/cadastro")
    public ResponseEntity<PessoaResponse> cadastrarPessoa(
            @Valid @RequestBody PessoaRequest request) {

        try {
            // CORREÇÃO: Chama o método no serviço correto (CadastroPessoaService)
            Pessoa pessoaSalva = cadastroPessoaService.cadastrarPessoa(request);
            var response = pessoaMapper.toResponse(pessoaSalva);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (IllegalArgumentException e) {
            // Em um ambiente real, o GlobalExceptionHandler lida com isso.
            throw new RuntimeException("Erro ao realizar cadastro completo: " + e.getMessage(), e);
        }
    }

    /**
     * POST /api/v1/pessoas/cadastro/simplificado
     * Cadastro inicial apenas com e-mail e senha.
     */
    @Operation(summary = "Cadastro simplificado de um novo usuário", description = "Permite o cadastro rápido com e-mail e senha.")
    @PostMapping("/cadastro/simplificado")
    public ResponseEntity<PessoaResponse> cadastrarSimplificado(
            @Valid @RequestBody CadastroSimplificadoRequest request) {

        try {
            // CORREÇÃO: Chama o método no serviço correto (CadastroPessoaService)
            Pessoa pessoaSalva = cadastroPessoaService.cadastrarPessoaSimplificado(request);

            var response = pessoaMapper.toResponse(pessoaSalva);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (IllegalArgumentException e) {
            // Em um ambiente real, o GlobalExceptionHandler lida com isso.
            throw new RuntimeException("Erro ao realizar cadastro simplificado: " + e.getMessage(), e);
        }
    }
}
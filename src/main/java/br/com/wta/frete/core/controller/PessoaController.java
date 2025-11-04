package br.com.wta.frete.core.controller;

import org.springframework.http.HttpStatus; //
import org.springframework.http.ResponseEntity; //
import org.springframework.web.bind.annotation.PostMapping; //
import org.springframework.web.bind.annotation.RequestBody; //
import org.springframework.web.bind.annotation.RequestMapping; //
import org.springframework.web.bind.annotation.RestController; //

import br.com.wta.frete.core.controller.dto.PessoaRequest; //
import br.com.wta.frete.core.controller.dto.PessoaResponse; //
// NOVO IMPORT: DTO de requisição simplificada
import br.com.wta.frete.core.controller.dto.CadastroSimplificadoRequest; //
import br.com.wta.frete.core.service.PessoaService; //
import br.com.wta.frete.core.service.mapper.PessoaMapper; //
import jakarta.validation.Valid; //
// NOVOS IMPORTS para Swagger/OpenAPI
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Documentação: Controller REST para a entidade Pessoa.
 * Responsável por gerenciar as requisições HTTP da API.
 */
@RestController
@RequestMapping("/api/pessoas") // Prefixo padrão para endpoints de API
@Tag(name = "Pessoa", description = "Endpoints para o cadastro e gestão de dados básicos de usuários (Pessoas).") // NOVO
public class PessoaController {

    private final PessoaService pessoaService; // Injeção de dependência
    private final PessoaMapper pessoaMapper; // Injeção de dependência

    // Injeção de Dependências
    public PessoaController(PessoaService pessoaService, PessoaMapper pessoaMapper) {
        this.pessoaService = pessoaService; //
        this.pessoaMapper = pessoaMapper; //
    }

    /**
     * Documentação: Endpoint REST para cadastrar um novo usuário (Completo).
     * * @param request DTO de requisição com os dados do usuário.
     * * @return ResponseEntity com o DTO de resposta (PessoaResponse) e status 201
     * (Created).
     */
    @Operation(summary = "Cadastro completo de um novo usuário", description = "Cria um novo usuário com todos os dados obrigatórios e inicia o fluxo de ativação de conta (envio de e-mail).") // NOVO
    @PostMapping("/cadastro")
    public ResponseEntity<PessoaResponse> cadastrarPessoa(@Valid @RequestBody PessoaRequest request) { //

        try {
            // 1. Chama a lógica de negócio para salvar
            var pessoaSalva = pessoaService.cadastrarPessoa(request); //

            // 2. Mapeia a Entidade salva para o DTO de Resposta
            var response = pessoaMapper.toResponse(pessoaSalva); //

            // 3. Retorna o status 201 CREATED
            return ResponseEntity.status(HttpStatus.CREATED).body(response); //

        } catch (IllegalArgumentException e) {
            // Tratamento de erros de regra de negócio (e-mail/documento duplicado)
            throw new RuntimeException("Erro ao cadastrar pessoa: " + e.getMessage(), e); //
        }
    }

    // =====================================================================
    // NOVO ENDPOINT: CADASTRO SIMPLIFICADO
    // =====================================================================

    /**
     * Documentação: Endpoint REST para cadastrar um novo usuário com apenas e-mail
     * e senha.
     * Utiliza a lógica de preenchimento com placeholders no Service e inicia o
     * processo de ativação.
     * * @param request DTO de requisição simplificada com e-mail e senha.
     * * @return ResponseEntity com o DTO de resposta (PessoaResponse) e status 201
     * (Created).
     */
    @Operation(summary = "Cadastro simplificado de um novo usuário", description = "Permite o cadastro rápido com e-mail e senha, preenchendo os dados restantes com placeholders e iniciando o fluxo de ativação.") // NOVO
    @PostMapping("/cadastro/simplificado")
    public ResponseEntity<PessoaResponse> cadastrarSimplificado(
            @Valid @RequestBody CadastroSimplificadoRequest request) { //

        try {
            // 1. Chama a lógica de negócio para salvar (usa o método simplificado do
            // Service)
            // Este método já lida com o mapeamento (placeholders) e ativação.
            var pessoaSalva = pessoaService.cadastrarPessoaSimplificado(request); //

            // 2. Mapeia a Entidade salva para o DTO de Resposta (PessoaResponse)
            var response = pessoaMapper.toResponse(pessoaSalva); //

            // 3. Retorna o status 201 CREATED
            return ResponseEntity.status(HttpStatus.CREATED).body(response); //

        } catch (IllegalArgumentException e) {
            // Captura erros de regra de negócio (e-mail duplicado) do Service
            throw new RuntimeException("Erro ao realizar cadastro simplificado: " + e.getMessage(), e); //
        }
    }
}
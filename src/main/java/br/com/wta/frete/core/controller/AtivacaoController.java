package br.com.wta.frete.core.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.wta.frete.core.service.PessoaService;
import br.com.wta.frete.core.service.TokenAtivacaoService;

/**
 * Controller: AtivacaoController
 * Endpoint que trata o clique no link de ativação enviado por e-mail (Fase 3).
 */
@RestController
@RequestMapping("/api/v1/ativacao")
public class AtivacaoController {

    @Autowired
    private TokenAtivacaoService tokenService;

    @Autowired
    private PessoaService pessoaService;

    /**
     * Documentação: ativarConta
     * Lógica para validar o token no Redis e ativar a conta no PostgreSQL.
     * Exemplo de URL de ativação: GET /api/v1/ativacao?token=a1b2c3d4e5f6...
     */
    @GetMapping
    public ResponseEntity<String> ativarConta(@RequestParam("token") String token) {

        // 1. Busca o ID da Pessoa no Redis (Fase 3: Validação)
        // Se o token existir e não tiver expirado (em 24h), retorna o ID.
        Long pessoaId = tokenService.obterIdPessoaPorToken(token);

        if (pessoaId == null) {
            // Token não encontrado ou expirou (TTL de 24h atingido)
            return ResponseEntity.badRequest()
                    .body("Token de ativação inválido ou expirado. Por favor, solicite um novo link.");
        }

        // 2. Atualiza a flag 'ativo' no PostgreSQL
        try {
            pessoaService.ativarPessoa(pessoaId);
            // 3. Retorna sucesso e redireciona (ou mostra uma página de sucesso)
            return ResponseEntity.ok("Conta ativada com sucesso! Você já pode fazer login.");
        } catch (Exception e) {
            // Caso haja erro na atualização do banco de dados
            return ResponseEntity.internalServerError().body("Erro interno ao ativar a conta.");
        }
    }
}
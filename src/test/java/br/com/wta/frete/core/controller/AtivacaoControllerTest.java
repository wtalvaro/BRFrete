package br.com.wta.frete.core.controller;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
// Removemos: import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.anonymous;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
// NOVO IMPORT
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import br.com.wta.frete.core.service.PessoaService;
import br.com.wta.frete.core.service.TokenAtivacaoService;

// REMOVEMOS AS IMPORTAÇÕES RELACIONADAS AO anonymous() QUE ESTAVAM CAUSANDO CONFLITO
// E AGORA VAMOS DESABILITAR OS FILTROS DE SEGURANÇA.
// @WebMvcTest: Carrega apenas a camada de Controller (o que queremos).
// @AutoConfigureMockMvc(addFilters = false): Diz para o MockMvc ignorar os filtros do Spring Security.
@WebMvcTest(AtivacaoController.class)
@AutoConfigureMockMvc(addFilters = false) // <--- CORREÇÃO FINAL PARA O REDIRECIONAMENTO 302
public class AtivacaoControllerTest {

    // Simula a injeção do MockMvc para simular requisições HTTP
    @Autowired
    private MockMvc mockMvc;

    // Mocks das dependências de serviço (Lógica de Negócio e Repositório)
    @SuppressWarnings("removal")
    @MockBean
    private TokenAtivacaoService tokenService;

    @SuppressWarnings("removal")
    @MockBean
    private PessoaService pessoaService;

    // --- VARIÁVEIS DE TESTE ---
    private static final String ENDPOINT_ATIVACAO = "/api/v1/ativacao";
    private static final String TOKEN_VALIDO = "a1b2c3d4e5f6g7h8i9j0l1m2n3o4p5q6";
    private static final String TOKEN_INVALIDO = "token-expirado";
    private static final Long PESSOA_ID = 1L;

    // =================================================================
    // TESTE 1: Caminho Feliz - Sucesso
    // =================================================================

    @SuppressWarnings("null")
    @Test
    void deveRetornarStatus200EAtivarContaComTokenValido() throws Exception {
        // 1. ARRANGE
        when(tokenService.obterIdPessoaPorToken(TOKEN_VALIDO)).thenReturn(PESSOA_ID);
        doNothing().when(pessoaService).ativarPessoa(PESSOA_ID);

        // 2. ACT & ASSERT
        mockMvc.perform(get(ENDPOINT_ATIVACAO).param("token", TOKEN_VALIDO)
                // REMOVIDO: .with(anonymous())
                .contentType(MediaType.APPLICATION_JSON))
                // Deve retornar Status 200 (OK)
                .andExpect(status().isOk())
                .andExpect(content().string("Conta ativada com sucesso! Você já pode fazer login."));

        // 3. VERIFY
        verify(tokenService).obterIdPessoaPorToken(TOKEN_VALIDO);
        verify(pessoaService).ativarPessoa(PESSOA_ID);
    }

    // =================================================================
    // TESTE 2: Caminho Triste - Token Inválido ou Expirado
    // =================================================================

    @SuppressWarnings("null")
    @Test
    void deveRetornarStatus400ComTokenInvalidoOuExpirado() throws Exception {
        // 1. ARRANGE
        when(tokenService.obterIdPessoaPorToken(TOKEN_INVALIDO)).thenReturn(null);

        // 2. ACT & ASSERT
        mockMvc.perform(get(ENDPOINT_ATIVACAO).param("token", TOKEN_INVALIDO)
                // REMOVIDO: .with(anonymous())
                .contentType(MediaType.APPLICATION_JSON))
                // Deve retornar Status 400 (BAD REQUEST)
                .andExpect(status().isBadRequest()).andExpect(
                        content().string("Token de ativação inválido ou expirado. Por favor, solicite um novo link."));

        // 3. VERIFY
        verify(tokenService).obterIdPessoaPorToken(TOKEN_INVALIDO);
        verify(pessoaService, never()).ativarPessoa(anyLong());
    }

    // =================================================================
    // TESTE 3: Caminho Triste - Erro Interno do Serviço
    // =================================================================

    @SuppressWarnings("null")
    @Test
    void deveRetornarStatus500EmCasoDeFalhaInterna() throws Exception {
        // 1. ARRANGE
        when(tokenService.obterIdPessoaPorToken(TOKEN_VALIDO)).thenReturn(PESSOA_ID);
        doThrow(new RuntimeException("Erro de conexão com o DB")).when(pessoaService).ativarPessoa(PESSOA_ID);

        // 2. ACT & ASSERT
        mockMvc.perform(get(ENDPOINT_ATIVACAO).param("token", TOKEN_VALIDO)
                // REMOVIDO: .with(anonymous())
                .contentType(MediaType.APPLICATION_JSON))
                // Deve retornar Status 500 (INTERNAL SERVER ERROR)
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Erro interno ao ativar a conta."));

        // 3. VERIFY
        verify(tokenService).obterIdPessoaPorToken(TOKEN_VALIDO);
        verify(pessoaService).ativarPessoa(PESSOA_ID);
    }
}
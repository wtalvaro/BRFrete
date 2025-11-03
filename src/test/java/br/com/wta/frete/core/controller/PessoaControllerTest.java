package br.com.wta.frete.core.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate; // Import necessário
import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
// IMPORTS DE EXCLUSÃO COMPLETOS PARA RESOLVER O ERRO DE CONTEXTO
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientAutoConfiguration;
import org.springframework.boot.autoconfigure.security.oauth2.resource.servlet.OAuth2ResourceServerAutoConfiguration;

import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.wta.frete.core.controller.dto.CadastroSimplificadoRequest;
import br.com.wta.frete.core.controller.dto.PessoaResponse;
import br.com.wta.frete.core.controller.dto.PessoaRequest;
import br.com.wta.frete.core.entity.Pessoa;
import br.com.wta.frete.core.service.PessoaService;
import br.com.wta.frete.core.service.mapper.PessoaMapper;
import br.com.wta.frete.shared.exception.InvalidDataException;

/**
 * Teste de Integração para PessoaController, com exclusão de Security para
 * isolamento.
 */
@WebMvcTest(value = PessoaController.class, excludeAutoConfiguration = { SecurityAutoConfiguration.class,
        UserDetailsServiceAutoConfiguration.class, OAuth2ClientAutoConfiguration.class,
        OAuth2ResourceServerAutoConfiguration.class })
@ActiveProfiles("test")
public class PessoaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @SuppressWarnings("removal")
    @MockBean
    private PessoaService pessoaService;

    @SuppressWarnings("removal")
    @MockBean
    private PessoaMapper pessoaMapper;

    private static final String ENDPOINT_SIMPLIFICADO = "/api/pessoas/cadastro/simplificado";
    private static final String ENDPOINT_COMPLETO = "/api/pessoas/cadastro";

    private CadastroSimplificadoRequest requestSimplificado;
    private PessoaRequest requestCompleto;
    private Pessoa pessoaSimulada;
    private PessoaResponse responseSimulada;

    @BeforeEach
    void setup() {
        requestSimplificado = new CadastroSimplificadoRequest("teste@email.com", "senhaSegura123");

        // DTO de Requisição Completa: AGORA COM 6 ARGUMENTOS, incluindo dataNascimento
        requestCompleto = new PessoaRequest("Nome Completo do Utilizador", // 1. nomeCompleto
                "12.345.678/0001-90", // 2. documento
                LocalDate.of(1990, 1, 1), // 3. dataNascimento (CORRIGIDO)
                "nome.completo@email.com", // 4. email
                "SenhaForte123", // 5. senha
                "5511987654321" // 6. telefone
        );

        pessoaSimulada = new Pessoa();
        pessoaSimulada.setId(1L);
        pessoaSimulada.setEmail("teste@email.com");
        pessoaSimulada.setNome("PLACEHOLDER");
        pessoaSimulada.setAtivo(true);

        // DTO de Resposta: AGORA COM 9 ARGUMENTOS (id, nome, email, telefone,
        // dataNascimento, dataCadastro, ativo, isColaborador, isCliente)
        responseSimulada = new PessoaResponse(
                pessoaSimulada.getId(),
                pessoaSimulada.getNome(),
                pessoaSimulada.getEmail(),
                "5511987654321", // Telefone (usando um valor, pode ser null, mas o original era null)
                LocalDate.of(1990, 1, 1), // dataNascimento (CORRIGIDO)
                LocalDateTime.now(), // dataCadastro
                pessoaSimulada.isAtivo(),
                false, // isColaborador
                true // isCliente
        );
    }

    // =================================================================
    // TESTE 1: cadastrarSimplificado - Caminho Feliz (201 Created)
    // =================================================================
    @SuppressWarnings("null")
    @Test
    void deveCadastrarSimplificadoComSucessoERetornarStatus201() throws Exception {
        when(pessoaService.cadastrarPessoaSimplificado(any(CadastroSimplificadoRequest.class)))
                .thenReturn(pessoaSimulada);
        when(pessoaMapper.toResponse(pessoaSimulada)).thenReturn(responseSimulada);

        mockMvc.perform(post(ENDPOINT_SIMPLIFICADO).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestSimplificado)))

                .andExpect(status().isCreated()).andExpect(content().contentType(MediaType.APPLICATION_JSON))

                .andExpect(jsonPath("$.id").value(responseSimulada.id()))
                .andExpect(jsonPath("$.email").value(responseSimulada.email()))
                .andExpect(jsonPath("$.nomeCompleto").value(responseSimulada.nomeCompleto()));
    }

    // =================================================================
    // TESTE 2: cadastrarSimplificado - Caminho Triste (E-mail Duplicado -> 400)
    // =================================================================
    @SuppressWarnings("null")
    @Test
    void deveRetornarStatus400QuandoEmailSimplificadoForDuplicado() throws Exception {
        final String MENSAGEM_ERRO_NEGOCIO = "E-mail já existe na base de dados.";
        final String REASON_CODE = "EMAIL_DUPLICADO";
        final String FIELD = "email";

        // 1. ARRANGE
        doThrow(new InvalidDataException(MENSAGEM_ERRO_NEGOCIO, REASON_CODE, FIELD)).when(pessoaService)
                .cadastrarPessoaSimplificado(any(CadastroSimplificadoRequest.class));

        // 2. ACT & 3. ASSERT
        mockMvc.perform(post(ENDPOINT_SIMPLIFICADO).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestSimplificado)))

                .andExpect(status().isBadRequest()).andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))

                // Verifica os campos customizados do ProblemDetail
                .andExpect(jsonPath("$.title").value("Dados de Entrada Inválidos"))
                .andExpect(jsonPath("$.reasonCode").value(REASON_CODE)).andExpect(jsonPath("$.field").value(FIELD));

        verify(pessoaService, times(1)).cadastrarPessoaSimplificado(any(CadastroSimplificadoRequest.class));
        verify(pessoaMapper, never()).toResponse(any(Pessoa.class));
    }

    // =================================================================
    // TESTE 3: cadastrarSimplificado - Caminho Triste (Validação DTO -> 400)
    // =================================================================
    @SuppressWarnings("null")
    @Test
    void deveRetornarStatus400ParaRequestSimplificadoInvalida() throws Exception {
        // 1. ARRANGE (E-mail Nulo)
        CadastroSimplificadoRequest requestInvalida = new CadastroSimplificadoRequest(null, "senhaSegura123");

        // 2. ACT & 3. ASSERT
        mockMvc.perform(post(ENDPOINT_SIMPLIFICADO).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestInvalida)))

                .andExpect(status().isBadRequest()).andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))

                .andExpect(jsonPath("$.validationErrors").exists())
                .andExpect(jsonPath("$.validationErrors.email").exists());

        verify(pessoaService, never()).cadastrarPessoaSimplificado(any(CadastroSimplificadoRequest.class));
        verify(pessoaMapper, never()).toResponse(any(Pessoa.class));
    }

    // =================================================================
    // TESTE 4: cadastrarCompleto - Caminho Feliz (201 Created)
    // =================================================================
    @SuppressWarnings("null")
    @Test
    void deveCadastrarCompletoComSucessoERetornarStatus201() throws Exception {
        when(pessoaService.cadastrarPessoa(any(PessoaRequest.class))).thenReturn(pessoaSimulada);

        when(pessoaMapper.toResponse(pessoaSimulada)).thenReturn(responseSimulada);

        mockMvc.perform(post(ENDPOINT_COMPLETO).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestCompleto)))

                .andExpect(status().isCreated()).andExpect(content().contentType(MediaType.APPLICATION_JSON))

                .andExpect(jsonPath("$.id").value(responseSimulada.id()))
                .andExpect(jsonPath("$.email").value(responseSimulada.email()))
                .andExpect(jsonPath("$.nomeCompleto").value(responseSimulada.nomeCompleto()));

        verify(pessoaService, times(1)).cadastrarPessoa(any(PessoaRequest.class));
    }

    // =================================================================
    // TESTE 5: cadastrarCompleto - Caminho Triste (Validação DTO -> 400)
    // =================================================================
    @SuppressWarnings("null")
    @Test
    void deveRetornarStatus400ParaRequestCompletaInvalida() throws Exception {
        // 1. ARRANGE (Criar uma requisição inválida: nome e documento nulos)
        // A ordem dos campos é: nomeCompleto, documento, dataNascimento, email, senha,
        // telefone
        PessoaRequest requestInvalida = new PessoaRequest(null, // 1. nomeCompleto: Inválido (@NotBlank)
                null, // 2. documento: Inválido (@NotBlank)
                LocalDate.of(2000, 1, 1), // 3. dataNascimento: Válido (adicionado para compilar)
                "valido@email.com", // 4. email: Válido
                "SenhaForte123", // 5. senha: Válido
                "5511987654321" // 6. telefone: Válido
        );

        // 2. ACT & 3. ASSERT (Execução e Verificação)
        mockMvc.perform(post(ENDPOINT_COMPLETO).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestInvalida)))

                // Verifica o status HTTP: Validação falha -> 400 Bad Request
                .andExpect(status().isBadRequest()).andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))

                // Verifica se a estrutura de erro contém os campos inválidos
                .andExpect(jsonPath("$.validationErrors").exists())
                .andExpect(jsonPath("$.validationErrors.nomeCompleto").exists())
                .andExpect(jsonPath("$.validationErrors.documento").exists());

        // 4. VERIFY (O service NUNCA deve ser chamado)
        verify(pessoaService, never()).cadastrarPessoa(any(PessoaRequest.class));
        verify(pessoaMapper, never()).toResponse(any(Pessoa.class));
    }

    // =================================================================
    // TESTE 6: cadastrarCompleto - Caminho Triste (E-mail ou Documento Duplicado ->
    // 400)
    // NOVO TESTE
    // =================================================================
    /**
     * Documentação: Testa o cenário onde o PessoaService lança uma
     * InvalidDataException (regra de negócio violada, ex: e-mail ou documento
     * duplicado) para o cadastro completo, garantindo que o Controller retorna
     * Status 400 com os campos personalizados do ProblemDetail.
     */
    @SuppressWarnings("null")
    @Test
    void deveRetornarStatus400QuandoEmailOuDocumentoCompletoForDuplicado() throws Exception {
        final String MENSAGEM_ERRO_NEGOCIO = "Documento já existe na base de dados.";
        final String REASON_CODE = "DOCUMENTO_DUPLICADO";
        final String FIELD = "documento";

        // 1. ARRANGE
        // Simula o Service lançando a exceção de negócio
        doThrow(new InvalidDataException(MENSAGEM_ERRO_NEGOCIO, REASON_CODE, FIELD)).when(pessoaService)
                .cadastrarPessoa(any(PessoaRequest.class));

        // 2. ACT & 3. ASSERT
        mockMvc.perform(post(ENDPOINT_COMPLETO).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestCompleto)))

                .andExpect(status().isBadRequest()).andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))

                // Verifica os campos customizados do ProblemDetail
                .andExpect(jsonPath("$.title").value("Dados de Entrada Inválidos"))
                .andExpect(jsonPath("$.reasonCode").value(REASON_CODE)).andExpect(jsonPath("$.field").value(FIELD));

        // 4. VERIFY
        verify(pessoaService, times(1)).cadastrarPessoa(any(PessoaRequest.class));
        verify(pessoaMapper, never()).toResponse(any(Pessoa.class));
    }
}
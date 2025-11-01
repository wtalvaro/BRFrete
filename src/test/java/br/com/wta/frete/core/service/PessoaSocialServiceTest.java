package br.com.wta.frete.core.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull; // NOVO: Para a senha
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import br.com.wta.frete.core.controller.dto.OAuth2UserRequestDTO;
import br.com.wta.frete.core.entity.Perfil;
import br.com.wta.frete.core.entity.Pessoa;
import br.com.wta.frete.core.repository.PerfilRepository;
import br.com.wta.frete.core.repository.PessoaRepository;

@ExtendWith(MockitoExtension.class)
public class PessoaSocialServiceTest {

    // Dependências: Mocks necessários para injetar no PessoaSocialService
    @Mock
    private PessoaRepository pessoaRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private PerfilRepository perfilRepository;

    // Injeta os mocks no serviço que estamos a testar
    @InjectMocks
    private PessoaSocialService pessoaSocialService;

    // Constante para a senha codificada (retorno simulado do PasswordEncoder)
    private final String SENHA_CODIFICADA_PLACEHOLDER = "placeholder-senha-codificada";

    // =================================================================
    // TESTE 1: Cenário Novo Cadastro Social (CORRIGIDO: Asserção de Senha)
    // =================================================================

    /**
     * Teste: deveCriarNovaPessoaQuandoSocialIdNaoExistir Objetivo: Quando um ID
     * Social é novo, o serviço deve criar, preencher e salvar uma nova entidade
     * Pessoa com os dados sociais e uma senha placeholder.
     */
    @Test
    void deveCriarNovaPessoaQuandoSocialIdNaoExistir() {
        // 1. ARRANGE (Preparação)
        String NOVO_SOCIAL_ID = "google-id-novo-9876";
        String NOVO_EMAIL = "novosocial@email.com";

        // DTO de requisição na ordem: socialId, nome, email, provedor
        OAuth2UserRequestDTO request = new OAuth2UserRequestDTO(NOVO_SOCIAL_ID, "Novo Utilizador Social", NOVO_EMAIL,
                "google");

        // Simulação 1.1: O repositório NÃO encontra pelo SocialId
        when(pessoaRepository.findBySocialIdWithPerfis(NOVO_SOCIAL_ID)).thenReturn(Optional.empty());

        // Simulação 1.2: O repositório NÃO encontra pelo Email
        when(pessoaRepository.findByEmailWithPerfis(NOVO_EMAIL)).thenReturn(Optional.empty());

        // Simulação 1.3: Simular o PasswordEncoder
        // Deve retornar o valor placeholder quando chamado com QUALQUER string
        when(passwordEncoder.encode(anyString())).thenReturn(SENHA_CODIFICADA_PLACEHOLDER);

        // Simulação 1.4: Simular o PerfilRepository
        Perfil perfilPadrao = new Perfil();
        perfilPadrao.setNomePerfil("LEAD");
        when(perfilRepository.findByNomePerfil(anyString())).thenReturn(perfilPadrao);

        // Simulação 1.5: Simula que o repositório SALVA e retorna a entidade com o ID
        // gerado.
        when(pessoaRepository.save(any(Pessoa.class))).thenAnswer(invocation -> {
            Pessoa pessoaSalva = invocation.getArgument(0);
            pessoaSalva.setId(1L);
            return pessoaSalva;
        });

        // 2. ACT (Ação)
        Pessoa pessoaCriada = pessoaSocialService.cadastrarOuObterPessoaSocial(request);

        // 3. ASSERT (Verificação)

        // 3.1. Verifica interações de busca e persistência
        verify(pessoaRepository, times(1)).findBySocialIdWithPerfis(NOVO_SOCIAL_ID);
        verify(pessoaRepository, times(1)).findByEmailWithPerfis(NOVO_EMAIL);
        verify(pessoaRepository, times(1)).save(any(Pessoa.class));
        verify(perfilRepository, times(1)).findByNomePerfil(anyString());
        verify(passwordEncoder, times(1)).encode(anyString());

        // 3.2. Verifica as regras de negócio
        assertEquals(NOVO_SOCIAL_ID, pessoaCriada.getSocialId(), "O socialId deve ser o da requisição.");
        assertTrue(pessoaCriada.isAtivo(), "Contas sociais devem ser ativadas imediatamente.");

        // CORREÇÃO: Verifica se a senha é o PLACEHOLDER CODIFICADO (o valor retornado
        // pelo mock)
        assertNotNull(pessoaCriada.getSenha(), "A conta social deve ter uma senha placeholder.");
        assertEquals(SENHA_CODIFICADA_PLACEHOLDER, pessoaCriada.getSenha(),
                "A senha deve corresponder ao valor codificado retornado pelo mock.");
    }

    // =================================================================
    // TESTE 2: Cenário Utilizador Social Existente (AGORA DEVE PASSAR)
    // =================================================================

    /**
     * Teste: deveRetornarPessoaExistenteQuandoSocialIdJaExiste Objetivo: Quando o
     * ID Social já existe, o serviço deve obter e retornar a Pessoa existente,
     * garantindo que NENHUMA operação de escrita é feita.
     */
    @Test
    void deveRetornarPessoaExistenteQuandoSocialIdJaExiste() {
        // 1. ARRANGE (Preparação)
        String ID_SOCIAL_EXISTENTE = "id-do-google-existente-123";
        // DTO de requisição
        OAuth2UserRequestDTO request = new OAuth2UserRequestDTO(ID_SOCIAL_EXISTENTE, "Utilizador Existente",
                "existente@social.com", "google");

        Pessoa pessoaExistente = new Pessoa();
        pessoaExistente.setId(50L);
        pessoaExistente.setSocialId(ID_SOCIAL_EXISTENTE);
        pessoaExistente.setNome(request.getNome());
        pessoaExistente.setAtivo(true);

        // Simulação: O repositório ENCONTRA a pessoa pelo socialId e retorna o objeto.
        when(pessoaRepository.findBySocialIdWithPerfis(ID_SOCIAL_EXISTENTE)).thenReturn(Optional.of(pessoaExistente));

        // 2. ACT (Ação)
        Pessoa resultado = pessoaSocialService.cadastrarOuObterPessoaSocial(request);

        // 3. ASSERT (Verificação)

        // 3.1. Verifica se a busca pelo socialId foi realizada
        verify(pessoaRepository, times(1)).findBySocialIdWithPerfis(ID_SOCIAL_EXISTENTE);

        // 3.2. Confirma que NENHUMA operação de criação foi chamada
        verify(pessoaRepository, never()).findByEmailWithPerfis(anyString());
        verify(pessoaRepository, never()).save(any(Pessoa.class));
        verify(passwordEncoder, never()).encode(anyString());
        verify(perfilRepository, never()).findByNomePerfil(anyString());

        // 3.3. Verifica se o objeto retornado é a pessoa existente
        assertEquals(pessoaExistente.getId(), resultado.getId(), "Deve retornar o ID da pessoa já existente.");
        assertEquals(pessoaExistente.getNome(), resultado.getNome(), "Deve retornar a entidade existente.");
    }
}
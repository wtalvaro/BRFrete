package br.com.wta.frete.core.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
// <<--- IMPORTAÇÕES NECESSÁRIAS
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
// --- FIM DAS IMPORTAÇÕES >>
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Import recomendado

import br.com.wta.frete.core.controller.dto.OAuth2UserRequestDTO;
import br.com.wta.frete.core.entity.Pessoa;
import br.com.wta.frete.core.entity.PessoaPerfil; // Import necessário

@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();
    private final PessoaService pessoaService;

    public CustomOAuth2UserService(PessoaService pessoaService) {
        this.pessoaService = pessoaService;
    }

    /**
     * MÉTODO: loadUser
     * Processa a resposta do provedor e sincroniza com o banco de dados.
     * Esta transação garante que o acesso a pessoa.getPerfis() (LAZY)
     * seja seguro, embora o JOIN FETCH no serviço subjacente seja a
     * melhor prática.
     */
    @Override
    @Transactional // Adicionar @Transactional aqui garante que a sessão JPA
                   // permaneça aberta durante a leitura dos perfis
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oauth2User = delegate.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint()
                .getUserNameAttributeName();

        String nome = oauth2User.getAttribute("name");
        String email = oauth2User.getAttribute("email");
        String socialId = oauth2User.getName();

        if (email == null) {
            throw new OAuth2AuthenticationException(
                    "O provedor OAuth2 (" + registrationId + ") não forneceu o endereço de e-mail.");
        }

        // 3. Processamento no serviço de negócio
        // ASSUMIMOS que o método abaixo (PessoaService) está usando
        // 'findBySocialIdWithPerfis' ou 'findByEmailWithPerfis' (com JOIN FETCH)
        OAuth2UserRequestDTO requestDTO = new OAuth2UserRequestDTO(socialId, nome, email, registrationId);
        Pessoa pessoa = pessoaService.cadastrarOuObterPessoaSocial(requestDTO);

        // <<--- INÍCIO DA ALTERAÇÃO CRÍTICA ---

        // 4. Mapear os Perfis (agora carregados via JOIN FETCH) para Autoridades
        // Esta chamada (pessoa.getPerfis()) é o ponto exato onde a
        // LazyInitializationException ocorreria se o JOIN FETCH não fosse usado
        // no PessoaService.
        Set<GrantedAuthority> authorities = mapPerfisToAuthorities(pessoa.getPerfis());

        // 5. Preparar atributos para o Principal
        Map<String, Object> attributes = new HashMap<>(oauth2User.getAttributes());
        attributes.put("pessoaId", pessoa.getId());

        // 6. Retornar o usuário com as autoridades CORRETAS
        return new DefaultOAuth2User(
                authorities, // <<--- MUDANÇA: Usamos a lista de perfis reais
                attributes,
                userNameAttributeName);

        // --- FIM DA ALTERAÇÃO >>
    }

    /**
     * Método auxiliar para converter nosso Set<PessoaPerfil> em uma coleção
     * que o Spring Security entende (GrantedAuthority).
     */
    private Set<GrantedAuthority> mapPerfisToAuthorities(Set<PessoaPerfil> perfis) {
        if (perfis == null || perfis.isEmpty()) {
            return Collections.emptySet();
        }

        return perfis.stream()
                // Mapeia cada Perfil para o formato "ROLE_NOME_DO_PERFIL"
                .map(pessoaPerfil -> new SimpleGrantedAuthority("ROLE_" + pessoaPerfil.getPerfil().getNomePerfil()))
                .collect(Collectors.toSet());
    }
}
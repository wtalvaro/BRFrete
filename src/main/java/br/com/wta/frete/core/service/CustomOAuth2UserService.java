package br.com.wta.frete.core.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import br.com.wta.frete.core.controller.dto.OAuth2UserRequestDTO; // Vamos criar este DTO
import br.com.wta.frete.core.entity.Pessoa;

/**
 * SERVIÇO: CustomOAuth2UserService
 * Propósito: Intercepta os dados do utilizador (Google/Facebook) após a
 * autenticação externa e integra-os ao nosso sistema de Pessoa.
 * Este é o ponto de coexistência entre o login social e os cadastros
 * existentes.
 */
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();
    private final PessoaService pessoaService;

    // Injeção de dependência do teu PessoaService
    public CustomOAuth2UserService(PessoaService pessoaService) {
        this.pessoaService = pessoaService;
    }

    /**
     * MÉTODO: loadUser
     * Processa a resposta do provedor (Google/Facebook) e sincroniza com o banco de
     * dados.
     */
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // 1. Carrega os dados brutos do provedor (padrão Spring Security)
        OAuth2User oauth2User = delegate.loadUser(userRequest);

        // Identifica o provedor (registrationId pode ser 'google' ou 'facebook')
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        // Extrai o nome, email e ID primário do utilizador
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint()
                .getUserNameAttributeName();

        // 2. Extrai dados para o nosso DTO
        String nome = oauth2User.getAttribute("name");
        String email = oauth2User.getAttribute("email");
        // 💥 NOVO: Obter o ID do utilizador (o "sub" do token, que é o nome principal)
        String socialId = oauth2User.getName(); // oauth2User.getName() geralmente é o "sub"

        // Proteção contra provedores que não retornam e-mail
        if (email == null) {
            throw new OAuth2AuthenticationException(
                    "O provedor OAuth2 (" + registrationId + ") não forneceu o endereço de e-mail.");
        }

        // 3. Processamento no serviço de negócio
        // Aqui chamamos o método que criará/obterá a Pessoa
        // 💥 NOVO DTO: Adicionar o socialId à requisição
        OAuth2UserRequestDTO requestDTO = new OAuth2UserRequestDTO(socialId, nome, email, registrationId);
        Pessoa pessoa = pessoaService.cadastrarOuObterPessoaSocial(requestDTO); // Novo método que vamos criar

        // 4. Retorna um objeto OAuth2User que será usado para a sessão do Spring
        // Security
        // 🚨 CORREÇÃO: Cria uma cópia MODIFICÁVEL do mapa de atributos.
        // O mapa original de oauth2User.getAttributes() é imutável.
        Map<String, Object> attributes = new HashMap<>(oauth2User.getAttributes());

        // Adicionamos o ID da Pessoa aos atributos, para ser facilmente acessível
        attributes.put("pessoaId", pessoa.getId());

        return new DefaultOAuth2User(
                // O Spring Security usa Collection<GrantedAuthority> para perfis/roles
                Collections.emptyList(),
                attributes,
                userNameAttributeName);
    }
}
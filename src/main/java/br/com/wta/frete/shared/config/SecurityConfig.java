package br.com.wta.frete.shared.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import br.com.wta.frete.core.service.CustomOAuth2UserService;

/**
 * CLASSE DE CONFIGURAÇÃO: SecurityConfig
 * Propósito: Define as políticas de segurança da aplicação (Spring Security).
 * Inclui a definição do PasswordEncoder e a cadeia de filtros de segurança.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // ❌ CORREÇÃO: Removemos a injeção de campo. Este campo não é mais necessário.
    // private final CustomOAuth2UserService customOAuth2UserService;

    /**
     * ❌ CORREÇÃO: Removemos o construtor que causava a dependência circular.
     * * public SecurityConfig(CustomOAuth2UserService customOAuth2UserService) {
     * this.customOAuth2UserService = customOAuth2UserService;
     * }
     */

    // Construtor sem argumentos (ou padrão) é implícito ou pode ser mantido vazio
    // se necessário.

    /**
     * BEAN: PasswordEncoder
     * Propósito: Fornece o algoritmo de criptografia BCrypt (padrão e recomendado).
     * * @return Uma instância de BCryptPasswordEncoder.
     */
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * BEAN: SecurityFilterChain (Perfil 'dev')
     * Propósito: Configura a segurança para o ambiente de DESENVOLVIMENTO ('dev').
     * * @param http Objeto HttpSecurity para configurar a segurança HTTP.
     * 
     * @param customOAuth2UserService O Serviço de usuário OAuth2.
     *                                ✅ CORREÇÃO: Injetado como PARÂMETRO do método,
     *                                garantindo que o Spring
     *                                inicialize todas as dependências (como
     *                                PessoaService) primeiro,
     *                                quebrando o ciclo.
     *                                * @return O SecurityFilterChain configurado.
     * @throws Exception Se ocorrer um erro na construção da cadeia de filtros.
     */
    @Bean
    @Profile("dev")
    SecurityFilterChain securityFilterChainDev(
            HttpSecurity http,
            CustomOAuth2UserService customOAuth2UserService // ✅ Injeção Corrigida aqui
    ) throws Exception {
        http
                // 1. Configuração do CSRF (Desabilitado para simplificar APIs REST)
                .csrf(AbstractHttpConfigurer::disable)

                // 2. Regras de Autorização de Requisição
                .authorizeHttpRequests(authorize -> authorize
                        // Rotas Públicas: Permitem acesso sem autenticação
                        .requestMatchers(
                                "/",
                                "/login",
                                "/api/pessoas/cadastro/**",
                                "/api/v1/ativacao/**",
                                "/public/**")
                        .permitAll()
                        // Rotas Protegidas: Exige que o utilizador esteja autenticado para todas as
                        // outras URLs
                        .anyRequest().authenticated())

                // 3. Configuração do Fluxo de Login OAuth2
                .oauth2Login(oauth2 -> oauth2
                        // Utiliza o nosso serviço personalizado
                        .userInfoEndpoint(userInfo -> userInfo
                                // ✅ Usamos a variável do PARÂMETRO aqui.
                                .userService(customOAuth2UserService))
                        // URL de sucesso após login bem-sucedido
                        .defaultSuccessUrl("/success", true))

                // 4. Configuração do Fluxo de Logout
                .logout(logout -> logout
                        .logoutSuccessUrl("/") // Redireciona para a página inicial após o logout
                        .permitAll());

        return http.build();
    }
}
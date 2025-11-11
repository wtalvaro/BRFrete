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
// import org.springframework.security.web.util.matcher.AntPathRequestMatcher; // Não é mais necessário

import br.com.wta.frete.core.service.CustomOAuth2UserService;

/**
 * CLASSE DE CONFIGURAÇÃO: SecurityConfig
 * Propósito: Define as políticas de segurança da aplicação (Spring Security).
 * Inclui a definição do PasswordEncoder e a cadeia de filtros de segurança.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

        /**
         * BEAN: PasswordEncoder
         * Propósito: Fornece o algoritmo de criptografia BCrypt (padrão e recomendado).
         * 
         * @return Uma instância de BCryptPasswordEncoder.
         */
        @Bean
        PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }

        /**
         * BEAN: SecurityFilterChain (Perfil 'dev')
         * Propósito: Configura a segurança para o ambiente de DESENVOLVIMENTO ('dev').
         * Libera o acesso a todos os endpoints do Actuator e rotas públicas/API.
         * * @param http Objeto para configurar o HttpSecurity.
         * 
         * @param customOAuth2UserService Serviço injetado para lidar com dados do
         *                                usuário OAuth2.
         * @return Uma SecurityFilterChain configurada.
         */
        @Bean
        @Profile("dev")
        SecurityFilterChain securityFilterChainDev(
                        HttpSecurity http,
                        CustomOAuth2UserService customOAuth2UserService) throws Exception {
                http
                                // 1. Configuração do CSRF
                                .csrf(AbstractHttpConfigurer::disable)

                                // 2. Regras de Autorização de Requisição
                                .authorizeHttpRequests(authorize -> authorize
                                                // Rotas Públicas (App e API /dev/): Permitem acesso sem autenticação
                                                .requestMatchers(
                                                                "/",
                                                                "/login",
                                                                "/api/**",
                                                                "/api/pessoas/cadastro/**",
                                                                "/api/v1/ativacao/**",
                                                                "/public/**",
                                                                // 🟢 ADIÇÃO PARA LIBERAR ACTUATOR: Libera todos os
                                                                // endpoints do Actuator
                                                                "/actuator/**",
                                                                // URLs estáticas
                                                                "/favicon.ico",
                                                                "/css/**",
                                                                "/js/**",
                                                                "/images/**",
                                                                "/webjars/**")
                                                .permitAll()

                                                // Rotas Protegidas: O que não foi permitido acima (se houver), exige
                                                // autenticação.
                                                .anyRequest().authenticated())

                                // 3. Configuração do Fluxo de Login OAuth2
                                .oauth2Login(oauth2 -> oauth2
                                                .userInfoEndpoint(userInfo -> userInfo
                                                                .userService(customOAuth2UserService))
                                                .defaultSuccessUrl("/success", true))

                                // 4. Configuração do Fluxo de Logout
                                .logout(logout -> logout
                                                .logoutSuccessUrl("/")
                                                .permitAll());

                return http.build();
        }
}
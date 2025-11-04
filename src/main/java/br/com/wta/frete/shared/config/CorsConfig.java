package br.com.wta.frete.shared.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(@NonNull CorsRegistry registry) {
        registry.addMapping("/api/**") // Aplica o CORS a todas as rotas que começam com /api
                // CORREÇÃO: Usa allowedOriginPatterns("*") para permitir todas as origens
                // enquanto allowCredentials(true) está ativo no modo DEV.
                .allowedOriginPatterns("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Métodos HTTP permitidos
                .allowedHeaders("*") // Permite todos os headers
                .allowCredentials(true) // Permite credenciais (cookies, Authorization headers, etc.)
                .maxAge(3600); // Tempo de cache do pre-flight request
    }
}
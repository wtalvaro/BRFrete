package br.com.wta.frete.core.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController // Usamos @RestController para devolver dados (JSON/XML)
public class SuccessRestController {

    /**
     * MÉTODO: success
     * Mapeia o caminho /success e retorna os detalhes do utilizador.
     */
    @GetMapping("/success")
    public Map<String, Object> success(@AuthenticationPrincipal OAuth2User oauth2User) {

        // Retorna todos os atributos para confirmação
        return Map.of(
                "status", "SUCCESS",
                "message", "Login e sincronização de dados bem-sucedidos.",
                "data", oauth2User.getAttributes());
    }
}
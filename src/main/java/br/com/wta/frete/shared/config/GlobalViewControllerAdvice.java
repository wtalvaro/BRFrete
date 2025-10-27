package br.com.wta.frete.shared.config; // Coloque no seu pacote de configurações

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import jakarta.servlet.http.HttpServletRequest;

/**
 * ControllerAdvice para injeção global da variável 'baseUrl' em todos os
 * Models.
 * Isso garante que a documentação (home.html) funcione corretamente em todos os
 * ambientes.
 */
@ControllerAdvice
public class GlobalViewControllerAdvice {

    /**
     * Injeta a variável 'baseUrl' no Model de todas as views.
     * O valor é construído dinamicamente a partir do pedido HTTP.
     */
    @ModelAttribute("baseUrl")
    public String addBaseUrlToModel(HttpServletRequest request) {

        // Constrói a URL base: esquema://servidor
        String baseUrl = request.getScheme() + "://" + request.getServerName();

        // Adiciona a porta se não for a padrão (80 ou 443)
        if (request.getServerPort() != 80 && request.getServerPort() != 443) {
            baseUrl += ":" + request.getServerPort();
        }

        // Retorna o valor, que será acessível em Thymeleaf como ${baseUrl}
        return baseUrl;
    }
}
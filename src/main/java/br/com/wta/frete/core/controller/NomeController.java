package br.com.wta.frete.core.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * NomeController:
 * Este é um Controller simples do Spring Boot, responsável por
 * lidar com os pedidos HTTP e retornar o nome "wagner".
 */
@RestController
public class NomeController {

    /**
     * getNome():
     * Mapeia o pedido GET para o caminho /nome.
     *
     * @return O nome "wagner" como uma String.
     *
     *         Instruções de Acesso:
     *         Após iniciares a aplicação Spring Boot, acede a:
     *         http://localhost:8080/nome
     */
    @GetMapping("/nome")
    public String getNome() {
        // A lógica é muito simples: apenas retornar a string desejada.
        System.out.println("Pedido recebido no endpoint /nome. Retornando 'wagner'.");
        return "wagner";
    }
}
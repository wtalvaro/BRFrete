package br.com.wta.frete.core.service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

/**
 * Serviço: TokenAtivacaoService
 * Descrição: Gerencia a criação e validação de tokens de ativação no Redis.
 * O Redis garante a expiração automática (TTL).
 */
@Service
public class TokenAtivacaoService {

    // Documentação: Objeto do Spring que simplifica operações de String no Redis.
    private final ValueOperations<String, String> valueOperations;

    // Constante para definir o tempo de expiração do token (24 horas)
    private static final long EXPIRACAO_HORAS = 24;

    /**
     * Injeção de dependência do StringRedisTemplate (configuração de Redis).
     */
    public TokenAtivacaoService(StringRedisTemplate redisTemplate) {
        this.valueOperations = redisTemplate.opsForValue();
    }

    /**
     * Método Educativo: criarToken
     * Gera um token único, associa-o ao ID da Pessoa e define o TTL de 24 horas.
     * 
     * @param pessoaId O ID da pessoa recém-cadastrada.
     * @return O token de ativação gerado.
     */
    public String criarToken(Long pessoaId) {
        // 1. Geração do token único (UUID é um bom gerador de IDs únicos)
        String token = UUID.randomUUID().toString().replace("-", "");

        // 2. Definindo a CHAVE e o VALOR no Redis
        // CHAVE: "ativacao:TOKEN" (ex: "ativacao:a1b2c3d4e5f6...")
        // VALOR: "ID_DA_PESSOA" (ex: "123")
        String chaveRedis = "ativacao:" + token;
        String valorRedis = pessoaId.toString();

        // 3. Salvamento no Redis com Tempo de Expiração (TTL)
        // O Redis salva o par chave-valor e o apaga automaticamente após 24 horas.
        valueOperations.set(chaveRedis, valorRedis, EXPIRACAO_HORAS, TimeUnit.HOURS);

        System.out.println("Token gerado: " + token + " e salvo no Redis por " + EXPIRACAO_HORAS + " horas.");

        return token;
    }

    /**
     * Método Educativo: obterIdPessoaPorToken
     * Busca o ID da Pessoa associado a um token.
     * 
     * @param token O token fornecido pelo usuário via URL.
     * @return O ID da Pessoa (Long) se o token for válido e não expirado, ou null.
     */
    public Long obterIdPessoaPorToken(String token) {
        String chaveRedis = "ativacao:" + token;

        // O Redis retorna o VALOR ("ID_DA_PESSOA") se a chave existir e não estiver
        // expirada.
        // Se a chave não existir (token expirado ou inválido), retorna null.
        String pessoaIdStr = valueOperations.get(chaveRedis);

        if (pessoaIdStr != null) {
            // Se encontrou, apaga o token imediatamente, pois ele já foi usado.
            removerToken(token);
            return Long.parseLong(pessoaIdStr);
        }

        return null;
    }

    /**
     * Remove o token do Redis após o uso ou em caso de reenvio.
     */
    public void removerToken(String token) {
        String chaveRedis = "ativacao:" + token;
        valueOperations.getOperations().delete(chaveRedis);
    }
}
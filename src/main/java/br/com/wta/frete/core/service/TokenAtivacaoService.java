package br.com.wta.frete.core.service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

/**
 * Serviço: TokenAtivacaoService Descrição: Gerencia a criação e validação de
 * tokens de ativação no Redis. O Redis garante a expiração automática (TTL). *
 * NOTA: A lógica foi ajustada para armazenar o ID como chave (activate:ID) e o
 * Token como valor para simplificar a validação em testes de integração e a
 * lógica de verificação.
 */
@Service
public class TokenAtivacaoService {

    private final ValueOperations<String, String> valueOperations;
    private static final long EXPIRACAO_HORAS = 24;

    // Prefixo usado no Teste (Chave: ID, Valor: Token)
    private static final String KEY_PREFIX_ACTIVATE = "activate:";

    // Prefixo usado no Lookup (Chave: Token, Valor: ID)
    private static final String KEY_PREFIX_LOOKUP = "ativacao:";

    public TokenAtivacaoService(StringRedisTemplate redisTemplate) {
        this.valueOperations = redisTemplate.opsForValue();
    }

    /**
     * Método Educativo: criarToken Gera um token único, associa-o ao ID da Pessoa
     * (como chave) e define o TTL. * @param pessoaId O ID da pessoa
     * recém-cadastrada.
     * 
     * @return O token de ativação gerado.
     */
    @SuppressWarnings("null")
    public String criarToken(Long pessoaId) {
        // 1. Geração do token único
        String token = UUID.randomUUID().toString().replace("-", "");

        // 2. Construção das chaves/valores
        // CHAVE 1 (Para Testes e Validação Simples: ID -> Token)
        String chavePessoaId = KEY_PREFIX_ACTIVATE + pessoaId;
        String valorToken = token;

        // CHAVE 2 (Para Ativação, Lookup: Token -> ID)
        String chaveToken = KEY_PREFIX_LOOKUP + token;
        String valorPessoaId = pessoaId.toString();

        // 3. Salvamento no Redis com Tempo de Expiração (TTL)

        // Salva a chave principal (para o teste assertar)
        valueOperations.set(chavePessoaId, valorToken, EXPIRACAO_HORAS, TimeUnit.HOURS);

        // Salva a chave de lookup (para o processo de ativação)
        valueOperations.set(chaveToken, valorPessoaId, EXPIRACAO_HORAS, TimeUnit.HOURS);

        System.out.println("Token gerado: " + token + " e salvo no Redis por " + EXPIRACAO_HORAS + " horas.");

        return token;
    }

    /**
     * Busca o ID da Pessoa associado a um token. (Esta é a lógica usada pelo
     * Controller de Ativação) * @param token O token fornecido pelo usuário via
     * URL.
     * 
     * @return O ID da Pessoa (Long) se o token for válido e não expirado, ou null.
     */
    public Long obterIdPessoaPorToken(String token) {
        // Usa a chave de lookup (Token -> ID)
        String chaveRedis = KEY_PREFIX_LOOKUP + token;

        // O Redis retorna o VALOR ("ID_DA_PESSOA") se a chave existir e não estiver
        // expirada.
        String pessoaIdStr = valueOperations.get(chaveRedis);

        if (pessoaIdStr != null) {
            // Se encontrou, apaga o token principal e o de lookup.
            removerToken(token);
            // Remove o token salvo no formato 'activate:ID' também (Para limpar a memória)
            String tokenLimpeza = valueOperations.get(KEY_PREFIX_ACTIVATE + pessoaIdStr);
            if (tokenLimpeza != null) {
                removerTokenPorId(Long.parseLong(pessoaIdStr));
            }
            return Long.parseLong(pessoaIdStr);
        }

        return null;
    }

    /**
     * Remove o token de lookup (chave: token) do Redis.
     */
    public void removerToken(String token) {
        valueOperations.getOperations().delete(KEY_PREFIX_LOOKUP + token);
    }

    /**
     * Remove o token principal (chave: ID) do Redis.
     */
    public void removerTokenPorId(Long pessoaId) {
        valueOperations.getOperations().delete(KEY_PREFIX_ACTIVATE + pessoaId);
    }
}
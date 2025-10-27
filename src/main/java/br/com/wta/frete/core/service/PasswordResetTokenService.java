// Local: br.com.wta.frete.web.exposed.service.PasswordResetTokenService.java

package br.com.wta.frete.core.service;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import java.time.Duration;

@Service
public class PasswordResetTokenService {

	// StringRedisTemplate é usado para armazenar Strings e gerenciar TTL
	private final StringRedisTemplate redisTemplate;
	private static final Duration TOKEN_EXPIRATION = Duration.ofMinutes(15);
	private static final String KEY_PREFIX = "pwd_reset:";

	public PasswordResetTokenService(StringRedisTemplate redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	/**
	 * Armazena o token de recuperação, associando-o ao ID do usuário, com um tempo
	 * de expiração.
	 * 
	 * @param userId O ID da pessoa (pessoa_id)
	 * @param token  O token de segurança (string aleatória)
	 */
	public void storeToken(Long userId, String token) {
		String key = KEY_PREFIX + userId;
		// O método set armazena o valor com o TTL (Time-to-Live)
		redisTemplate.opsForValue().set(key, token, TOKEN_EXPIRATION);
	}

	/**
	 * Recupera o token. Retorna null se ele expirou ou não existe.
	 * 
	 * @param userId O ID da pessoa
	 * @return O token, ou null
	 */
	public String getToken(Long userId) {
		String key = KEY_PREFIX + userId;
		return redisTemplate.opsForValue().get(key);
	}

	/**
	 * Remove o token após a senha ser redefinida.
	 * 
	 * @param userId O ID da pessoa
	 */
	public void deleteToken(Long userId) {
		String key = KEY_PREFIX + userId;
		redisTemplate.delete(key);
	}
}
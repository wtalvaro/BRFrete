package br.com.wta.frete.core.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.Duration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.ActiveProfiles;

/**
 * Classe de teste para verificar a correta interação entre o serviço de tokens
 * e o cache Valkey (Redis).
 */
@SpringBootTest
@ActiveProfiles("test")
class PasswordResetTokenServiceTest {

	@Autowired
	private PasswordResetTokenService service;

	@Autowired
	private StringRedisTemplate redisTemplate;

	/**
	 * Limpa o banco de dados do Valkey antes de cada teste.
	 */
	@BeforeEach
	@SuppressWarnings("deprecation") // Suprime o aviso de uso de flushDb()
	void setUp() {
		// Uso de RedisCallback<Void> para forçar a assinatura correta e limpar o cache.
		redisTemplate.execute((RedisCallback<Void>) connection -> {
			connection.flushDb();
			return null;
		});
	}

	@Test
	void shouldStoreAndRetrieveToken() {
		Long userId = 100L;
		String token = "TESTE_TOKEN_DE_RECUPERACAO";

		service.storeToken(userId, token);
		String retrievedToken = service.getToken(userId);

		assertEquals(token, retrievedToken, "O token recuperado deve ser idêntico ao armazenado.");
	}

	@Test
	void shouldReturnNullIfTokenDoesNotExist() {
		Long userId = 999L;
		assertNull(service.getToken(userId), "Deve retornar null para um token que nunca foi armazenado.");
	}

	@Test
	void shouldDeleteToken() {
		Long userId = 200L;
		String token = "TOKEN_TO_BE_DELETED";
		service.storeToken(userId, token);

		assertNotNull(service.getToken(userId));

		service.deleteToken(userId);

		assertNull(service.getToken(userId), "O token deve ser null após a exclusão.");
	}

	/**
	 * Testa se o token expira automaticamente no Valkey. Este teste manipula o TTL
	 * da chave diretamente no cache (Solução 2).
	 */
	@Test
	void tokenShouldExpireAutomatically() throws InterruptedException {
		Long userId = 400L;
		String token = "EXPIRE_TOKEN_ALT";

		// 1. Ação: Armazena o token (usa o TTL padrão de 15 min definido no serviço)
		service.storeToken(userId, token);

		// 2. Ação de Teste: Define a chave que o serviço criou.
		// É crucial saber o prefixo: KEY_PREFIX + userId, que neste caso é "pwd_reset:"
		String key = "pwd_reset:" + userId;

		// 3. Ação de Teste: Altera o TTL da chave no Valkey para 1 segundo
		// Isso força a expiração rápida para fins de teste.
		redisTemplate.expire(key, Duration.ofSeconds(1));

		// 4. Ação: Espera um pouco mais do que o tempo de expiração (1500ms = 1.5
		// segundos)
		Thread.sleep(1500);

		// 5. Asserção: O token deve ser null, indicando que o Valkey o removeu
		assertNull(service.getToken(userId), "O token deve ser NULL após o TTL ter expirado.");
	}
}
package br.com.wta.frete.shared.config;

import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * Configuração de Flyway exclusiva para o perfil "test".
 * * Objetivo: Forçar o Flyway a limpar o esquema do banco de dados (clean)
 * antes de aplicar as migrações (migrate) sempre que o contexto Spring
 * for carregado para um teste de integração (@SpringBootTest).
 * * Isso resolve o erro de "Migration checksum mismatch" e garante que cada
 * teste de integração comece com uma base de dados limpa.
 */
@Configuration
@Profile("test")
public class TestFlywayConfig {

    @Bean
    public FlywayMigrationStrategy flywayCleanMigrationStrategy() {
        // Esta estratégia executa flyway.clean() seguido por flyway.migrate()
        // O Flyway é autoconfigurado pelo Spring e injetado aqui implicitamente.
        return flyway -> {
            // Habilita e executa a limpeza, removendo o histórico e todas as tabelas.
            flyway.clean();

            // Re-aplica as migrações a partir do zero, registrando o checksum correto.
            flyway.migrate();
        };
    }
}
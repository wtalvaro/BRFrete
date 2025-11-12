# 🚚 BRFrete - Hub de Logística Social e Economia Circular

O **BRFrete** é um ecossistema digital inovador que integra um **Marketplace de Economia Circular** a uma **Plataforma de Logística Urbana** baseada em um modelo de Leilão Reverso de Fretes. Nosso foco é empoderar microempreendedores e transportadores avulsos (como catadores, motoristas de van e motofretistas), garantindo um fluxo constante de fretes e pagamento rápido e seguro.

## 🎯 Objetivo do Projeto

O objetivo principal é criar um hub digital para a economia circular e logística de última milha para microempreendedores, promovendo a formalização e a eficiência logística.

  * **Marketplace de Economia Circular (MEC):** Balcão de negociação para materiais de reuso, brechós e cotação de metais.
  * **Logística de Leilão Reverso:** Sistema de negociação onde transportadores competem com lances para fretes de clientes locais, garantindo o melhor custo-benefício e agilidade.
  * **Monetização Central:** Focada no **Transportador (Colaborador)** através de uma comissão sobre o valor do frete (`logistica.lances.valor_lance`) e serviços financeiros de alto valor, como o **Adiantamento de Recebíveis** (Factoring).

## 🛠️ Stack Tecnológica

A plataforma é construída como um **Monolito Modular** em Java, utilizando uma arquitetura em camadas (`Controller -> Service -> Repository`) para garantir a manutenibilidade e a clareza.

| Categoria | Tecnologia | Detalhe de Uso |
| :--- | :--- | :--- |
| **Backend** | Spring Boot 3+ | Desenvolvimento de APIs REST (`@RestController`) e Web MVC (`@Controller` + Thymeleaf). |
| **Persistência** | Spring Data JPA / Hibernate | Mapeamento Objeto-Relacional (ORM). |
| **Base de Dados** | PostgreSQL | Base de dados relacional com suporte a Schemas e tipos complexos (como o ENUM `logistica.status_servico`). |

## 📐 Estrutura Modular da Base de Dados (Schemas)

O projeto é modularizado de acordo com os schemas de base de dados, garantindo que a estrutura do código espelhe o modelo de dados.

| Schema | Foco Funcional | Exemplo de Entidade Principal |
| :--- | :--- | :--- |
| **`core`** | Identidades (Pessoas), Perfis, Contas Digitais. | `core.pessoas`, `core.contas_digitais`. |
| **`colaboradores`** | Detalhes de Transportadores, Sucateiros, Lojistas e Catadores. | `colaboradores.transportadores`, `colaboradores.veiculos`. |
| **`clientes`** | Detalhes do Cliente e Pedidos de Coleta. | `clientes.detalhes`, `clientes.pedidos_coleta. |
| **`logistica`** | Ordens de Serviço, Fretes, Leilões e Cálculos ANTT. | `logistica.ordens_servico`, `logistica.fretes`, `logistica.lances`. |
| **`marketplace`** | Venda/Doação de Produtos e Categorias. | `marketplace.produtos`, `marketplace.categorias`. |
| **`inventario`** | Controlo de Estoque/Stock de materiais. | `inventario.estoque`. |
| **`social`** | Avaliações, Comentários e Seguidores. | `social.avaliacoes`, `social.comentarios`. |

## 🏃 Como Correr o Projeto

1.  **Pré-requisitos:** Instale o Java 17+ e o PostgreSQL.
2.  **Configuração da Base de Dados (Flyway Ready):**
    Para garantir que as migrações do Flyway funcionem corretamente e para separar os ambientes, crie duas bases de dados no PostgreSQL:
    * `brfrete_dev` (Para desenvolvimento local)
    * `brfrete_test` (Para execução de testes de integração)
    
    Além disso, certifique-se de que os seguintes schemas existam em ambas as bases de dados (os scripts de criação de schemas podem ser rodados manualmente ou pelo Flyway se configurado):
    * `core`, `colaboradores`, `clientes`, `logistica`, `marketplace`, `inventario`, `social`.
    
    **Dica:** Os scripts SQL para a estrutura podem estar em `src/main/resources/static/docs/sql/schema.sql`.
    
3.  **Configuração do Ambiente (`application.properties`):**
    Crie o ficheiro `src/main/resources/application-dev.properties` e preencha-o com suas credenciais.
    
    **ATENÇÃO:** As credenciais sensíveis (senhas, chaves secretas) não devem ser versionadas. Use este modelo, substituindo os `[PLACEHOLDERS]` por seus valores.
    
    ```properties
    # Define o perfil de ambiente ativo
    spring.profiles.active=dev
    spring.application.name=wta-frete-api
    server.port=8080
    server.address=0.0.0.0

    # URL base para a aplicação (para callbacks de OAuth2, etc.)
    app.base.url=https://[SEU_ENDPOINT_NGROK_OU_LOCALHOST]

    # =======================================================
    # 2. CONFIGURAÇÃO DO BANCO DE DADOS (PostgreSQL)
    # =======================================================
    # DB de Desenvolvimento
    spring.datasource.url=jdbc:postgresql://localhost:[POSTGRESQL_PORT]/dev
    spring.datasource.username=[YOUR_DB_USERNAME]
    spring.datasource.password=[YOUR_DB_PASSWORD]
    
    # Configuração para o Flyway/Testes de Integração
    spring.test.datasource.url=jdbc:postgresql://localhost:[POSTGRESQL_PORT]/test
    spring.test.datasource.username=[YOUR_DB_USERNAME]
    spring.test.datasource.password=[YOUR_DB_PASSWORD]

    # Configurações do Pool de Conexões (HikariCP)
    spring.datasource.hikari.maximum-pool-size=10
    spring.datasource.hikari.minimum-idle=5
    
    # =======================================================
    # 5. CONFIGURAÇÃO DE EMAIL (SMTP - Gmail)
    # =======================================================
    spring.mail.host=smtp.gmail.com
    spring.mail.port=587
    spring.mail.username=[YOUR_GOOGLE_CLIENT_EMAIL]
    spring.mail.password=[YOUR_GENERATED_APP_PASSWORD] 
    spring.mail.properties.mail.smtp.from=noreply@brfrete.com
    
    # =======================================================
    # 6. CONFIGURAÇÃO OAUTH2 (LOGIN SOCIAL)
    # =======================================================
    # Registra o Google como Provedor OAuth2 para Login Social.
    spring.security.oauth2.client.registration.google.client-id=[YOUR_GOOGLE_CLIENT_ID]
    spring.security.oauth2.client.registration.google.client-secret=[YOUR_GOOGLE_CLIENT_SECRET]
    # Escopos (Permissões) solicitados ao Google: email e informações básicas do perfil.
    spring.security.oauth2.client.registration.google.scope=openid,email,profile
    ```

4.  **Execução:** Corra a aplicação principal usando o Maven ou diretamente pela IDE.

```bash
mvn spring-boot:run

## 📝 Licença

Este projeto está licenciado sob a Licença MIT - veja o arquivo [LICENSE](LICENSE) para detalhes.

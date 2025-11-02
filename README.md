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
2.  **Base de Dados:** Crie um banco de dados PostgreSQL e os Schemas necessários (`core`, `colaboradores`, `clientes`, etc.).
    **Dica:** Os scripts SQL para a estrutura podem estar em `src/main/resources/static/docs/sql/schema.sql`.
3.  **Configuração:** Edite o ficheiro `src/main/resources/application.properties` ou `application-dev.properties` com as credenciais do seu banco de dados.
4.  **Execução:** Corra a aplicação principal usando o Maven ou diretamente pela IDE.

```bash
mvn spring-boot:run
```

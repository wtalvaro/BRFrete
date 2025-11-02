# 🚚 BRFrete - Hub de Logística Social e Economia Circular

[cite\_start]O **BRFrete** é um ecossistema digital inovador que integra um **Marketplace de Economia Circular** a uma **Plataforma de Logística Urbana** baseada em um modelo de Leilão Reverso de Fretes[cite: 261]. [cite\_start]Nosso foco é empoderar microempreendedores e transportadores avulsos (como catadores, motoristas de van e motofretistas), garantindo um fluxo constante de fretes e pagamento rápido e seguro[cite: 234, 238].

## 🎯 Objetivo do Projeto

[cite\_start]O objetivo principal é criar um hub digital para a economia circular e logística de última milha para microempreendedores, promovendo a formalização e a eficiência logística[cite: 265].

  * [cite\_start]**Marketplace de Economia Circular (MEC):** Balcão de negociação para materiais de reuso, brechós e cotação de metais[cite: 260, 261].
  * [cite\_start]**Logística de Leilão Reverso:** Sistema de negociação onde transportadores competem com lances para fretes de clientes locais, garantindo o melhor custo-benefício e agilidade[cite: 261].
  * [cite\_start]**Monetização Central:** Focada no **Transportador (Colaborador)** através de uma comissão sobre o valor do frete (`logistica.lances.valor_lance`) e serviços financeiros de alto valor, como o **Adiantamento de Recebíveis** (Factoring)[cite: 236, 239, 244, 250].

## 🛠️ Stack Tecnológica

[cite\_start]A plataforma é construída como um **Monolito Modular** em Java, utilizando uma arquitetura em camadas (`Controller -> Service -> Repository`) para garantir a manutenibilidade e a clareza[cite: 291, 293, 335].

| Categoria | Tecnologia | Detalhe de Uso |
| :--- | :--- | :--- |
| **Backend** | Spring Boot 3+ | [cite\_start]Desenvolvimento de APIs REST (`@RestController`) e Web MVC (`@Controller` + Thymeleaf)[cite: 283, 343, 345]. |
| **Persistência** | Spring Data JPA / Hibernate | [cite\_start]Mapeamento Objeto-Relacional (ORM)[cite: 292]. |
| **Base de Dados** | PostgreSQL | [cite\_start]Base de dados relacional com suporte a Schemas e tipos complexos (como o ENUM `logistica.status_servico`)[cite: 334, 338, 339]. |

## 📐 Estrutura Modular da Base de Dados (Schemas)

[cite\_start]O projeto é modularizado de acordo com os schemas de base de dados, garantindo que a estrutura do código espelhe o modelo de dados[cite: 286, 289, 333].

| Schema | [cite\_start]Foco Funcional [cite: 3, 284] | [cite\_start]Exemplo de Entidade Principal [cite: 302, 303, 305] |
| :--- | :--- | :--- |
| **`core`** | Identidades (Pessoas), Perfis, Contas Digitais. | [cite\_start]`core.pessoas`, `core.contas_digitais`[cite: 22, 38]. |
| **`colaboradores`** | Detalhes de Transportadores, Sucateiros, Lojistas e Catadores. | [cite\_start]`colaboradores.transportadores`, `colaboradores.veiculos`[cite: 44, 60]. |
| **`clientes`** | Detalhes do Cliente e Pedidos de Coleta. | [cite\_start]`clientes.detalhes`, `clientes.pedidos_coleta`[cite: 68, 71]. |
| **`logistica`** | Ordens de Serviço, Fretes, Leilões e Cálculos ANTT. | [cite\_start]`logistica.ordens_servico`, `logistica.fretes`, `logistica.lances`[cite: 88, 97, 115]. |
| **`marketplace`** | Venda/Doação de Produtos e Categorias. | [cite\_start]`marketplace.produtos`, `marketplace.categorias`[cite: 132, 128]. |
| **`inventario`** | Controlo de Estoque/Stock de materiais. | [cite\_start]`inventario.estoque`[cite: 143]. |
| **`social`** | Avaliações, Comentários e Seguidores. | [cite\_start]`social.avaliacoes`, `social.comentarios`[cite: 150, 160]. |

## 📁 Estrutura do Código (Spring Boot)

A arquitetura do código reflete os schemas do banco de dados, sendo organizada por pacotes que correspondem aos módulos funcionais.

```
src/main/java/com.seuprojeto.plataforma
├── core/
│   ├── api/          # Controladores REST (JSON, /api/v1/...)
│   ├── web/          # Controladores Web (HTML, /app/...)
│   ├── entity/       # Mapeamento JPA (e.g., Pessoa.java)
│   ├── repository/   # JpaRepository (e.g., PessoaRepository.java)
│   └── service/      # Lógica de Negócio (e.g., PessoaService.java)
├── colaboradores/
│   ├── entity/       # (e.g., Transportador.java, Veiculo.java)
... (outros schemas)
```

> [cite\_start]O mapeamento da tabela `core.pessoas` para o JPA é feito utilizando `@Table(name = "pessoas", schema = "core")`[cite: 310, 334].

## 🏃 Como Correr o Projeto

1.  **Pré-requisitos:** Instale o Java 17+ e o PostgreSQL.
2.  [cite\_start]**Base de Dados:** Crie um banco de dados PostgreSQL e os Schemas necessários (`core`, `colaboradores`, `clientes`, etc.)[cite: 9, 10, 11, 12, 13, 14, 15, 16, 17].
      * [cite\_start]**Dica:** Os scripts SQL para a estrutura podem estar em `src/main/resources/static/docs/sql/schema.sql`[cite: 228].
3.  [cite\_start]**Configuração:** Edite o ficheiro `src/main/resources/application.properties` ou `application-dev.properties` [cite: 227] com as credenciais do seu banco de dados.
4.  **Execução:** Corra a aplicação principal usando o Maven ou diretamente pela IDE.

<!-- end list -->

```bash
mvn spring-boot:run
```

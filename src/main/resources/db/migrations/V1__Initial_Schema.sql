-- ######################################################################
-- SCRIPT COMPLETO PARA CRIAÇÃO DE ESQUEMA DE BASE DE DADOS (POSTGRESQL)
-- Versão Final com Funcionalidades de CHAT e Q&A (Perguntas/Respostas)
-- ######################################################################


-- ======================================================================
-- 0. LIMPEZA DO AMBIENTE (DROP IF EXISTS)
-- Garante que o script pode ser executado repetidamente e na ordem correta
-- ======================================================================

-- SCHEMA SOCIAL
DROP TABLE IF EXISTS social.seguidores CASCADE;
DROP TABLE IF EXISTS social.comentarios CASCADE;
DROP TABLE IF EXISTS social.avaliacoes CASCADE;

-- SCHEMA INVENTARIO
DROP TABLE IF EXISTS inventario.estoque_produto CASCADE; -- NOVO: Estoque de Produto
DROP TABLE IF EXISTS inventario.estoque CASCADE;

-- SCHEMA MARKETPLACE (Inclui a nova Q&A)
DROP TABLE IF EXISTS marketplace.perguntas_produto CASCADE; -- NOVO Q&A: Perguntas/Respostas do Produto
DROP TABLE IF EXISTS marketplace.produtos CASCADE;
DROP TABLE IF EXISTS marketplace.categorias CASCADE;

-- SCHEMA LOGISTICA
DROP TABLE IF EXISTS logistica.lances CASCADE;
DROP TABLE IF EXISTS logistica.itens_frete CASCADE;
DROP TABLE IF EXISTS logistica.fretes CASCADE;
DROP TABLE IF EXISTS logistica.ordens_servico CASCADE;
DROP TABLE IF EXISTS logistica.antt_parametros CASCADE;
DROP TABLE IF EXISTS logistica.cotacoes_materiais CASCADE;
DROP TABLE IF EXISTS logistica.modalidades_frete CASCADE;
DROP TABLE IF EXISTS logistica.status_leilao CASCADE;

-- SCHEMA CLIENTES
DROP TABLE IF EXISTS clientes.pedidos_coleta CASCADE;
DROP TABLE IF EXISTS clientes.detalhes CASCADE;

-- SCHEMA COLABORADORES
DROP TABLE IF EXISTS colaboradores.veiculos CASCADE;
DROP TABLE IF EXISTS colaboradores.transportadores CASCADE;
DROP TABLE IF EXISTS colaboradores.sucateiros CASCADE;
DROP TABLE IF EXISTS colaboradores.lojistas CASCADE;
DROP TABLE IF EXISTS colaboradores.catadores CASCADE;

-- SCHEMA CORE (Tabelas de Chat e MESTRAS)
DROP TABLE IF EXISTS core.mensagens CASCADE; -- NOVO CHAT
DROP TABLE IF EXISTS core.participantes_conversa CASCADE; -- NOVO CHAT
DROP TABLE IF EXISTS core.conversas CASCADE; -- NOVO CHAT
DROP TABLE IF EXISTS core.pessoa_perfil CASCADE;
DROP TABLE IF EXISTS core.contas_digitais CASCADE;
DROP TABLE IF EXISTS core.perfis CASCADE;
DROP TABLE IF EXISTS core.pessoas CASCADE;

-- Apaga Tipos de Dados Personalizados
DROP TYPE IF EXISTS logistica.status_servico;


-- ======================================================================
-- 1. CRIAÇÃO DE SCHEMAS
-- ======================================================================

CREATE SCHEMA IF NOT EXISTS core;
CREATE SCHEMA IF NOT EXISTS colaboradores;
CREATE SCHEMA IF NOT EXISTS clientes;
CREATE SCHEMA IF NOT EXISTS logistica;
CREATE SCHEMA IF NOT EXISTS inventario;
CREATE SCHEMA IF NOT EXISTS social;
CREATE SCHEMA IF NOT EXISTS marketplace;


-- ======================================================================
-- 2. TIPO DE DADOS PERSONALIZADO (ENUM)
-- ======================================================================

-- Define o tipo ENUM para o status de uma Ordem de Serviço
CREATE TYPE logistica.status_servico AS ENUM (
    'PENDENTE',
    'CONFIRMADO',
    'COLETADO',
    'EM_TRANSPORTE',
    'ENTREGUE',
    'CANCELADO'
);


-- ======================================================================
-- 3. SCHEMA CORE: Identidades e Perfis M:M (Tabelas MESTRAS)
-- ======================================================================

CREATE TABLE core.pessoas (
	pessoa_id bigserial NOT NULL,
	nome varchar(255) NOT NULL,
	documento varchar(18) NOT NULL,
	email varchar(100) NOT NULL,
	telefone varchar(20) NULL,
	senha varchar(255) NOT NULL,
	data_cadastro timestamp DEFAULT now() NULL,
	ativo bool DEFAULT false NOT NULL,
	is_colaborador bool DEFAULT false NOT NULL,
	is_cliente bool DEFAULT false NOT NULL,
	CONSTRAINT pessoas_documento_key UNIQUE (documento),
	CONSTRAINT pessoas_email_key UNIQUE (email),
    -- Usa o operador ~* para Regex case-insensitive
    CONSTRAINT chk_email_valido
	    CHECK (email ~* '^[A-Za-z0-9._-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,4}$'),
	CONSTRAINT pessoas_pkey PRIMARY KEY (pessoa_id)
);

CREATE TABLE core.perfis (
    perfil_id SERIAL PRIMARY KEY,
    nome_perfil VARCHAR(50) UNIQUE NOT NULL,
    descricao TEXT
);

CREATE TABLE core.pessoa_perfil (
    pessoa_id BIGINT NOT NULL,
    perfil_id INTEGER NOT NULL,
    PRIMARY KEY (pessoa_id, perfil_id),
    FOREIGN KEY (pessoa_id) REFERENCES core.pessoas(pessoa_id) ON DELETE CASCADE,
    FOREIGN KEY (perfil_id) REFERENCES core.perfis(perfil_id) ON DELETE RESTRICT
);

CREATE TABLE core.contas_digitais (
    pessoa_id BIGINT PRIMARY KEY,
    conta_uuid VARCHAR(64) UNIQUE NOT NULL,
    status_kyc VARCHAR(20) NOT NULL DEFAULT 'PENDENTE',
    data_abertura TIMESTAMP WITHOUT TIME ZONE DEFAULT now(),
    FOREIGN KEY (pessoa_id) REFERENCES core.pessoas(pessoa_id) ON DELETE CASCADE
);

-- ======================================================================
-- 3.1. TABELAS PARA SISTEMA DE CHAT PRIVADO (CORE)
-- Adiciona funcionalidade de comunicação Ponto-a-Ponto (P2P) entre usuários
-- ======================================================================

-- 1. Tabela de Conversas (Chats)
CREATE TABLE core.conversas (
    conversa_id BIGSERIAL PRIMARY KEY,
    tipo_conversa VARCHAR(20) NOT NULL DEFAULT 'PRIVADA', -- Ex: PRIVADA, GRUPO, SUPORTE
    data_criacao TIMESTAMP WITHOUT TIME ZONE DEFAULT now(),
    ultima_mensagem_em TIMESTAMP WITHOUT TIME ZONE DEFAULT now()
);

-- 2. Tabela de Participantes da Conversa (Relação M:M)
CREATE TABLE core.participantes_conversa (
    conversa_id BIGINT NOT NULL,
    pessoa_id BIGINT NOT NULL,
    data_entrada TIMESTAMP WITHOUT TIME ZONE DEFAULT now(),
    PRIMARY KEY (conversa_id, pessoa_id),
    FOREIGN KEY (conversa_id) REFERENCES core.conversas(conversa_id) ON DELETE CASCADE,
    FOREIGN KEY (pessoa_id) REFERENCES core.pessoas(pessoa_id) ON DELETE CASCADE
);

-- 3. Tabela de Mensagens
CREATE TABLE core.mensagens (
    mensagem_id BIGSERIAL PRIMARY KEY,
    conversa_id BIGINT NOT NULL,
    autor_id BIGINT NOT NULL,
    conteudo TEXT NOT NULL,
    data_envio TIMESTAMP WITHOUT TIME ZONE DEFAULT now(),
    is_lida BOOLEAN NOT NULL DEFAULT false,
    FOREIGN KEY (conversa_id) REFERENCES core.conversas(conversa_id) ON DELETE CASCADE,
    FOREIGN KEY (autor_id) REFERENCES core.pessoas(pessoa_id) ON DELETE RESTRICT
);


-- ======================================================================
-- 4. SCHEMAS ESPECÍFICOS DE COLABORADORES
-- ======================================================================

CREATE TABLE colaboradores.transportadores (
    pessoa_id BIGINT PRIMARY KEY,
    licenca_transporte VARCHAR(100),
    FOREIGN KEY (pessoa_id) REFERENCES core.pessoas(pessoa_id) ON DELETE CASCADE
);

CREATE TABLE colaboradores.sucateiros (
    pessoa_id BIGINT PRIMARY KEY,
    razao_social VARCHAR(255) NOT NULL,
    cnpj_secundario VARCHAR(18),
    licenca_ambiental VARCHAR(100),
    endereco_patio TEXT NOT NULL,
    FOREIGN KEY (pessoa_id) REFERENCES core.pessoas(pessoa_id) ON DELETE CASCADE
);

CREATE TABLE colaboradores.lojistas (
    pessoa_id BIGINT PRIMARY KEY,
    nome_loja VARCHAR(255) NOT NULL,
    endereco_coleta TEXT NOT NULL,
    horario_atendimento VARCHAR(100),
    FOREIGN KEY (pessoa_id) REFERENCES core.pessoas(pessoa_id) ON DELETE CASCADE
);

CREATE TABLE colaboradores.catadores (
    pessoa_id BIGINT PRIMARY KEY,
    data_nascimento DATE,
    associacao_id INTEGER,
    area_atuacao_geografica TEXT,
    FOREIGN KEY (pessoa_id) REFERENCES core.pessoas(pessoa_id) ON DELETE CASCADE
);

CREATE TABLE colaboradores.veiculos (
    veiculo_id SERIAL PRIMARY KEY,
    transportador_pessoa_id BIGINT NOT NULL,
    matricula VARCHAR(20) UNIQUE NOT NULL,
    tipo_veiculo VARCHAR(50) NOT NULL,
    capacidade_peso_kg NUMERIC(10, 2) NOT NULL,
    capacidade_volume_m3 NUMERIC(10, 2),
    status_veiculo VARCHAR(20) NOT NULL DEFAULT 'ATIVO',
    FOREIGN KEY (transportador_pessoa_id) REFERENCES colaboradores.transportadores(pessoa_id) ON DELETE CASCADE
);


-- ======================================================================
-- 5. SCHEMA CLIENTES
-- ======================================================================

CREATE TABLE clientes.detalhes (
    pessoa_id BIGINT PRIMARY KEY,
    tipo_cliente VARCHAR(20) NOT NULL,
    preferencias_coleta TEXT,
    FOREIGN KEY (pessoa_id) REFERENCES core.pessoas(pessoa_id) ON DELETE CASCADE
);

CREATE TABLE clientes.pedidos_coleta (
    pedido_id SERIAL PRIMARY KEY,
    cliente_id BIGINT NOT NULL,
    descricao_pedido TEXT NOT NULL,
    data_solicitacao TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (cliente_id) REFERENCES clientes.detalhes(pessoa_id) ON DELETE RESTRICT
);


-- ======================================================================
-- 6. SCHEMA LOGISTICA
-- ======================================================================

CREATE TABLE logistica.modalidades_frete (
    modalidade_id SERIAL PRIMARY KEY,
    nome_modalidade VARCHAR(50) UNIQUE NOT NULL
);

CREATE TABLE logistica.status_leilao (
    status_id SERIAL PRIMARY KEY,
    nome_status VARCHAR(50) UNIQUE NOT NULL
);

CREATE TABLE logistica.antt_parametros (
    parametro_id SERIAL PRIMARY KEY,
    chave VARCHAR(100) UNIQUE NOT NULL,
    valor NUMERIC(10, 4) NOT NULL,
    descricao TEXT,
    data_vigencia DATE DEFAULT now()
);

CREATE TABLE logistica.ordens_servico (
    ordem_id BIGSERIAL PRIMARY KEY,
    cliente_solicitante_id BIGINT NOT NULL,
    transportador_designado_id BIGINT,
    data_solicitacao TIMESTAMP WITHOUT TIME ZONE DEFAULT now(),
    data_prevista_coleta DATE,
    endereco_coleta TEXT NOT NULL,
    cep_coleta VARCHAR(8) NOT NULL,
    cep_destino VARCHAR(8) NOT NULL,
    status logistica.status_servico NOT NULL DEFAULT 'PENDENTE'::logistica.status_servico,
    FOREIGN KEY (cliente_solicitante_id) REFERENCES clientes.detalhes(pessoa_id) ON DELETE RESTRICT,
    FOREIGN KEY (transportador_designado_id) REFERENCES colaboradores.transportadores(pessoa_id) ON DELETE SET NULL
);

CREATE TABLE logistica.fretes (
    frete_id SERIAL PRIMARY KEY,
    ordem_servico_id BIGINT NOT NULL,
    modalidade_id INTEGER NOT NULL,
    status_leilao_id INTEGER NOT NULL,
    data_expiracao_negociacao TIMESTAMP WITH TIME ZONE,
    preco_sugerido NUMERIC(10, 2),
    antt_piso_minimo NUMERIC(10, 2),
    custo_base_mercado NUMERIC(10, 2),
    distancia_km NUMERIC(10, 2),
    transportador_selecionado_id BIGINT,
    FOREIGN KEY (ordem_servico_id) REFERENCES logistica.ordens_servico(ordem_id) ON DELETE CASCADE,
    FOREIGN KEY (modalidade_id) REFERENCES logistica.modalidades_frete(modalidade_id) ON DELETE RESTRICT,
    FOREIGN KEY (status_leilao_id) REFERENCES logistica.status_leilao(status_id) ON DELETE RESTRICT,
    FOREIGN KEY (transportador_selecionado_id) REFERENCES colaboradores.transportadores(pessoa_id) ON DELETE SET NULL
);

CREATE TABLE logistica.itens_frete (
    item_frete_id SERIAL PRIMARY KEY,
    frete_id INTEGER NOT NULL,
    descricao TEXT NOT NULL,
    tipo_material VARCHAR(100),
    peso_estimado_kg NUMERIC(10, 2) NOT NULL,
    volume_estimado_m3 NUMERIC(10, 2),
    FOREIGN KEY (frete_id) REFERENCES logistica.fretes(frete_id) ON DELETE CASCADE
);

CREATE TABLE logistica.lances (
    lance_id SERIAL PRIMARY KEY,
    frete_id INTEGER NOT NULL,
    transportador_id BIGINT NOT NULL,
    valor_lance NUMERIC(10, 2) NOT NULL,
    data_lance TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    is_vencedor BOOLEAN DEFAULT false,
    motivo_cancelamento TEXT,
    FOREIGN KEY (frete_id) REFERENCES logistica.fretes(frete_id) ON DELETE CASCADE,
    FOREIGN KEY (transportador_id) REFERENCES colaboradores.transportadores(pessoa_id) ON DELETE RESTRICT
);

CREATE TABLE logistica.cotacoes_materiais (
    cotacao_id SERIAL PRIMARY KEY,
    material_nome VARCHAR(100) UNIQUE NOT NULL,
    preco_medio_kg NUMERIC(10, 2) NOT NULL,
    unidade_medida VARCHAR(10) NOT NULL DEFAULT 'KG',
    data_atualizacao TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);


-- ======================================================================
-- 7. SCHEMA MARKETPLACE
-- ======================================================================

CREATE TABLE marketplace.categorias (
    categoria_id SERIAL PRIMARY KEY,
    nome_categoria VARCHAR(100) UNIQUE NOT NULL,
    tipo_geral VARCHAR(20) NOT NULL
);

CREATE TABLE marketplace.produtos (
    produto_id SERIAL PRIMARY KEY,
    vendedor_id BIGINT NOT NULL,
    categoria_id INTEGER NOT NULL,
    titulo VARCHAR(255) NOT NULL,
    descricao TEXT,
    preco NUMERIC(10, 2) NOT NULL,
    quantidade INTEGER NOT NULL DEFAULT 1,
    unidade_medida VARCHAR(10) DEFAULT 'UN',
    data_publicacao TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    is_disponivel BOOLEAN NOT NULL DEFAULT true,
    is_doacao BOOLEAN NOT NULL DEFAULT false,
    FOREIGN KEY (vendedor_id) REFERENCES core.pessoas(pessoa_id) ON DELETE RESTRICT,
    FOREIGN KEY (categoria_id) REFERENCES marketplace.categorias(categoria_id) ON DELETE RESTRICT,
    CONSTRAINT check_preco_doacao
        CHECK (
            (is_doacao = TRUE AND preco = 0.00) OR
            (is_doacao = FALSE AND preco > 0.00)
        )
);

-- ======================================================================
-- 7.1. TABELA DE PERGUNTAS E RESPOSTAS (Q&A) DO PRODUTO (MARKETPLACE)
-- Permite que usuários façam perguntas públicas sobre um produto.
-- ======================================================================

CREATE TABLE marketplace.perguntas_produto (
    pergunta_id BIGSERIAL PRIMARY KEY,
    produto_id INTEGER NOT NULL,
    autor_id BIGINT NOT NULL,
    texto_conteudo TEXT NOT NULL,
    data_publicacao TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    pergunta_pai_id BIGINT, -- Usado para respostas (links para a pergunta original)
    is_publica BOOLEAN NOT NULL DEFAULT true, -- Permite moderação ou ocultação
    FOREIGN KEY (produto_id) REFERENCES marketplace.produtos(produto_id) ON DELETE CASCADE,
    FOREIGN KEY (autor_id) REFERENCES core.pessoas(pessoa_id) ON DELETE RESTRICT,
    FOREIGN KEY (pergunta_pai_id) REFERENCES marketplace.perguntas_produto(pergunta_id) ON DELETE SET NULL
);

-- Adiciona um índice para consultas rápidas na página do produto
CREATE INDEX idx_perguntas_produto_id ON marketplace.perguntas_produto (produto_id);


-- ======================================================================
-- 8. SCHEMA INVENTARIO (Com Modelo Otimizado)
-- ======================================================================

-- Tabela original: Estoque de Materiais/Sucata a Granel (para Sucateiros)
CREATE TABLE inventario.estoque (
    estoque_id BIGSERIAL PRIMARY KEY,
    gestor_id BIGINT NOT NULL,
    tipo_material VARCHAR(50) NOT NULL,
    quantidade_kg NUMERIC(10, 2) NOT NULL,
    data_entrada TIMESTAMP WITHOUT TIME ZONE DEFAULT now(),
    localizacao TEXT,
    FOREIGN KEY (gestor_id) REFERENCES core.pessoas(pessoa_id) ON DELETE RESTRICT
);

-- Tabela NOVO: Estoque para Produtos do Marketplace
-- Gerencia o saldo de itens que estão ativamente anunciados no 'marketplace.produtos'.
CREATE TABLE inventario.estoque_produto (
    estoque_produto_id BIGSERIAL PRIMARY KEY,
    produto_id INTEGER UNIQUE NOT NULL,
    quantidade_disponivel INTEGER NOT NULL,
    data_ultima_entrada TIMESTAMP WITHOUT TIME ZONE DEFAULT now(),
    FOREIGN KEY (produto_id)
        REFERENCES marketplace.produtos(produto_id)
        ON DELETE CASCADE,
    CONSTRAINT check_quantidade_positiva
        CHECK (quantidade_disponivel >= 0)
);


-- ======================================================================
-- 9. SCHEMA SOCIAL
-- ======================================================================

CREATE TABLE social.avaliacoes (
    avaliacao_id BIGSERIAL PRIMARY KEY,
    avaliador_id BIGINT NOT NULL,
    avaliado_id BIGINT NOT NULL,
    ordem_servico_id BIGINT,
    produto_id INTEGER,
    pontuacao SMALLINT NOT NULL,
    comentario TEXT,
    data_avaliacao TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (avaliador_id) REFERENCES core.pessoas(pessoa_id) ON DELETE RESTRICT,
    FOREIGN KEY (avaliado_id) REFERENCES core.pessoas(pessoa_id) ON DELETE RESTRICT,
    FOREIGN KEY (ordem_servico_id) REFERENCES logistica.ordens_servico(ordem_id) ON DELETE SET NULL,
    FOREIGN KEY (produto_id) REFERENCES marketplace.produtos(produto_id) ON DELETE SET NULL,
    CONSTRAINT check_related_entity CHECK (
        (ordem_servico_id IS NOT NULL AND produto_id IS NULL) OR
        (ordem_servico_id IS NULL AND produto_id IS NOT NULL)
    )
);

CREATE TABLE social.comentarios (
    comentario_id BIGSERIAL PRIMARY KEY,
    autor_id BIGINT NOT NULL,
    produto_id INTEGER NOT NULL,
    texto_comentario TEXT NOT NULL,
    data_comentario TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    comentario_pai_id BIGINT,
    FOREIGN KEY (autor_id) REFERENCES core.pessoas(pessoa_id) ON DELETE RESTRICT,
    FOREIGN KEY (produto_id) REFERENCES marketplace.produtos(produto_id) ON DELETE CASCADE,
    FOREIGN KEY (comentario_pai_id) REFERENCES social.comentarios(comentario_id) ON DELETE SET NULL
);

CREATE TABLE social.seguidores (
    seguidor_id BIGINT NOT NULL,
    seguido_id BIGINT NOT NULL,
    PRIMARY KEY (seguidor_id, seguido_id),
    FOREIGN KEY (seguidor_id) REFERENCES core.pessoas(pessoa_id) ON DELETE CASCADE,
    FOREIGN KEY (seguido_id) REFERENCES core.pessoas(pessoa_id) ON DELETE CASCADE,
    CONSTRAINT check_self_follow CHECK (seguidor_id <> seguido_id)
);

-- Adicionar a nova coluna 'social_id' à tabela core.pessoas
-- O Google ID (sub) é geralmente uma string de 21 caracteres, 255 é seguro.
ALTER TABLE core.pessoas
ADD COLUMN social_id VARCHAR(255) UNIQUE;

-- Tornar a coluna 'documento' NULA/OPCIONAL
-- Isto é crucial, pois um registo social via Google pode não ter um documento na hora.
ALTER TABLE core.pessoas
ALTER COLUMN documento DROP NOT NULL;
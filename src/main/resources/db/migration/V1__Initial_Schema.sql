-- ######################################################################
-- SCRIPT COMPLETO PARA CRIAÇÃO DE ESQUEMA DE BASE DE DADOS (POSTGRESQL)
-- Versão Final com Funcionalidades de CHAT, Q&A e PREÇOS CUSTOMIZADOS POR VEÍCULO
-- CORRIGIDO: Problemas de dependência de Foreign Key no Flyway.
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
DROP TABLE IF EXISTS colaboradores.metricas_transportador CASCADE;
DROP TABLE IF EXISTS colaboradores.veiculos CASCADE;
DROP TABLE IF EXISTS colaboradores.transportadores CASCADE;
DROP TABLE IF EXISTS colaboradores.sucateiros CASCADE;
DROP TABLE IF EXISTS colaboradores.lojistas CASCADE;
DROP TABLE IF EXISTS colaboradores.catadores CASCADE;
DROP TABLE IF EXISTS colaboradores.horarios_operacao CASCADE;

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

-- NOVO ENUM: core.status_kyc_enum (Baseado em StatusKYC.java)
DROP TYPE IF EXISTS core.status_kyc_enum CASCADE;
CREATE TYPE core.status_kyc_enum AS ENUM (
    'PENDENTE',
    'EM_REVISAO',
    'APROVADO',
    'REPROVADO'
);

-- NOVO ENUM: core.tipo_conversa_enum (Baseado em TipoConversa.java)
DROP TYPE IF EXISTS core.tipo_conversa_enum CASCADE;
CREATE TYPE core.tipo_conversa_enum AS ENUM (
    'PRIVADA',
    'GRUPO',
    'SUPORTE'
);

-- NOVO ENUM: colaboradores.status_veiculo_enum (Baseado em StatusVeiculo.java)
DROP TYPE IF EXISTS colaboradores.status_veiculo_enum CASCADE;
CREATE TYPE colaboradores.status_veiculo_enum AS ENUM (
    'DISPONIVEL',
    'EM_MANUTENCAO',
    'EM_VIAGEM',
    'INATIVO'
);

-- NOVO ENUM: colaboradores.tipo_veiculo_enum (MÁXIMA VARIEDADE)
DROP TYPE IF EXISTS colaboradores.tipo_veiculo_enum CASCADE;
CREATE TYPE colaboradores.tipo_veiculo_enum AS ENUM (
    -- VEÍCULOS LEVES E URBANOS
    'VEICULO_UTILITARIO',   -- Como furgão ou caminhonete de carga (Fiorino/Saveiro)
    'VUC',                  -- Veículo Urbano de Carga (caminhão 3/4)
    'FURGAO',               -- Van fechada para transporte de carga

    -- CAMINHÕES RÍGIDOS (POR EIXO)
    'TOCO_2_EIXOS',         -- Caminhão Semipesado (2 eixos simples)
    'TRUCK_3_EIXOS',        -- Caminhão Pesado (3 eixos)
    'BITRUCK_4_EIXOS',      -- Caminhão com 4 eixos (2 na frente, 2 atrás)

    -- CARRETAS E COMBINAÇÕES SIMPLES
    'CAVALO_MECANICO_SIMPLES',-- Apenas a cabine tratora (Cavalo 4x2)
    'CARRETA_2_EIXOS',      -- Cavalo Simples + Semirreboque 2 eixos
    'CARRETA_3_EIXOS',      -- Cavalo Simples + Semirreboque 3 eixos
    'CARRETA_LS',           -- Cavalo Trucado (6x2) + Semirreboque 3 eixos

    -- COMBINAÇÕES ARTICULADAS DE MAIOR PORTE
    'BITREM_7_EIXOS',       -- Combinação de 7 eixos (Ex: Romeu e Julieta)
    'RODOTREM_9_EIXOS',     -- Combinação de 9 eixos (Máxima capacidade legal)
    'VANDERLEIA_3_EIXOS',   -- Semirreboque especial de 3 eixos distantes

    -- VEÍCULOS POR CARROCERIA (Especializados, se necessário para cálculo de frete)
    'BAU_SECO',             -- Baú Fechado para carga seca
    'BAU_FRIGORIFICO',      -- Baú com refrigeração
    'CACAMBA_BASICA',       -- Caçamba (para grãos, areia, etc.)
    'TANQUE',               -- Para transporte de líquidos/gases
    'CEGONHA',              -- Para transporte de veículos
    'PORTA_CONTAINER',      -- Chassi para transporte de contentores marítimos
    'GRADE_BAIXA',          -- Caminhão plataforma/carga geral aberta
    'GRANELEIRO'            -- Para grãos e produtos a granel
);

-- ======================================================================
-- 3. SCHEMA CORE: Identidades e Perfis M:M (Tabelas MESTRAS)
-- ======================================================================

CREATE TABLE core.pessoas (
	pessoa_id bigserial NOT NULL,
	nome varchar(255) NOT NULL,
	documento varchar(18) NULL,
    social_id VARCHAR(255) UNIQUE,
	email varchar(100) NOT NULL,
	telefone varchar(20) NULL,
	senha varchar(255) NOT NULL,
    data_nascimento DATE NULL,
	data_cadastro timestamp DEFAULT now() NULL,
	ativo bool DEFAULT false NOT NULL,
	is_colaborador bool DEFAULT false NOT NULL,
	is_cliente bool DEFAULT false NOT NULL,
	CONSTRAINT pessoas_documento_key UNIQUE (documento),
    CONSTRAINT pessoas_social_id_key UNIQUE (social_id), -- GARANTIR UNICIDADE NO SOCIAL ID
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
    -- COLUNA ATUALIZADA: Usando o novo tipo ENUM com o esquema qualificado
    status_kyc core.status_kyc_enum NOT NULL DEFAULT 'PENDENTE'::core.status_kyc_enum,
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
    -- COLUNA ATUALIZADA: Usando o novo tipo ENUM com o esquema qualificado
    tipo_conversa core.tipo_conversa_enum NOT NULL DEFAULT 'PRIVADA'::core.tipo_conversa_enum,
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
    versao INTEGER DEFAULT 0,
    FOREIGN KEY (pessoa_id) REFERENCES core.pessoas(pessoa_id) ON DELETE CASCADE
);

CREATE TABLE colaboradores.horarios_operacao (
    horario_id BIGSERIAL PRIMARY KEY,
    
    -- Chave estrangeira que aponta para o colaborador (Lojista ou Sucateiro)
    pessoa_id BIGINT NOT NULL,  
    
    dia_semana SMALLINT NOT NULL, -- 1=Domingo, 2=Segunda, ..., 7=Sábado
    
    hora_abertura TIME WITHOUT TIME ZONE NOT NULL,
    hora_fechamento TIME WITHOUT TIME ZONE NOT NULL,
    
    -- Restrições
    CONSTRAINT fk_colaborador_horario
        FOREIGN KEY (pessoa_id) 
        REFERENCES core.pessoas(pessoa_id) 
        ON DELETE CASCADE,
        
    -- Garante que não haja horários duplicados (abertura/fechamento) para o mesmo dia e colaborador
    UNIQUE (pessoa_id, dia_semana, hora_abertura) 
);

CREATE TABLE colaboradores.catadores (
    pessoa_id BIGINT PRIMARY KEY,
    associacao_id INTEGER,
    area_atuacao_geografica TEXT,
    FOREIGN KEY (pessoa_id) REFERENCES core.pessoas(pessoa_id) ON DELETE CASCADE
);

-- 5. Tabela de Veículos
CREATE TABLE colaboradores.veiculos (
    veiculo_id BIGSERIAL PRIMARY KEY,
    transportador_id BIGINT NOT NULL,
    placa VARCHAR(10) NOT NULL UNIQUE,
    renavam VARCHAR(11) NOT NULL UNIQUE,
    tipo_veiculo colaboradores.tipo_veiculo_enum NOT NULL, -- Usa o ENUM expandido
    status_veiculo colaboradores.status_veiculo_enum NOT NULL DEFAULT 'DISPONIVEL'::colaboradores.status_veiculo_enum,
    ano_fabricacao INTEGER,
    capacidade_m3 NUMERIC(10, 2), -- Metros Cúbicos
    capacidade_kg NUMERIC(10, 2), -- Quilogramas
    possui_rastreador BOOLEAN DEFAULT FALSE,
    
    -- CORREÇÃO DA CHAVE ESTRANGEIRA (FK): 
    -- Agora referencia colaboradores.transportadores(pessoa_id)
    FOREIGN KEY (transportador_id) REFERENCES colaboradores.transportadores(pessoa_id) ON DELETE CASCADE 
    
    -- NOTA: A FK para 'tipos_veiculos' foi removida, pois a coluna 'tipo_veiculo'
    -- agora usa o ENUM nativo do PostgreSQL.
);

-- ======================================================================
-- Módulo: COLABORADORES
-- 1. CRIAÇÃO DA TABELA: metricas_transportador
-- Catálogo de Métricas de Preços para os veículos de um Transportador.
-- ======================================================================

CREATE TABLE colaboradores.metricas_transportador (
    -- Chave Primária para a Métrica
    metrica_id BIGSERIAL PRIMARY KEY,

    -- FK para o Transportador: Um Transportador é dono de um conjunto de Métricas
    transportador_pessoa_id BIGINT NOT NULL,

    -- Nome Descritivo da Métrica (Ex: "Caminhão Toco - Custo Padrão Sucata Leve")
    nome_metrica VARCHAR(100) NOT NULL,

    -- CAMPOS DE FILTRO PARA PRECIFICAÇÃO DINÂMICA
    tipo_carga_material VARCHAR(100),
    tipo_veiculo colaboradores.tipo_veiculo_enum,
    modalidade_frete_id INTEGER,

    -- Parâmetros de Custo Personalizados:
    custo_fixo_viagem NUMERIC(10, 2) NOT NULL DEFAULT 0.00,
    custo_por_km NUMERIC(10, 4) NOT NULL DEFAULT 0.0000,
    margem_lucro NUMERIC(5, 4) DEFAULT 0.1000,
    custo_hora_espera NUMERIC(10, 2) DEFAULT 0.00,

    -- Bloqueio Otimista
    versao INTEGER NOT NULL DEFAULT 0,

    -- Restrições de Chaves (A FK da Modalidade será adicionada no bloco ALTER TABLE)
    FOREIGN KEY (transportador_pessoa_id)
        REFERENCES colaboradores.transportadores(pessoa_id)
        ON DELETE CASCADE,

    -- Um transportador não pode ter duas métricas com o mesmo nome
    UNIQUE (transportador_pessoa_id, nome_metrica)
);

-- Adiciona um comentário para documentação
COMMENT ON TABLE colaboradores.metricas_transportador IS 
    'Catálogo de conjuntos de parâmetros de precificação customizados, definidos por transportadores e associados a tipos de veículo/carga.';

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
-- PARTE 1: CRIAÇÃO DE TODAS AS TABELAS (SEM FKs para dependências circulares/posteriores)
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

-- Tabela de Ordens de Serviço (FKs movidas para ALTER TABLE)
CREATE TABLE logistica.ordens_servico (
    ordem_id BIGSERIAL PRIMARY KEY,
    cliente_solicitante_id BIGINT NOT NULL,
    transportador_designado_id BIGINT,
    data_solicitacao TIMESTAMP WITHOUT TIME ZONE DEFAULT now(),
    data_prevista_coleta DATE,
    endereco_coleta TEXT NOT NULL,
    cep_coleta VARCHAR(8) NOT NULL,
    cep_destino VARCHAR(8) NOT NULL,
    status logistica.status_servico NOT NULL DEFAULT 'PENDENTE'::logistica.status_servico
    -- FKs para clientes.detalhes e colaboradores.transportadores removidas
);

-- Tabela de Fretes (FKs movidas para ALTER TABLE)
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
    transportador_selecionado_id BIGINT
    -- Todas as FKs removidas
);

-- Tabela de Itens de Frete (FKs movidas para ALTER TABLE)
CREATE TABLE logistica.itens_frete (
    item_frete_id SERIAL PRIMARY KEY,
    frete_id INTEGER NOT NULL,
    descricao TEXT NOT NULL,
    tipo_material VARCHAR(100),
    peso_estimado_kg NUMERIC(10, 2) NOT NULL,
    volume_estimado_m3 NUMERIC(10, 2)
    -- FK para logistica.fretes removida
);

-- Tabela de Lances (FKs movidas para ALTER TABLE)
CREATE TABLE logistica.lances (
    lance_id SERIAL PRIMARY KEY,
    frete_id INTEGER NOT NULL,
    transportador_id BIGINT NOT NULL,
    valor_lance NUMERIC(10, 2) NOT NULL,
    data_lance TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    is_vencedor BOOLEAN DEFAULT false,
    motivo_cancelamento TEXT
    -- FKs para logistica.fretes e colaboradores.transportadores removidas
);

CREATE TABLE logistica.cotacoes_materiais (
    cotacao_id SERIAL PRIMARY KEY,
    material_nome VARCHAR(100) UNIQUE NOT NULL,
    preco_medio_kg NUMERIC(10, 2) NOT NULL,
    unidade_medida VARCHAR(10) NOT NULL DEFAULT 'KG',
    data_atualizacao TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);


-- ======================================================================
-- 6. SCHEMA LOGISTICA
-- PARTE 2: ADIÇÃO DAS FOREIGN KEYS (APÓS TODAS AS TABELAS EXISTIREM)
-- ======================================================================

-- Adiciona FKs em logistica.ordens_servico
ALTER TABLE logistica.ordens_servico
    ADD CONSTRAINT fk_os_cliente
        FOREIGN KEY (cliente_solicitante_id) REFERENCES clientes.detalhes(pessoa_id) ON DELETE RESTRICT,
    ADD CONSTRAINT fk_os_transportador_designado
        FOREIGN KEY (transportador_designado_id) REFERENCES colaboradores.transportadores(pessoa_id) ON DELETE SET NULL;

-- Adiciona FKs em logistica.fretes
ALTER TABLE logistica.fretes
    ADD CONSTRAINT fk_frete_ordem_servico
        FOREIGN KEY (ordem_servico_id) REFERENCES logistica.ordens_servico(ordem_id) ON DELETE CASCADE,
    ADD CONSTRAINT fk_frete_modalidade
        FOREIGN KEY (modalidade_id) REFERENCES logistica.modalidades_frete(modalidade_id) ON DELETE RESTRICT,
    ADD CONSTRAINT fk_frete_status_leilao
        FOREIGN KEY (status_leilao_id) REFERENCES logistica.status_leilao(status_id) ON DELETE RESTRICT,
    ADD CONSTRAINT fk_frete_transportador_selecionado
        FOREIGN KEY (transportador_selecionado_id) REFERENCES colaboradores.transportadores(pessoa_id) ON DELETE SET NULL;

-- Adiciona FKs em logistica.itens_frete
ALTER TABLE logistica.itens_frete
    ADD CONSTRAINT fk_item_frete
        FOREIGN KEY (frete_id) REFERENCES logistica.fretes(frete_id) ON DELETE CASCADE;

-- Adiciona FKs em logistica.lances
ALTER TABLE logistica.lances
    ADD CONSTRAINT fk_lance_frete
        FOREIGN KEY (frete_id) REFERENCES logistica.fretes(frete_id) ON DELETE CASCADE,
    ADD CONSTRAINT fk_lance_transportador
        FOREIGN KEY (transportador_id) REFERENCES colaboradores.transportadores(pessoa_id) ON DELETE RESTRICT;

-- Adiciona FKs no catálogo de métricas (colaboradores.metricas_transportador)
ALTER TABLE colaboradores.metricas_transportador
    ADD CONSTRAINT fk_metrica_modalidade
        FOREIGN KEY (modalidade_frete_id)
        REFERENCES logistica.modalidades_frete(modalidade_id)
        ON DELETE SET NULL;

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


-- Inserção dos Papéis de Permissão (ROLES) na tabela core.perfis
-- Estes são dados estáticos e essenciais para a segurança do sistema.
INSERT INTO core.perfis (nome_perfil, descricao) VALUES
('ADMIN', 'Administrador do Sistema, acesso total.'),
('CLIENTE', 'Pessoa que utiliza os serviços (solicita frete/compra no marketplace).'),
('TRANSPORTADOR', 'Colaborador que realiza lances e executa fretes.'),
('LOJISTA', 'Colaborador que vende produtos no marketplace.'),
('SUCATEIRO', 'Colaborador com estoque de sucata para negociação.'),
('CATADOR', 'Colaborador que coleta material.'),
('LEAD', 'Utilizador interessado que está em fase de qualificação ou engajamento.')
ON CONFLICT (nome_perfil) DO NOTHING;

-- Nota: ON CONFLICT DO NOTHING evita problemas se o script for executado mais de uma vez 
-- em ambientes onde o Flyway não esteja a ser usado (ex: teste local).
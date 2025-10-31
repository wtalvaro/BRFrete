--
-- PostgreSQL database dump
--

\restrict JO5fR7NJuSIhfduklclb0zD5XPiUbdrTUmIP5T3QMFioqUYzXf6qGqBRlQbQ7vD

-- Dumped from database version 17.6
-- Dumped by pg_dump version 17.6

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: clientes; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA clientes;


ALTER SCHEMA clientes OWNER TO postgres;

--
-- Name: colaboradores; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA colaboradores;


ALTER SCHEMA colaboradores OWNER TO postgres;

--
-- Name: core; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA core;


ALTER SCHEMA core OWNER TO postgres;

--
-- Name: inventario; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA inventario;


ALTER SCHEMA inventario OWNER TO postgres;

--
-- Name: logistica; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA logistica;


ALTER SCHEMA logistica OWNER TO postgres;

--
-- Name: marketplace; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA marketplace;


ALTER SCHEMA marketplace OWNER TO postgres;

--
-- Name: social; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA social;


ALTER SCHEMA social OWNER TO postgres;

--
-- Name: status_servico; Type: TYPE; Schema: logistica; Owner: postgres
--

CREATE TYPE logistica.status_servico AS ENUM (
    'PENDENTE',
    'CONFIRMADO',
    'COLETADO',
    'EM_TRANSPORTE',
    'ENTREGUE',
    'CANCELADO'
);


ALTER TYPE logistica.status_servico OWNER TO postgres;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: detalhes; Type: TABLE; Schema: clientes; Owner: postgres
--

CREATE TABLE clientes.detalhes (
    pessoa_id bigint NOT NULL,
    tipo_cliente character varying(20) NOT NULL,
    preferencias_coleta text
);


ALTER TABLE clientes.detalhes OWNER TO postgres;

--
-- Name: pedidos_coleta; Type: TABLE; Schema: clientes; Owner: postgres
--

CREATE TABLE clientes.pedidos_coleta (
    pedido_id integer NOT NULL,
    cliente_id bigint NOT NULL,
    descricao_pedido text NOT NULL,
    data_solicitacao timestamp with time zone DEFAULT CURRENT_TIMESTAMP
);


ALTER TABLE clientes.pedidos_coleta OWNER TO postgres;

--
-- Name: pedidos_coleta_pedido_id_seq; Type: SEQUENCE; Schema: clientes; Owner: postgres
--

CREATE SEQUENCE clientes.pedidos_coleta_pedido_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE clientes.pedidos_coleta_pedido_id_seq OWNER TO postgres;

--
-- Name: pedidos_coleta_pedido_id_seq; Type: SEQUENCE OWNED BY; Schema: clientes; Owner: postgres
--

ALTER SEQUENCE clientes.pedidos_coleta_pedido_id_seq OWNED BY clientes.pedidos_coleta.pedido_id;


--
-- Name: catadores; Type: TABLE; Schema: colaboradores; Owner: postgres
--

CREATE TABLE colaboradores.catadores (
    pessoa_id bigint NOT NULL,
    data_nascimento date,
    associacao_id integer,
    area_atuacao_geografica text
);


ALTER TABLE colaboradores.catadores OWNER TO postgres;

--
-- Name: lojistas; Type: TABLE; Schema: colaboradores; Owner: postgres
--

CREATE TABLE colaboradores.lojistas (
    pessoa_id bigint NOT NULL,
    nome_loja character varying(255) NOT NULL,
    endereco_coleta text NOT NULL,
    horario_atendimento character varying(100)
);


ALTER TABLE colaboradores.lojistas OWNER TO postgres;

--
-- Name: sucateiros; Type: TABLE; Schema: colaboradores; Owner: postgres
--

CREATE TABLE colaboradores.sucateiros (
    pessoa_id bigint NOT NULL,
    razao_social character varying(255) NOT NULL,
    cnpj_secundario character varying(18),
    licenca_ambiental character varying(100),
    endereco_patio text NOT NULL
);


ALTER TABLE colaboradores.sucateiros OWNER TO postgres;

--
-- Name: transportadores; Type: TABLE; Schema: colaboradores; Owner: postgres
--

CREATE TABLE colaboradores.transportadores (
    pessoa_id bigint NOT NULL,
    licenca_transporte character varying(100)
);


ALTER TABLE colaboradores.transportadores OWNER TO postgres;

--
-- Name: veiculos; Type: TABLE; Schema: colaboradores; Owner: postgres
--

CREATE TABLE colaboradores.veiculos (
    veiculo_id integer NOT NULL,
    transportador_pessoa_id bigint NOT NULL,
    matricula character varying(20) NOT NULL,
    tipo_veiculo character varying(50) NOT NULL,
    capacidade_peso_kg numeric(10,2) NOT NULL,
    capacidade_volume_m3 numeric(10,2),
    status_veiculo character varying(20) DEFAULT 'ATIVO'::character varying NOT NULL
);


ALTER TABLE colaboradores.veiculos OWNER TO postgres;

--
-- Name: veiculos_veiculo_id_seq; Type: SEQUENCE; Schema: colaboradores; Owner: postgres
--

CREATE SEQUENCE colaboradores.veiculos_veiculo_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE colaboradores.veiculos_veiculo_id_seq OWNER TO postgres;

--
-- Name: veiculos_veiculo_id_seq; Type: SEQUENCE OWNED BY; Schema: colaboradores; Owner: postgres
--

ALTER SEQUENCE colaboradores.veiculos_veiculo_id_seq OWNED BY colaboradores.veiculos.veiculo_id;


--
-- Name: contas_digitais; Type: TABLE; Schema: core; Owner: postgres
--

CREATE TABLE core.contas_digitais (
    pessoa_id bigint NOT NULL,
    conta_uuid character varying(64) NOT NULL,
    status_kyc character varying(20) DEFAULT 'PENDENTE'::character varying NOT NULL,
    data_abertura timestamp without time zone DEFAULT now()
);


ALTER TABLE core.contas_digitais OWNER TO postgres;

--
-- Name: conversas; Type: TABLE; Schema: core; Owner: postgres
--

CREATE TABLE core.conversas (
    conversa_id bigint NOT NULL,
    tipo_conversa character varying(20) DEFAULT 'PRIVADA'::character varying NOT NULL,
    data_criacao timestamp without time zone DEFAULT now(),
    ultima_mensagem_em timestamp without time zone DEFAULT now()
);


ALTER TABLE core.conversas OWNER TO postgres;

--
-- Name: conversas_conversa_id_seq; Type: SEQUENCE; Schema: core; Owner: postgres
--

CREATE SEQUENCE core.conversas_conversa_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE core.conversas_conversa_id_seq OWNER TO postgres;

--
-- Name: conversas_conversa_id_seq; Type: SEQUENCE OWNED BY; Schema: core; Owner: postgres
--

ALTER SEQUENCE core.conversas_conversa_id_seq OWNED BY core.conversas.conversa_id;


--
-- Name: mensagens; Type: TABLE; Schema: core; Owner: postgres
--

CREATE TABLE core.mensagens (
    mensagem_id bigint NOT NULL,
    conversa_id bigint NOT NULL,
    autor_id bigint NOT NULL,
    conteudo text NOT NULL,
    data_envio timestamp without time zone DEFAULT now(),
    is_lida boolean DEFAULT false NOT NULL
);


ALTER TABLE core.mensagens OWNER TO postgres;

--
-- Name: mensagens_mensagem_id_seq; Type: SEQUENCE; Schema: core; Owner: postgres
--

CREATE SEQUENCE core.mensagens_mensagem_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE core.mensagens_mensagem_id_seq OWNER TO postgres;

--
-- Name: mensagens_mensagem_id_seq; Type: SEQUENCE OWNED BY; Schema: core; Owner: postgres
--

ALTER SEQUENCE core.mensagens_mensagem_id_seq OWNED BY core.mensagens.mensagem_id;


--
-- Name: participantes_conversa; Type: TABLE; Schema: core; Owner: postgres
--

CREATE TABLE core.participantes_conversa (
    conversa_id bigint NOT NULL,
    pessoa_id bigint NOT NULL,
    data_entrada timestamp without time zone DEFAULT now()
);


ALTER TABLE core.participantes_conversa OWNER TO postgres;

--
-- Name: perfis; Type: TABLE; Schema: core; Owner: postgres
--

CREATE TABLE core.perfis (
    perfil_id integer NOT NULL,
    nome_perfil character varying(50) NOT NULL,
    descricao text
);


ALTER TABLE core.perfis OWNER TO postgres;

--
-- Name: perfis_perfil_id_seq; Type: SEQUENCE; Schema: core; Owner: postgres
--

CREATE SEQUENCE core.perfis_perfil_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE core.perfis_perfil_id_seq OWNER TO postgres;

--
-- Name: perfis_perfil_id_seq; Type: SEQUENCE OWNED BY; Schema: core; Owner: postgres
--

ALTER SEQUENCE core.perfis_perfil_id_seq OWNED BY core.perfis.perfil_id;


--
-- Name: pessoa_perfil; Type: TABLE; Schema: core; Owner: postgres
--

CREATE TABLE core.pessoa_perfil (
    pessoa_id bigint NOT NULL,
    perfil_id integer NOT NULL
);


ALTER TABLE core.pessoa_perfil OWNER TO postgres;

--
-- Name: pessoas; Type: TABLE; Schema: core; Owner: postgres
--

CREATE TABLE core.pessoas (
    pessoa_id bigint NOT NULL,
    nome character varying(255) NOT NULL,
    documento character varying(18),
    email character varying(100) NOT NULL,
    telefone character varying(20),
    senha character varying(255) NOT NULL,
    data_cadastro timestamp without time zone DEFAULT now(),
    ativo boolean DEFAULT false NOT NULL,
    is_colaborador boolean DEFAULT false NOT NULL,
    is_cliente boolean DEFAULT false NOT NULL,
    social_id character varying(255),
    CONSTRAINT chk_email_valido CHECK (((email)::text ~* '^[A-Za-z0-9._-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,4}$'::text))
);


ALTER TABLE core.pessoas OWNER TO postgres;

--
-- Name: pessoas_pessoa_id_seq; Type: SEQUENCE; Schema: core; Owner: postgres
--

CREATE SEQUENCE core.pessoas_pessoa_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE core.pessoas_pessoa_id_seq OWNER TO postgres;

--
-- Name: pessoas_pessoa_id_seq; Type: SEQUENCE OWNED BY; Schema: core; Owner: postgres
--

ALTER SEQUENCE core.pessoas_pessoa_id_seq OWNED BY core.pessoas.pessoa_id;


--
-- Name: estoque; Type: TABLE; Schema: inventario; Owner: postgres
--

CREATE TABLE inventario.estoque (
    estoque_id bigint NOT NULL,
    gestor_id bigint NOT NULL,
    tipo_material character varying(50) NOT NULL,
    quantidade_kg numeric(10,2) NOT NULL,
    data_entrada timestamp without time zone DEFAULT now(),
    localizacao text,
    data_atualizacao timestamp with time zone,
    status_qualidade character varying(50)
);


ALTER TABLE inventario.estoque OWNER TO postgres;

--
-- Name: estoque_estoque_id_seq; Type: SEQUENCE; Schema: inventario; Owner: postgres
--

CREATE SEQUENCE inventario.estoque_estoque_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE inventario.estoque_estoque_id_seq OWNER TO postgres;

--
-- Name: estoque_estoque_id_seq; Type: SEQUENCE OWNED BY; Schema: inventario; Owner: postgres
--

ALTER SEQUENCE inventario.estoque_estoque_id_seq OWNED BY inventario.estoque.estoque_id;


--
-- Name: estoque_produto; Type: TABLE; Schema: inventario; Owner: postgres
--

CREATE TABLE inventario.estoque_produto (
    estoque_produto_id bigint NOT NULL,
    produto_id integer NOT NULL,
    quantidade_disponivel integer NOT NULL,
    data_ultima_entrada timestamp without time zone DEFAULT now(),
    localizacao character varying(100),
    ponto_reposicao integer,
    ultima_atualizacao timestamp with time zone,
    CONSTRAINT check_quantidade_positiva CHECK ((quantidade_disponivel >= 0))
);


ALTER TABLE inventario.estoque_produto OWNER TO postgres;

--
-- Name: estoque_produto_estoque_produto_id_seq; Type: SEQUENCE; Schema: inventario; Owner: postgres
--

CREATE SEQUENCE inventario.estoque_produto_estoque_produto_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE inventario.estoque_produto_estoque_produto_id_seq OWNER TO postgres;

--
-- Name: estoque_produto_estoque_produto_id_seq; Type: SEQUENCE OWNED BY; Schema: inventario; Owner: postgres
--

ALTER SEQUENCE inventario.estoque_produto_estoque_produto_id_seq OWNED BY inventario.estoque_produto.estoque_produto_id;


--
-- Name: antt_parametros; Type: TABLE; Schema: logistica; Owner: postgres
--

CREATE TABLE logistica.antt_parametros (
    parametro_id integer NOT NULL,
    chave character varying(100) NOT NULL,
    valor numeric(10,4) NOT NULL,
    descricao text,
    data_vigencia date DEFAULT now()
);


ALTER TABLE logistica.antt_parametros OWNER TO postgres;

--
-- Name: antt_parametros_parametro_id_seq; Type: SEQUENCE; Schema: logistica; Owner: postgres
--

CREATE SEQUENCE logistica.antt_parametros_parametro_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE logistica.antt_parametros_parametro_id_seq OWNER TO postgres;

--
-- Name: antt_parametros_parametro_id_seq; Type: SEQUENCE OWNED BY; Schema: logistica; Owner: postgres
--

ALTER SEQUENCE logistica.antt_parametros_parametro_id_seq OWNED BY logistica.antt_parametros.parametro_id;


--
-- Name: cotacoes_materiais; Type: TABLE; Schema: logistica; Owner: postgres
--

CREATE TABLE logistica.cotacoes_materiais (
    cotacao_id integer NOT NULL,
    material_nome character varying(100) NOT NULL,
    preco_medio_kg numeric(10,2) NOT NULL,
    unidade_medida character varying(10) DEFAULT 'KG'::character varying NOT NULL,
    data_atualizacao timestamp with time zone DEFAULT CURRENT_TIMESTAMP
);


ALTER TABLE logistica.cotacoes_materiais OWNER TO postgres;

--
-- Name: cotacoes_materiais_cotacao_id_seq; Type: SEQUENCE; Schema: logistica; Owner: postgres
--

CREATE SEQUENCE logistica.cotacoes_materiais_cotacao_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE logistica.cotacoes_materiais_cotacao_id_seq OWNER TO postgres;

--
-- Name: cotacoes_materiais_cotacao_id_seq; Type: SEQUENCE OWNED BY; Schema: logistica; Owner: postgres
--

ALTER SEQUENCE logistica.cotacoes_materiais_cotacao_id_seq OWNED BY logistica.cotacoes_materiais.cotacao_id;


--
-- Name: fretes; Type: TABLE; Schema: logistica; Owner: postgres
--

CREATE TABLE logistica.fretes (
    frete_id integer NOT NULL,
    ordem_servico_id bigint NOT NULL,
    modalidade_id integer NOT NULL,
    status_leilao_id integer NOT NULL,
    data_expiracao_negociacao timestamp with time zone,
    preco_sugerido numeric(10,2),
    antt_piso_minimo numeric(10,2),
    custo_base_mercado numeric(10,2),
    distancia_km numeric(10,2),
    transportador_selecionado_id bigint,
    tipo_embalagem character varying(50),
    valor_final_aceito numeric(10,2),
    valor_inicial_proposto numeric(10,2)
);


ALTER TABLE logistica.fretes OWNER TO postgres;

--
-- Name: fretes_frete_id_seq; Type: SEQUENCE; Schema: logistica; Owner: postgres
--

CREATE SEQUENCE logistica.fretes_frete_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE logistica.fretes_frete_id_seq OWNER TO postgres;

--
-- Name: fretes_frete_id_seq; Type: SEQUENCE OWNED BY; Schema: logistica; Owner: postgres
--

ALTER SEQUENCE logistica.fretes_frete_id_seq OWNED BY logistica.fretes.frete_id;


--
-- Name: itens_frete; Type: TABLE; Schema: logistica; Owner: postgres
--

CREATE TABLE logistica.itens_frete (
    item_frete_id integer NOT NULL,
    frete_id integer NOT NULL,
    descricao text NOT NULL,
    tipo_material character varying(100),
    peso_estimado_kg numeric(10,2) NOT NULL,
    volume_estimado_m3 numeric(10,2),
    item_id bigint NOT NULL,
    valor_estimado_unitario numeric(10,2),
    volume_m3 numeric(10,2)
);


ALTER TABLE logistica.itens_frete OWNER TO postgres;

--
-- Name: itens_frete_item_frete_id_seq; Type: SEQUENCE; Schema: logistica; Owner: postgres
--

CREATE SEQUENCE logistica.itens_frete_item_frete_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE logistica.itens_frete_item_frete_id_seq OWNER TO postgres;

--
-- Name: itens_frete_item_frete_id_seq; Type: SEQUENCE OWNED BY; Schema: logistica; Owner: postgres
--

ALTER SEQUENCE logistica.itens_frete_item_frete_id_seq OWNED BY logistica.itens_frete.item_frete_id;


--
-- Name: itens_frete_item_id_seq; Type: SEQUENCE; Schema: logistica; Owner: postgres
--

ALTER TABLE logistica.itens_frete ALTER COLUMN item_id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME logistica.itens_frete_item_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: lances; Type: TABLE; Schema: logistica; Owner: postgres
--

CREATE TABLE logistica.lances (
    lance_id bigint NOT NULL,
    frete_id integer NOT NULL,
    transportador_id bigint NOT NULL,
    valor_lance numeric(10,2) NOT NULL,
    data_lance timestamp with time zone DEFAULT CURRENT_TIMESTAMP,
    is_vencedor boolean DEFAULT false,
    motivo_cancelamento text
);


ALTER TABLE logistica.lances OWNER TO postgres;

--
-- Name: lances_lance_id_seq; Type: SEQUENCE; Schema: logistica; Owner: postgres
--

CREATE SEQUENCE logistica.lances_lance_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE logistica.lances_lance_id_seq OWNER TO postgres;

--
-- Name: lances_lance_id_seq; Type: SEQUENCE OWNED BY; Schema: logistica; Owner: postgres
--

ALTER SEQUENCE logistica.lances_lance_id_seq OWNED BY logistica.lances.lance_id;


--
-- Name: modalidades_frete; Type: TABLE; Schema: logistica; Owner: postgres
--

CREATE TABLE logistica.modalidades_frete (
    modalidade_id integer NOT NULL,
    nome_modalidade character varying(50) NOT NULL
);


ALTER TABLE logistica.modalidades_frete OWNER TO postgres;

--
-- Name: modalidades_frete_modalidade_id_seq; Type: SEQUENCE; Schema: logistica; Owner: postgres
--

CREATE SEQUENCE logistica.modalidades_frete_modalidade_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE logistica.modalidades_frete_modalidade_id_seq OWNER TO postgres;

--
-- Name: modalidades_frete_modalidade_id_seq; Type: SEQUENCE OWNED BY; Schema: logistica; Owner: postgres
--

ALTER SEQUENCE logistica.modalidades_frete_modalidade_id_seq OWNED BY logistica.modalidades_frete.modalidade_id;


--
-- Name: ordens_servico; Type: TABLE; Schema: logistica; Owner: postgres
--

CREATE TABLE logistica.ordens_servico (
    ordem_id bigint NOT NULL,
    cliente_solicitante_id bigint NOT NULL,
    transportador_designado_id bigint,
    data_solicitacao timestamp without time zone DEFAULT now(),
    data_prevista_coleta date,
    endereco_coleta text NOT NULL,
    cep_coleta character varying(8) NOT NULL,
    cep_destino character varying(8) NOT NULL,
    status character varying(20) DEFAULT 'PENDENTE'::logistica.status_servico NOT NULL,
    data_criacao timestamp with time zone,
    distancia_km numeric(10,2),
    prazo_coleta timestamp with time zone,
    transportador_pessoa_id bigint
);


ALTER TABLE logistica.ordens_servico OWNER TO postgres;

--
-- Name: ordens_servico_ordem_id_seq; Type: SEQUENCE; Schema: logistica; Owner: postgres
--

CREATE SEQUENCE logistica.ordens_servico_ordem_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE logistica.ordens_servico_ordem_id_seq OWNER TO postgres;

--
-- Name: ordens_servico_ordem_id_seq; Type: SEQUENCE OWNED BY; Schema: logistica; Owner: postgres
--

ALTER SEQUENCE logistica.ordens_servico_ordem_id_seq OWNED BY logistica.ordens_servico.ordem_id;


--
-- Name: status_leilao; Type: TABLE; Schema: logistica; Owner: postgres
--

CREATE TABLE logistica.status_leilao (
    status_id integer NOT NULL,
    nome_status character varying(50) NOT NULL
);


ALTER TABLE logistica.status_leilao OWNER TO postgres;

--
-- Name: status_leilao_status_id_seq; Type: SEQUENCE; Schema: logistica; Owner: postgres
--

CREATE SEQUENCE logistica.status_leilao_status_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE logistica.status_leilao_status_id_seq OWNER TO postgres;

--
-- Name: status_leilao_status_id_seq; Type: SEQUENCE OWNED BY; Schema: logistica; Owner: postgres
--

ALTER SEQUENCE logistica.status_leilao_status_id_seq OWNED BY logistica.status_leilao.status_id;


--
-- Name: categorias; Type: TABLE; Schema: marketplace; Owner: postgres
--

CREATE TABLE marketplace.categorias (
    categoria_id integer NOT NULL,
    nome_categoria character varying(100) NOT NULL,
    tipo_geral character varying(20) NOT NULL,
    descricao text
);


ALTER TABLE marketplace.categorias OWNER TO postgres;

--
-- Name: categorias_categoria_id_seq; Type: SEQUENCE; Schema: marketplace; Owner: postgres
--

CREATE SEQUENCE marketplace.categorias_categoria_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE marketplace.categorias_categoria_id_seq OWNER TO postgres;

--
-- Name: categorias_categoria_id_seq; Type: SEQUENCE OWNED BY; Schema: marketplace; Owner: postgres
--

ALTER SEQUENCE marketplace.categorias_categoria_id_seq OWNED BY marketplace.categorias.categoria_id;


--
-- Name: perguntas_produto; Type: TABLE; Schema: marketplace; Owner: postgres
--

CREATE TABLE marketplace.perguntas_produto (
    pergunta_id bigint NOT NULL,
    produto_id integer NOT NULL,
    autor_id bigint NOT NULL,
    texto_conteudo text NOT NULL,
    data_publicacao timestamp with time zone DEFAULT CURRENT_TIMESTAMP,
    pergunta_pai_id bigint,
    is_publica boolean DEFAULT true NOT NULL,
    data_resposta timestamp with time zone,
    resposta text
);


ALTER TABLE marketplace.perguntas_produto OWNER TO postgres;

--
-- Name: perguntas_produto_pergunta_id_seq; Type: SEQUENCE; Schema: marketplace; Owner: postgres
--

CREATE SEQUENCE marketplace.perguntas_produto_pergunta_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE marketplace.perguntas_produto_pergunta_id_seq OWNER TO postgres;

--
-- Name: perguntas_produto_pergunta_id_seq; Type: SEQUENCE OWNED BY; Schema: marketplace; Owner: postgres
--

ALTER SEQUENCE marketplace.perguntas_produto_pergunta_id_seq OWNED BY marketplace.perguntas_produto.pergunta_id;


--
-- Name: produtos; Type: TABLE; Schema: marketplace; Owner: postgres
--

CREATE TABLE marketplace.produtos (
    produto_id integer NOT NULL,
    vendedor_id bigint NOT NULL,
    categoria_id integer NOT NULL,
    titulo character varying(255) NOT NULL,
    descricao text,
    preco numeric(10,2) NOT NULL,
    quantidade integer DEFAULT 1 NOT NULL,
    unidade_medida character varying(10) DEFAULT 'UN'::character varying,
    data_publicacao timestamp with time zone DEFAULT CURRENT_TIMESTAMP,
    is_disponivel boolean DEFAULT true NOT NULL,
    is_doacao boolean DEFAULT false NOT NULL,
    data_listagem timestamp with time zone,
    peso_kg numeric(10,2),
    CONSTRAINT check_preco_doacao CHECK ((((is_doacao = true) AND (preco = 0.00)) OR ((is_doacao = false) AND (preco > 0.00))))
);


ALTER TABLE marketplace.produtos OWNER TO postgres;

--
-- Name: produtos_produto_id_seq; Type: SEQUENCE; Schema: marketplace; Owner: postgres
--

CREATE SEQUENCE marketplace.produtos_produto_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE marketplace.produtos_produto_id_seq OWNER TO postgres;

--
-- Name: produtos_produto_id_seq; Type: SEQUENCE OWNED BY; Schema: marketplace; Owner: postgres
--

ALTER SEQUENCE marketplace.produtos_produto_id_seq OWNED BY marketplace.produtos.produto_id;


--
-- Name: avaliacoes; Type: TABLE; Schema: social; Owner: postgres
--

CREATE TABLE social.avaliacoes (
    avaliacao_id bigint NOT NULL,
    avaliador_id bigint NOT NULL,
    avaliado_id bigint NOT NULL,
    ordem_servico_id bigint,
    produto_id integer,
    pontuacao smallint NOT NULL,
    comentario text,
    data_avaliacao timestamp with time zone DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT check_related_entity CHECK ((((ordem_servico_id IS NOT NULL) AND (produto_id IS NULL)) OR ((ordem_servico_id IS NULL) AND (produto_id IS NOT NULL))))
);


ALTER TABLE social.avaliacoes OWNER TO postgres;

--
-- Name: avaliacoes_avaliacao_id_seq; Type: SEQUENCE; Schema: social; Owner: postgres
--

CREATE SEQUENCE social.avaliacoes_avaliacao_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE social.avaliacoes_avaliacao_id_seq OWNER TO postgres;

--
-- Name: avaliacoes_avaliacao_id_seq; Type: SEQUENCE OWNED BY; Schema: social; Owner: postgres
--

ALTER SEQUENCE social.avaliacoes_avaliacao_id_seq OWNED BY social.avaliacoes.avaliacao_id;


--
-- Name: comentarios; Type: TABLE; Schema: social; Owner: postgres
--

CREATE TABLE social.comentarios (
    comentario_id bigint NOT NULL,
    autor_id bigint NOT NULL,
    produto_id integer NOT NULL,
    texto_comentario text NOT NULL,
    data_comentario timestamp with time zone DEFAULT CURRENT_TIMESTAMP,
    comentario_pai_id bigint
);


ALTER TABLE social.comentarios OWNER TO postgres;

--
-- Name: comentarios_comentario_id_seq; Type: SEQUENCE; Schema: social; Owner: postgres
--

CREATE SEQUENCE social.comentarios_comentario_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE social.comentarios_comentario_id_seq OWNER TO postgres;

--
-- Name: comentarios_comentario_id_seq; Type: SEQUENCE OWNED BY; Schema: social; Owner: postgres
--

ALTER SEQUENCE social.comentarios_comentario_id_seq OWNED BY social.comentarios.comentario_id;


--
-- Name: seguidores; Type: TABLE; Schema: social; Owner: postgres
--

CREATE TABLE social.seguidores (
    seguidor_id bigint NOT NULL,
    seguido_id bigint NOT NULL,
    CONSTRAINT check_self_follow CHECK ((seguidor_id <> seguido_id))
);


ALTER TABLE social.seguidores OWNER TO postgres;

--
-- Name: pedidos_coleta pedido_id; Type: DEFAULT; Schema: clientes; Owner: postgres
--

ALTER TABLE ONLY clientes.pedidos_coleta ALTER COLUMN pedido_id SET DEFAULT nextval('clientes.pedidos_coleta_pedido_id_seq'::regclass);


--
-- Name: veiculos veiculo_id; Type: DEFAULT; Schema: colaboradores; Owner: postgres
--

ALTER TABLE ONLY colaboradores.veiculos ALTER COLUMN veiculo_id SET DEFAULT nextval('colaboradores.veiculos_veiculo_id_seq'::regclass);


--
-- Name: conversas conversa_id; Type: DEFAULT; Schema: core; Owner: postgres
--

ALTER TABLE ONLY core.conversas ALTER COLUMN conversa_id SET DEFAULT nextval('core.conversas_conversa_id_seq'::regclass);


--
-- Name: mensagens mensagem_id; Type: DEFAULT; Schema: core; Owner: postgres
--

ALTER TABLE ONLY core.mensagens ALTER COLUMN mensagem_id SET DEFAULT nextval('core.mensagens_mensagem_id_seq'::regclass);


--
-- Name: perfis perfil_id; Type: DEFAULT; Schema: core; Owner: postgres
--

ALTER TABLE ONLY core.perfis ALTER COLUMN perfil_id SET DEFAULT nextval('core.perfis_perfil_id_seq'::regclass);


--
-- Name: pessoas pessoa_id; Type: DEFAULT; Schema: core; Owner: postgres
--

ALTER TABLE ONLY core.pessoas ALTER COLUMN pessoa_id SET DEFAULT nextval('core.pessoas_pessoa_id_seq'::regclass);


--
-- Name: estoque estoque_id; Type: DEFAULT; Schema: inventario; Owner: postgres
--

ALTER TABLE ONLY inventario.estoque ALTER COLUMN estoque_id SET DEFAULT nextval('inventario.estoque_estoque_id_seq'::regclass);


--
-- Name: estoque_produto estoque_produto_id; Type: DEFAULT; Schema: inventario; Owner: postgres
--

ALTER TABLE ONLY inventario.estoque_produto ALTER COLUMN estoque_produto_id SET DEFAULT nextval('inventario.estoque_produto_estoque_produto_id_seq'::regclass);


--
-- Name: antt_parametros parametro_id; Type: DEFAULT; Schema: logistica; Owner: postgres
--

ALTER TABLE ONLY logistica.antt_parametros ALTER COLUMN parametro_id SET DEFAULT nextval('logistica.antt_parametros_parametro_id_seq'::regclass);


--
-- Name: cotacoes_materiais cotacao_id; Type: DEFAULT; Schema: logistica; Owner: postgres
--

ALTER TABLE ONLY logistica.cotacoes_materiais ALTER COLUMN cotacao_id SET DEFAULT nextval('logistica.cotacoes_materiais_cotacao_id_seq'::regclass);


--
-- Name: fretes frete_id; Type: DEFAULT; Schema: logistica; Owner: postgres
--

ALTER TABLE ONLY logistica.fretes ALTER COLUMN frete_id SET DEFAULT nextval('logistica.fretes_frete_id_seq'::regclass);


--
-- Name: itens_frete item_frete_id; Type: DEFAULT; Schema: logistica; Owner: postgres
--

ALTER TABLE ONLY logistica.itens_frete ALTER COLUMN item_frete_id SET DEFAULT nextval('logistica.itens_frete_item_frete_id_seq'::regclass);


--
-- Name: lances lance_id; Type: DEFAULT; Schema: logistica; Owner: postgres
--

ALTER TABLE ONLY logistica.lances ALTER COLUMN lance_id SET DEFAULT nextval('logistica.lances_lance_id_seq'::regclass);


--
-- Name: modalidades_frete modalidade_id; Type: DEFAULT; Schema: logistica; Owner: postgres
--

ALTER TABLE ONLY logistica.modalidades_frete ALTER COLUMN modalidade_id SET DEFAULT nextval('logistica.modalidades_frete_modalidade_id_seq'::regclass);


--
-- Name: ordens_servico ordem_id; Type: DEFAULT; Schema: logistica; Owner: postgres
--

ALTER TABLE ONLY logistica.ordens_servico ALTER COLUMN ordem_id SET DEFAULT nextval('logistica.ordens_servico_ordem_id_seq'::regclass);


--
-- Name: status_leilao status_id; Type: DEFAULT; Schema: logistica; Owner: postgres
--

ALTER TABLE ONLY logistica.status_leilao ALTER COLUMN status_id SET DEFAULT nextval('logistica.status_leilao_status_id_seq'::regclass);


--
-- Name: categorias categoria_id; Type: DEFAULT; Schema: marketplace; Owner: postgres
--

ALTER TABLE ONLY marketplace.categorias ALTER COLUMN categoria_id SET DEFAULT nextval('marketplace.categorias_categoria_id_seq'::regclass);


--
-- Name: perguntas_produto pergunta_id; Type: DEFAULT; Schema: marketplace; Owner: postgres
--

ALTER TABLE ONLY marketplace.perguntas_produto ALTER COLUMN pergunta_id SET DEFAULT nextval('marketplace.perguntas_produto_pergunta_id_seq'::regclass);


--
-- Name: produtos produto_id; Type: DEFAULT; Schema: marketplace; Owner: postgres
--

ALTER TABLE ONLY marketplace.produtos ALTER COLUMN produto_id SET DEFAULT nextval('marketplace.produtos_produto_id_seq'::regclass);


--
-- Name: avaliacoes avaliacao_id; Type: DEFAULT; Schema: social; Owner: postgres
--

ALTER TABLE ONLY social.avaliacoes ALTER COLUMN avaliacao_id SET DEFAULT nextval('social.avaliacoes_avaliacao_id_seq'::regclass);


--
-- Name: comentarios comentario_id; Type: DEFAULT; Schema: social; Owner: postgres
--

ALTER TABLE ONLY social.comentarios ALTER COLUMN comentario_id SET DEFAULT nextval('social.comentarios_comentario_id_seq'::regclass);


--
-- Name: detalhes detalhes_pkey; Type: CONSTRAINT; Schema: clientes; Owner: postgres
--

ALTER TABLE ONLY clientes.detalhes
    ADD CONSTRAINT detalhes_pkey PRIMARY KEY (pessoa_id);


--
-- Name: pedidos_coleta pedidos_coleta_pkey; Type: CONSTRAINT; Schema: clientes; Owner: postgres
--

ALTER TABLE ONLY clientes.pedidos_coleta
    ADD CONSTRAINT pedidos_coleta_pkey PRIMARY KEY (pedido_id);


--
-- Name: catadores catadores_pkey; Type: CONSTRAINT; Schema: colaboradores; Owner: postgres
--

ALTER TABLE ONLY colaboradores.catadores
    ADD CONSTRAINT catadores_pkey PRIMARY KEY (pessoa_id);


--
-- Name: lojistas lojistas_pkey; Type: CONSTRAINT; Schema: colaboradores; Owner: postgres
--

ALTER TABLE ONLY colaboradores.lojistas
    ADD CONSTRAINT lojistas_pkey PRIMARY KEY (pessoa_id);


--
-- Name: sucateiros sucateiros_pkey; Type: CONSTRAINT; Schema: colaboradores; Owner: postgres
--

ALTER TABLE ONLY colaboradores.sucateiros
    ADD CONSTRAINT sucateiros_pkey PRIMARY KEY (pessoa_id);


--
-- Name: transportadores transportadores_pkey; Type: CONSTRAINT; Schema: colaboradores; Owner: postgres
--

ALTER TABLE ONLY colaboradores.transportadores
    ADD CONSTRAINT transportadores_pkey PRIMARY KEY (pessoa_id);


--
-- Name: veiculos ukhbmulola9aked6nx00djbad1r; Type: CONSTRAINT; Schema: colaboradores; Owner: postgres
--

ALTER TABLE ONLY colaboradores.veiculos
    ADD CONSTRAINT ukhbmulola9aked6nx00djbad1r UNIQUE (matricula);


--
-- Name: veiculos veiculos_matricula_key; Type: CONSTRAINT; Schema: colaboradores; Owner: postgres
--

ALTER TABLE ONLY colaboradores.veiculos
    ADD CONSTRAINT veiculos_matricula_key UNIQUE (matricula);


--
-- Name: veiculos veiculos_pkey; Type: CONSTRAINT; Schema: colaboradores; Owner: postgres
--

ALTER TABLE ONLY colaboradores.veiculos
    ADD CONSTRAINT veiculos_pkey PRIMARY KEY (veiculo_id);


--
-- Name: contas_digitais contas_digitais_conta_uuid_key; Type: CONSTRAINT; Schema: core; Owner: postgres
--

ALTER TABLE ONLY core.contas_digitais
    ADD CONSTRAINT contas_digitais_conta_uuid_key UNIQUE (conta_uuid);


--
-- Name: contas_digitais contas_digitais_pkey; Type: CONSTRAINT; Schema: core; Owner: postgres
--

ALTER TABLE ONLY core.contas_digitais
    ADD CONSTRAINT contas_digitais_pkey PRIMARY KEY (pessoa_id);


--
-- Name: conversas conversas_pkey; Type: CONSTRAINT; Schema: core; Owner: postgres
--

ALTER TABLE ONLY core.conversas
    ADD CONSTRAINT conversas_pkey PRIMARY KEY (conversa_id);


--
-- Name: mensagens mensagens_pkey; Type: CONSTRAINT; Schema: core; Owner: postgres
--

ALTER TABLE ONLY core.mensagens
    ADD CONSTRAINT mensagens_pkey PRIMARY KEY (mensagem_id);


--
-- Name: participantes_conversa participantes_conversa_pkey; Type: CONSTRAINT; Schema: core; Owner: postgres
--

ALTER TABLE ONLY core.participantes_conversa
    ADD CONSTRAINT participantes_conversa_pkey PRIMARY KEY (conversa_id, pessoa_id);


--
-- Name: perfis perfis_nome_perfil_key; Type: CONSTRAINT; Schema: core; Owner: postgres
--

ALTER TABLE ONLY core.perfis
    ADD CONSTRAINT perfis_nome_perfil_key UNIQUE (nome_perfil);


--
-- Name: perfis perfis_pkey; Type: CONSTRAINT; Schema: core; Owner: postgres
--

ALTER TABLE ONLY core.perfis
    ADD CONSTRAINT perfis_pkey PRIMARY KEY (perfil_id);


--
-- Name: pessoa_perfil pessoa_perfil_pkey; Type: CONSTRAINT; Schema: core; Owner: postgres
--

ALTER TABLE ONLY core.pessoa_perfil
    ADD CONSTRAINT pessoa_perfil_pkey PRIMARY KEY (pessoa_id, perfil_id);


--
-- Name: pessoas pessoas_documento_key; Type: CONSTRAINT; Schema: core; Owner: postgres
--

ALTER TABLE ONLY core.pessoas
    ADD CONSTRAINT pessoas_documento_key UNIQUE (documento);


--
-- Name: pessoas pessoas_email_key; Type: CONSTRAINT; Schema: core; Owner: postgres
--

ALTER TABLE ONLY core.pessoas
    ADD CONSTRAINT pessoas_email_key UNIQUE (email);


--
-- Name: pessoas pessoas_pkey; Type: CONSTRAINT; Schema: core; Owner: postgres
--

ALTER TABLE ONLY core.pessoas
    ADD CONSTRAINT pessoas_pkey PRIMARY KEY (pessoa_id);


--
-- Name: pessoas pessoas_social_id_key; Type: CONSTRAINT; Schema: core; Owner: postgres
--

ALTER TABLE ONLY core.pessoas
    ADD CONSTRAINT pessoas_social_id_key UNIQUE (social_id);


--
-- Name: perfis ukitlvuaigjuko7u30xqojsd8e; Type: CONSTRAINT; Schema: core; Owner: postgres
--

ALTER TABLE ONLY core.perfis
    ADD CONSTRAINT ukitlvuaigjuko7u30xqojsd8e UNIQUE (nome_perfil);


--
-- Name: estoque estoque_pkey; Type: CONSTRAINT; Schema: inventario; Owner: postgres
--

ALTER TABLE ONLY inventario.estoque
    ADD CONSTRAINT estoque_pkey PRIMARY KEY (estoque_id);


--
-- Name: estoque_produto estoque_produto_pkey; Type: CONSTRAINT; Schema: inventario; Owner: postgres
--

ALTER TABLE ONLY inventario.estoque_produto
    ADD CONSTRAINT estoque_produto_pkey PRIMARY KEY (estoque_produto_id);


--
-- Name: estoque_produto estoque_produto_produto_id_key; Type: CONSTRAINT; Schema: inventario; Owner: postgres
--

ALTER TABLE ONLY inventario.estoque_produto
    ADD CONSTRAINT estoque_produto_produto_id_key UNIQUE (produto_id);


--
-- Name: antt_parametros antt_parametros_chave_key; Type: CONSTRAINT; Schema: logistica; Owner: postgres
--

ALTER TABLE ONLY logistica.antt_parametros
    ADD CONSTRAINT antt_parametros_chave_key UNIQUE (chave);


--
-- Name: antt_parametros antt_parametros_pkey; Type: CONSTRAINT; Schema: logistica; Owner: postgres
--

ALTER TABLE ONLY logistica.antt_parametros
    ADD CONSTRAINT antt_parametros_pkey PRIMARY KEY (parametro_id);


--
-- Name: cotacoes_materiais cotacoes_materiais_material_nome_key; Type: CONSTRAINT; Schema: logistica; Owner: postgres
--

ALTER TABLE ONLY logistica.cotacoes_materiais
    ADD CONSTRAINT cotacoes_materiais_material_nome_key UNIQUE (material_nome);


--
-- Name: cotacoes_materiais cotacoes_materiais_pkey; Type: CONSTRAINT; Schema: logistica; Owner: postgres
--

ALTER TABLE ONLY logistica.cotacoes_materiais
    ADD CONSTRAINT cotacoes_materiais_pkey PRIMARY KEY (cotacao_id);


--
-- Name: fretes fretes_pkey; Type: CONSTRAINT; Schema: logistica; Owner: postgres
--

ALTER TABLE ONLY logistica.fretes
    ADD CONSTRAINT fretes_pkey PRIMARY KEY (frete_id);


--
-- Name: itens_frete itens_frete_pkey; Type: CONSTRAINT; Schema: logistica; Owner: postgres
--

ALTER TABLE ONLY logistica.itens_frete
    ADD CONSTRAINT itens_frete_pkey PRIMARY KEY (item_frete_id);


--
-- Name: lances lances_pkey; Type: CONSTRAINT; Schema: logistica; Owner: postgres
--

ALTER TABLE ONLY logistica.lances
    ADD CONSTRAINT lances_pkey PRIMARY KEY (lance_id);


--
-- Name: modalidades_frete modalidades_frete_nome_modalidade_key; Type: CONSTRAINT; Schema: logistica; Owner: postgres
--

ALTER TABLE ONLY logistica.modalidades_frete
    ADD CONSTRAINT modalidades_frete_nome_modalidade_key UNIQUE (nome_modalidade);


--
-- Name: modalidades_frete modalidades_frete_pkey; Type: CONSTRAINT; Schema: logistica; Owner: postgres
--

ALTER TABLE ONLY logistica.modalidades_frete
    ADD CONSTRAINT modalidades_frete_pkey PRIMARY KEY (modalidade_id);


--
-- Name: ordens_servico ordens_servico_pkey; Type: CONSTRAINT; Schema: logistica; Owner: postgres
--

ALTER TABLE ONLY logistica.ordens_servico
    ADD CONSTRAINT ordens_servico_pkey PRIMARY KEY (ordem_id);


--
-- Name: status_leilao status_leilao_nome_status_key; Type: CONSTRAINT; Schema: logistica; Owner: postgres
--

ALTER TABLE ONLY logistica.status_leilao
    ADD CONSTRAINT status_leilao_nome_status_key UNIQUE (nome_status);


--
-- Name: status_leilao status_leilao_pkey; Type: CONSTRAINT; Schema: logistica; Owner: postgres
--

ALTER TABLE ONLY logistica.status_leilao
    ADD CONSTRAINT status_leilao_pkey PRIMARY KEY (status_id);


--
-- Name: modalidades_frete uk6gtpmo4agj58w4s4yg5ecki29; Type: CONSTRAINT; Schema: logistica; Owner: postgres
--

ALTER TABLE ONLY logistica.modalidades_frete
    ADD CONSTRAINT uk6gtpmo4agj58w4s4yg5ecki29 UNIQUE (nome_modalidade);


--
-- Name: antt_parametros uka8jpvk79l3ybrsxnyl2fkm3pq; Type: CONSTRAINT; Schema: logistica; Owner: postgres
--

ALTER TABLE ONLY logistica.antt_parametros
    ADD CONSTRAINT uka8jpvk79l3ybrsxnyl2fkm3pq UNIQUE (chave);


--
-- Name: cotacoes_materiais ukfuuqrjcguug48otlbpcim5rng; Type: CONSTRAINT; Schema: logistica; Owner: postgres
--

ALTER TABLE ONLY logistica.cotacoes_materiais
    ADD CONSTRAINT ukfuuqrjcguug48otlbpcim5rng UNIQUE (material_nome);


--
-- Name: status_leilao uknf5d5y5f23h0acxuemb5ia136; Type: CONSTRAINT; Schema: logistica; Owner: postgres
--

ALTER TABLE ONLY logistica.status_leilao
    ADD CONSTRAINT uknf5d5y5f23h0acxuemb5ia136 UNIQUE (nome_status);


--
-- Name: categorias categorias_nome_categoria_key; Type: CONSTRAINT; Schema: marketplace; Owner: postgres
--

ALTER TABLE ONLY marketplace.categorias
    ADD CONSTRAINT categorias_nome_categoria_key UNIQUE (nome_categoria);


--
-- Name: categorias categorias_pkey; Type: CONSTRAINT; Schema: marketplace; Owner: postgres
--

ALTER TABLE ONLY marketplace.categorias
    ADD CONSTRAINT categorias_pkey PRIMARY KEY (categoria_id);


--
-- Name: perguntas_produto perguntas_produto_pkey; Type: CONSTRAINT; Schema: marketplace; Owner: postgres
--

ALTER TABLE ONLY marketplace.perguntas_produto
    ADD CONSTRAINT perguntas_produto_pkey PRIMARY KEY (pergunta_id);


--
-- Name: produtos produtos_pkey; Type: CONSTRAINT; Schema: marketplace; Owner: postgres
--

ALTER TABLE ONLY marketplace.produtos
    ADD CONSTRAINT produtos_pkey PRIMARY KEY (produto_id);


--
-- Name: categorias ukf65h5k60655ylaslr1p7hvh9j; Type: CONSTRAINT; Schema: marketplace; Owner: postgres
--

ALTER TABLE ONLY marketplace.categorias
    ADD CONSTRAINT ukf65h5k60655ylaslr1p7hvh9j UNIQUE (nome_categoria);


--
-- Name: avaliacoes avaliacoes_pkey; Type: CONSTRAINT; Schema: social; Owner: postgres
--

ALTER TABLE ONLY social.avaliacoes
    ADD CONSTRAINT avaliacoes_pkey PRIMARY KEY (avaliacao_id);


--
-- Name: comentarios comentarios_pkey; Type: CONSTRAINT; Schema: social; Owner: postgres
--

ALTER TABLE ONLY social.comentarios
    ADD CONSTRAINT comentarios_pkey PRIMARY KEY (comentario_id);


--
-- Name: seguidores seguidores_pkey; Type: CONSTRAINT; Schema: social; Owner: postgres
--

ALTER TABLE ONLY social.seguidores
    ADD CONSTRAINT seguidores_pkey PRIMARY KEY (seguidor_id, seguido_id);


--
-- Name: idx_perguntas_produto_id; Type: INDEX; Schema: marketplace; Owner: postgres
--

CREATE INDEX idx_perguntas_produto_id ON marketplace.perguntas_produto USING btree (produto_id);


--
-- Name: detalhes detalhes_pessoa_id_fkey; Type: FK CONSTRAINT; Schema: clientes; Owner: postgres
--

ALTER TABLE ONLY clientes.detalhes
    ADD CONSTRAINT detalhes_pessoa_id_fkey FOREIGN KEY (pessoa_id) REFERENCES core.pessoas(pessoa_id) ON DELETE CASCADE;


--
-- Name: pedidos_coleta pedidos_coleta_cliente_id_fkey; Type: FK CONSTRAINT; Schema: clientes; Owner: postgres
--

ALTER TABLE ONLY clientes.pedidos_coleta
    ADD CONSTRAINT pedidos_coleta_cliente_id_fkey FOREIGN KEY (cliente_id) REFERENCES clientes.detalhes(pessoa_id) ON DELETE RESTRICT;


--
-- Name: catadores catadores_pessoa_id_fkey; Type: FK CONSTRAINT; Schema: colaboradores; Owner: postgres
--

ALTER TABLE ONLY colaboradores.catadores
    ADD CONSTRAINT catadores_pessoa_id_fkey FOREIGN KEY (pessoa_id) REFERENCES core.pessoas(pessoa_id) ON DELETE CASCADE;


--
-- Name: lojistas lojistas_pessoa_id_fkey; Type: FK CONSTRAINT; Schema: colaboradores; Owner: postgres
--

ALTER TABLE ONLY colaboradores.lojistas
    ADD CONSTRAINT lojistas_pessoa_id_fkey FOREIGN KEY (pessoa_id) REFERENCES core.pessoas(pessoa_id) ON DELETE CASCADE;


--
-- Name: sucateiros sucateiros_pessoa_id_fkey; Type: FK CONSTRAINT; Schema: colaboradores; Owner: postgres
--

ALTER TABLE ONLY colaboradores.sucateiros
    ADD CONSTRAINT sucateiros_pessoa_id_fkey FOREIGN KEY (pessoa_id) REFERENCES core.pessoas(pessoa_id) ON DELETE CASCADE;


--
-- Name: transportadores transportadores_pessoa_id_fkey; Type: FK CONSTRAINT; Schema: colaboradores; Owner: postgres
--

ALTER TABLE ONLY colaboradores.transportadores
    ADD CONSTRAINT transportadores_pessoa_id_fkey FOREIGN KEY (pessoa_id) REFERENCES core.pessoas(pessoa_id) ON DELETE CASCADE;


--
-- Name: veiculos veiculos_transportador_pessoa_id_fkey; Type: FK CONSTRAINT; Schema: colaboradores; Owner: postgres
--

ALTER TABLE ONLY colaboradores.veiculos
    ADD CONSTRAINT veiculos_transportador_pessoa_id_fkey FOREIGN KEY (transportador_pessoa_id) REFERENCES colaboradores.transportadores(pessoa_id) ON DELETE CASCADE;


--
-- Name: contas_digitais contas_digitais_pessoa_id_fkey; Type: FK CONSTRAINT; Schema: core; Owner: postgres
--

ALTER TABLE ONLY core.contas_digitais
    ADD CONSTRAINT contas_digitais_pessoa_id_fkey FOREIGN KEY (pessoa_id) REFERENCES core.pessoas(pessoa_id) ON DELETE CASCADE;


--
-- Name: mensagens mensagens_autor_id_fkey; Type: FK CONSTRAINT; Schema: core; Owner: postgres
--

ALTER TABLE ONLY core.mensagens
    ADD CONSTRAINT mensagens_autor_id_fkey FOREIGN KEY (autor_id) REFERENCES core.pessoas(pessoa_id) ON DELETE RESTRICT;


--
-- Name: mensagens mensagens_conversa_id_fkey; Type: FK CONSTRAINT; Schema: core; Owner: postgres
--

ALTER TABLE ONLY core.mensagens
    ADD CONSTRAINT mensagens_conversa_id_fkey FOREIGN KEY (conversa_id) REFERENCES core.conversas(conversa_id) ON DELETE CASCADE;


--
-- Name: participantes_conversa participantes_conversa_conversa_id_fkey; Type: FK CONSTRAINT; Schema: core; Owner: postgres
--

ALTER TABLE ONLY core.participantes_conversa
    ADD CONSTRAINT participantes_conversa_conversa_id_fkey FOREIGN KEY (conversa_id) REFERENCES core.conversas(conversa_id) ON DELETE CASCADE;


--
-- Name: participantes_conversa participantes_conversa_pessoa_id_fkey; Type: FK CONSTRAINT; Schema: core; Owner: postgres
--

ALTER TABLE ONLY core.participantes_conversa
    ADD CONSTRAINT participantes_conversa_pessoa_id_fkey FOREIGN KEY (pessoa_id) REFERENCES core.pessoas(pessoa_id) ON DELETE CASCADE;


--
-- Name: pessoa_perfil pessoa_perfil_perfil_id_fkey; Type: FK CONSTRAINT; Schema: core; Owner: postgres
--

ALTER TABLE ONLY core.pessoa_perfil
    ADD CONSTRAINT pessoa_perfil_perfil_id_fkey FOREIGN KEY (perfil_id) REFERENCES core.perfis(perfil_id) ON DELETE RESTRICT;


--
-- Name: pessoa_perfil pessoa_perfil_pessoa_id_fkey; Type: FK CONSTRAINT; Schema: core; Owner: postgres
--

ALTER TABLE ONLY core.pessoa_perfil
    ADD CONSTRAINT pessoa_perfil_pessoa_id_fkey FOREIGN KEY (pessoa_id) REFERENCES core.pessoas(pessoa_id) ON DELETE CASCADE;


--
-- Name: estoque estoque_gestor_id_fkey; Type: FK CONSTRAINT; Schema: inventario; Owner: postgres
--

ALTER TABLE ONLY inventario.estoque
    ADD CONSTRAINT estoque_gestor_id_fkey FOREIGN KEY (gestor_id) REFERENCES core.pessoas(pessoa_id) ON DELETE RESTRICT;


--
-- Name: estoque_produto estoque_produto_produto_id_fkey; Type: FK CONSTRAINT; Schema: inventario; Owner: postgres
--

ALTER TABLE ONLY inventario.estoque_produto
    ADD CONSTRAINT estoque_produto_produto_id_fkey FOREIGN KEY (produto_id) REFERENCES marketplace.produtos(produto_id) ON DELETE CASCADE;


--
-- Name: ordens_servico fkds12clh906lys0hqlav76n370; Type: FK CONSTRAINT; Schema: logistica; Owner: postgres
--

ALTER TABLE ONLY logistica.ordens_servico
    ADD CONSTRAINT fkds12clh906lys0hqlav76n370 FOREIGN KEY (transportador_pessoa_id) REFERENCES colaboradores.transportadores(pessoa_id);


--
-- Name: fretes fretes_modalidade_id_fkey; Type: FK CONSTRAINT; Schema: logistica; Owner: postgres
--

ALTER TABLE ONLY logistica.fretes
    ADD CONSTRAINT fretes_modalidade_id_fkey FOREIGN KEY (modalidade_id) REFERENCES logistica.modalidades_frete(modalidade_id) ON DELETE RESTRICT;


--
-- Name: fretes fretes_ordem_servico_id_fkey; Type: FK CONSTRAINT; Schema: logistica; Owner: postgres
--

ALTER TABLE ONLY logistica.fretes
    ADD CONSTRAINT fretes_ordem_servico_id_fkey FOREIGN KEY (ordem_servico_id) REFERENCES logistica.ordens_servico(ordem_id) ON DELETE CASCADE;


--
-- Name: fretes fretes_status_leilao_id_fkey; Type: FK CONSTRAINT; Schema: logistica; Owner: postgres
--

ALTER TABLE ONLY logistica.fretes
    ADD CONSTRAINT fretes_status_leilao_id_fkey FOREIGN KEY (status_leilao_id) REFERENCES logistica.status_leilao(status_id) ON DELETE RESTRICT;


--
-- Name: fretes fretes_transportador_selecionado_id_fkey; Type: FK CONSTRAINT; Schema: logistica; Owner: postgres
--

ALTER TABLE ONLY logistica.fretes
    ADD CONSTRAINT fretes_transportador_selecionado_id_fkey FOREIGN KEY (transportador_selecionado_id) REFERENCES colaboradores.transportadores(pessoa_id) ON DELETE SET NULL;


--
-- Name: itens_frete itens_frete_frete_id_fkey; Type: FK CONSTRAINT; Schema: logistica; Owner: postgres
--

ALTER TABLE ONLY logistica.itens_frete
    ADD CONSTRAINT itens_frete_frete_id_fkey FOREIGN KEY (frete_id) REFERENCES logistica.fretes(frete_id) ON DELETE CASCADE;


--
-- Name: lances lances_frete_id_fkey; Type: FK CONSTRAINT; Schema: logistica; Owner: postgres
--

ALTER TABLE ONLY logistica.lances
    ADD CONSTRAINT lances_frete_id_fkey FOREIGN KEY (frete_id) REFERENCES logistica.fretes(frete_id) ON DELETE CASCADE;


--
-- Name: lances lances_transportador_id_fkey; Type: FK CONSTRAINT; Schema: logistica; Owner: postgres
--

ALTER TABLE ONLY logistica.lances
    ADD CONSTRAINT lances_transportador_id_fkey FOREIGN KEY (transportador_id) REFERENCES colaboradores.transportadores(pessoa_id) ON DELETE RESTRICT;


--
-- Name: ordens_servico ordens_servico_cliente_solicitante_id_fkey; Type: FK CONSTRAINT; Schema: logistica; Owner: postgres
--

ALTER TABLE ONLY logistica.ordens_servico
    ADD CONSTRAINT ordens_servico_cliente_solicitante_id_fkey FOREIGN KEY (cliente_solicitante_id) REFERENCES clientes.detalhes(pessoa_id) ON DELETE RESTRICT;


--
-- Name: ordens_servico ordens_servico_transportador_designado_id_fkey; Type: FK CONSTRAINT; Schema: logistica; Owner: postgres
--

ALTER TABLE ONLY logistica.ordens_servico
    ADD CONSTRAINT ordens_servico_transportador_designado_id_fkey FOREIGN KEY (transportador_designado_id) REFERENCES colaboradores.transportadores(pessoa_id) ON DELETE SET NULL;


--
-- Name: perguntas_produto perguntas_produto_autor_id_fkey; Type: FK CONSTRAINT; Schema: marketplace; Owner: postgres
--

ALTER TABLE ONLY marketplace.perguntas_produto
    ADD CONSTRAINT perguntas_produto_autor_id_fkey FOREIGN KEY (autor_id) REFERENCES core.pessoas(pessoa_id) ON DELETE RESTRICT;


--
-- Name: perguntas_produto perguntas_produto_pergunta_pai_id_fkey; Type: FK CONSTRAINT; Schema: marketplace; Owner: postgres
--

ALTER TABLE ONLY marketplace.perguntas_produto
    ADD CONSTRAINT perguntas_produto_pergunta_pai_id_fkey FOREIGN KEY (pergunta_pai_id) REFERENCES marketplace.perguntas_produto(pergunta_id) ON DELETE SET NULL;


--
-- Name: perguntas_produto perguntas_produto_produto_id_fkey; Type: FK CONSTRAINT; Schema: marketplace; Owner: postgres
--

ALTER TABLE ONLY marketplace.perguntas_produto
    ADD CONSTRAINT perguntas_produto_produto_id_fkey FOREIGN KEY (produto_id) REFERENCES marketplace.produtos(produto_id) ON DELETE CASCADE;


--
-- Name: produtos produtos_categoria_id_fkey; Type: FK CONSTRAINT; Schema: marketplace; Owner: postgres
--

ALTER TABLE ONLY marketplace.produtos
    ADD CONSTRAINT produtos_categoria_id_fkey FOREIGN KEY (categoria_id) REFERENCES marketplace.categorias(categoria_id) ON DELETE RESTRICT;


--
-- Name: produtos produtos_vendedor_id_fkey; Type: FK CONSTRAINT; Schema: marketplace; Owner: postgres
--

ALTER TABLE ONLY marketplace.produtos
    ADD CONSTRAINT produtos_vendedor_id_fkey FOREIGN KEY (vendedor_id) REFERENCES core.pessoas(pessoa_id) ON DELETE RESTRICT;


--
-- Name: avaliacoes avaliacoes_avaliado_id_fkey; Type: FK CONSTRAINT; Schema: social; Owner: postgres
--

ALTER TABLE ONLY social.avaliacoes
    ADD CONSTRAINT avaliacoes_avaliado_id_fkey FOREIGN KEY (avaliado_id) REFERENCES core.pessoas(pessoa_id) ON DELETE RESTRICT;


--
-- Name: avaliacoes avaliacoes_avaliador_id_fkey; Type: FK CONSTRAINT; Schema: social; Owner: postgres
--

ALTER TABLE ONLY social.avaliacoes
    ADD CONSTRAINT avaliacoes_avaliador_id_fkey FOREIGN KEY (avaliador_id) REFERENCES core.pessoas(pessoa_id) ON DELETE RESTRICT;


--
-- Name: avaliacoes avaliacoes_ordem_servico_id_fkey; Type: FK CONSTRAINT; Schema: social; Owner: postgres
--

ALTER TABLE ONLY social.avaliacoes
    ADD CONSTRAINT avaliacoes_ordem_servico_id_fkey FOREIGN KEY (ordem_servico_id) REFERENCES logistica.ordens_servico(ordem_id) ON DELETE SET NULL;


--
-- Name: avaliacoes avaliacoes_produto_id_fkey; Type: FK CONSTRAINT; Schema: social; Owner: postgres
--

ALTER TABLE ONLY social.avaliacoes
    ADD CONSTRAINT avaliacoes_produto_id_fkey FOREIGN KEY (produto_id) REFERENCES marketplace.produtos(produto_id) ON DELETE SET NULL;


--
-- Name: comentarios comentarios_autor_id_fkey; Type: FK CONSTRAINT; Schema: social; Owner: postgres
--

ALTER TABLE ONLY social.comentarios
    ADD CONSTRAINT comentarios_autor_id_fkey FOREIGN KEY (autor_id) REFERENCES core.pessoas(pessoa_id) ON DELETE RESTRICT;


--
-- Name: comentarios comentarios_comentario_pai_id_fkey; Type: FK CONSTRAINT; Schema: social; Owner: postgres
--

ALTER TABLE ONLY social.comentarios
    ADD CONSTRAINT comentarios_comentario_pai_id_fkey FOREIGN KEY (comentario_pai_id) REFERENCES social.comentarios(comentario_id) ON DELETE SET NULL;


--
-- Name: comentarios comentarios_produto_id_fkey; Type: FK CONSTRAINT; Schema: social; Owner: postgres
--

ALTER TABLE ONLY social.comentarios
    ADD CONSTRAINT comentarios_produto_id_fkey FOREIGN KEY (produto_id) REFERENCES marketplace.produtos(produto_id) ON DELETE CASCADE;


--
-- Name: seguidores seguidores_seguido_id_fkey; Type: FK CONSTRAINT; Schema: social; Owner: postgres
--

ALTER TABLE ONLY social.seguidores
    ADD CONSTRAINT seguidores_seguido_id_fkey FOREIGN KEY (seguido_id) REFERENCES core.pessoas(pessoa_id) ON DELETE CASCADE;


--
-- Name: seguidores seguidores_seguidor_id_fkey; Type: FK CONSTRAINT; Schema: social; Owner: postgres
--

ALTER TABLE ONLY social.seguidores
    ADD CONSTRAINT seguidores_seguidor_id_fkey FOREIGN KEY (seguidor_id) REFERENCES core.pessoas(pessoa_id) ON DELETE CASCADE;


--
-- PostgreSQL database dump complete
--

\unrestrict JO5fR7NJuSIhfduklclb0zD5XPiUbdrTUmIP5T3QMFioqUYzXf6qGqBRlQbQ7vD


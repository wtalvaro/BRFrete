-- ######################################################################
-- 10. POPULAÇÃO DE DADOS DE EXEMPLO (DML)
-- SCRIPT ATUALIZADO (Inclui Limpeza, Chat, Q&A e Correção da SENHA ÚNICA)
-- ######################################################################

-- Define o fuso horário para consistência (opcional, mas recomendado)
SET TIME ZONE 'America/Sao_Paulo';

-- ======================================================================
-- 0. LIMPEZA TOTAL DE DADOS (TRUNCATE)
-- Limpa todas as tabelas e reinicia as sequências (IDs) para garantir
-- que os IDs de 1 a N sejam usados nas inserções abaixo.
-- (Comando adequado para PostgreSQL)
-- **CORREÇÃO APLICADA AQUI: Agrupando todas as tabelas na lista**
-- ======================================================================

TRUNCATE TABLE
    core.pessoas, core.contas_digitais, core.perfis, core.pessoa_perfil, core.conversas, core.participantes_conversa, core.mensagens,
    colaboradores.sucateiros, colaboradores.transportadores, colaboradores.veiculos,
    clientes.detalhes, clientes.pedidos_coleta,
    logistica.modalidades_frete, logistica.status_leilao, logistica.antt_parametros, logistica.cotacoes_materiais, logistica.ordens_servico, logistica.fretes, logistica.itens_frete, logistica.lances,
    marketplace.categorias, marketplace.produtos, marketplace.perguntas_produto,
    inventario.estoque, inventario.estoque_produto,
    social.avaliacoes, social.comentarios, social.seguidores
RESTART IDENTITY CASCADE;

-- ======================================================================
-- A. CORE DATA (Pessoas, Perfis e Contas)
-- ======================================================================

-- SENHA PADRÃO: Todos terão a mesma senha hash fictícia 'HASH_TESTE_COMUM'

-- 1. core.pessoas (IDs gerados: 1, 2, 3, 4)
-- Pessoa 1: Cliente PF
INSERT INTO core.pessoas (nome, documento, email, telefone, is_cliente, senha) VALUES
('Ana Silva', '11122233344', 'ana.silva@cliente.com', '11988887777', true, 'HASH_TESTE_COMUM');

-- Pessoa 2: Transportador PJ (Colaborador)
INSERT INTO core.pessoas (nome, documento, email, telefone, is_colaborador, ativo, senha) VALUES
('Logistica Rápida LTDA', '12345678000190', 'contato@logistica-rapida.com', '1133334444', true, true, 'HASH_TESTE_COMUM');

-- Pessoa 3: Sucateiro PJ (Colaborador)
INSERT INTO core.pessoas (nome, documento, email, telefone, is_colaborador, ativo, senha) VALUES
('Sucata Forte Eireli', '99887766000101', 'gerencia@sucataforte.com', '2122221111', true, true, 'HASH_TESTE_COMUM');

-- Pessoa 4: Administrador (Interno)
INSERT INTO core.pessoas (nome, documento, email, telefone, ativo, senha) VALUES
('Carlos Admin', '55566677788', 'carlos.admin@empresa.com', '1155556666', true, 'HASH_TESTE_COMUM');


-- 2. core.perfis (IDs gerados: 1, 2, 3)
INSERT INTO core.perfis (nome_perfil, descricao) VALUES
('LEAD', 'Usuário recém-cadastrado com e-mail/senha, aguardando complementação de dados.'),
('USUARIO_PADRAO', 'Usuário que completou o cadastro básico (documento/endereço), mas sem histórico de transações.'),
('CLIENTE', 'Usuário que já utilizou o site para comprar ou solicitar fretes.'),
('COLABORADOR', 'Usuário que oferece serviços de transporte de frete ou variados (Colaborador).');

-- 3. core.pessoa_perfil (Ana é Usuário Padrão, Carlos é Admin e Financeiro)
INSERT INTO core.pessoa_perfil (pessoa_id, perfil_id) VALUES
(1, 3), -- Ana (Cliente) como CLIENTE
(2, 4), -- Logistica Rápida LTDA como COLABORADOR
(3, 4), -- Sucata Forte Eireli como COLABORADOR
(4, 2); -- Carlos como USUARIO_PADRAO


-- 4. core.contas_digitais (Sucateiro e Transportador possuem contas para receber)
-- MODIFICAÇÃO: Status KYC do Transportador 2 mudado para 'EM_ANALISE'
INSERT INTO core.contas_digitais (pessoa_id, conta_uuid, status_kyc) VALUES
(2, 'uuid-log-rapida-9988', 'EM_ANALISE'),
(3, 'uuid-sucata-forte-7766', 'APROVADO');


-- ======================================================================
-- B. COLABORADORES E CLIENTES (Detalhes)
-- ======================================================================

-- 1. colaboradores.transportadores (Pessoa ID 2)
INSERT INTO colaboradores.transportadores (pessoa_id, licenca_transporte) VALUES
(2, 'ANTT-1234567-BR');

-- 2. colaboradores.sucateiros (Pessoa ID 3)
-- MODIFICAÇÃO: Adicionado um CNPJ secundário para simular um registro mais completo
INSERT INTO colaboradores.sucateiros (pessoa_id, razao_social, cnpj_secundario, licenca_ambiental, endereco_patio) VALUES
(3, 'Sucata Forte Eireli', '99887766000280', 'LIC-AMBIENTAL-SF-2025', 'Rua dos Sucateiros, 500, São Paulo/SP');

-- 3. colaboradores.veiculos (Veículo para o Transportador 2) (ID gerado: 1)
INSERT INTO colaboradores.veiculos (transportador_pessoa_id, matricula, tipo_veiculo, capacidade_peso_kg) VALUES
(2, 'ABC1D23', 'Caminhão VUC', 3000.00);


-- 4. clientes.detalhes (Pessoa ID 1)
INSERT INTO clientes.detalhes (pessoa_id, tipo_cliente) VALUES
(1, 'PF');

-- 5. clientes.pedidos_coleta (Pedido da Ana) (ID gerado: 1)
INSERT INTO clientes.pedidos_coleta (cliente_id, descricao_pedido) VALUES
(1, 'Muitas caixas de papelão e garrafas PET do final de semana.');


-- ======================================================================
-- C. LOGÍSTICA (Setup e Transações)
-- ======================================================================

-- 1. logistica.modalidades_frete (IDs: 1, 2)
INSERT INTO logistica.modalidades_frete (nome_modalidade) VALUES
('Lotação'),
('Fracionada');

-- 2. logistica.status_leilao (IDs: 1, 2)
INSERT INTO logistica.status_leilao (nome_status) VALUES
('ABERTO'),
('ENCERRADO');

-- 3. logistica.antt_parametros (Exemplo de parâmetros para cálculo) (IDs: 1, 2)
INSERT INTO logistica.antt_parametros (chave, valor, descricao) VALUES
('C_KM_BASICO', 1.5000, 'Custo por km rodado'),
('C_EIXO_2', 0.2500, 'Custo variável por eixo e km (2 eixos)');

-- 4. logistica.cotacoes_materiais (Exemplo de preço de mercado) (IDs: 1, 2)
INSERT INTO logistica.cotacoes_materiais (material_nome, preco_medio_kg) VALUES
('Plástico PET', 1.85),
('Alumínio', 6.50);

-- 5. logistica.ordens_servico (Cliente 1 para Sucateiro 3) (IDs: 1, 2)
-- Ordem 1: Pendente
INSERT INTO logistica.ordens_servico (cliente_solicitante_id, data_prevista_coleta, endereco_coleta, cep_coleta, cep_destino, status) VALUES
(1, '2025-10-20', 'Av. Paulista, 1000, São Paulo/SP', '01310000', '08000000', 'PENDENTE');

-- Ordem 2: Confirmada e Designada (Transportador 2)
INSERT INTO logistica.ordens_servico (cliente_solicitante_id, transportador_designado_id, data_prevista_coleta, endereco_coleta, cep_coleta, cep_destino, status) VALUES
(1, 2, '2025-10-22', 'Rua da Consolação, 50, São Paulo/SP', '01301000', '08000000', 'CONFIRMADO');


-- 6. logistica.fretes (Ligado à Ordem 2) (ID gerado: 1)
-- MODIFICAÇÃO: Definido o transportador_selecionado_id (Pessoa 2), pois já fez o lance vencedor.
INSERT INTO logistica.fretes (ordem_servico_id, modalidade_id, status_leilao_id, preco_sugerido, antt_piso_minimo, custo_base_mercado, distancia_km, transportador_selecionado_id) VALUES
(2, 1, 1, 150.00, 95.50, 130.00, 45.8, 2);


-- 7. logistica.itens_frete (Itens do Frete 1) (IDs: 1, 2)
INSERT INTO logistica.itens_frete (frete_id, descricao, tipo_material, peso_estimado_kg) VALUES
(1, 'Caixas grandes de papelão', 'Papel', 150.00),
(1, 'Garrafas PET', 'Plástico', 50.00);


-- 8. logistica.lances (Lances para o Frete 1) (IDs: 1, 2, 3)
-- Transportador 2 faz o lance vencedor (is_vencedor: true)
INSERT INTO logistica.lances (frete_id, transportador_id, valor_lance, is_vencedor) VALUES
(1, 2, 135.00, true);

-- Outro lance (que não foi vencedor)
INSERT INTO logistica.lances (frete_id, transportador_id, valor_lance, is_vencedor) VALUES
(1, 2, 138.00, false); -- Lance maior, mas posterior ao primeiro (ou foi cancelado, etc.)


-- ======================================================================
-- D. MARKETPLACE (Produtos e Estoque de Produtos)
-- ======================================================================

-- 1. marketplace.categorias (IDs: 1, 2)
INSERT INTO marketplace.categorias (nome_categoria, tipo_geral) VALUES
('Eletrônicos Recondicionados', 'Venda'),
('Móveis Doação', 'Doação');


-- 2. marketplace.produtos (Sucateiro 3 como Vendedor) (IDs: 1, 2)
-- Produto 1: Venda (Preço > 0, is_doacao: false)
INSERT INTO marketplace.produtos (vendedor_id, categoria_id, titulo, descricao, preco, quantidade, unidade_medida, is_doacao) VALUES
(3, 1, 'Notebook Dell Recondicionado', 'Em bom estado, ideal para tarefas básicas.', 850.00, 5, 'UN', false);

-- Produto 2: Doação (Preço = 0, is_doacao: true)
INSERT INTO marketplace.produtos (vendedor_id, categoria_id, titulo, descricao, preco, quantidade, unidade_medida, is_doacao) VALUES
(3, 2, 'Cadeira de Escritório Simples', 'Para retirada no local. Doação.', 0.00, 2, 'UN', true);


-- 3. inventario.estoque_produto (Estoque para o Marketplace)
-- Estoque Produto 1 (5 unidades)
INSERT INTO inventario.estoque_produto (produto_id, quantidade_disponivel) VALUES
(1, 5);

-- Estoque Produto 2 (2 unidades)
INSERT INTO inventario.estoque_produto (produto_id, quantidade_disponivel) VALUES
(2, 2);


-- ======================================================================
-- E. INVENTÁRIO (Estoque de Sucata a Granel)
-- ======================================================================

-- inventario.estoque (Estoque de Sucateiro 3) (IDs: 1, 2)
INSERT INTO inventario.estoque (gestor_id, tipo_material, quantidade_kg, localizacao) VALUES
(3, 'Plástico PET', 1500.00, 'Pátio 1, Setor A'),
(3, 'Cobre Fios', 250.50, 'Almoxarifado Seguro');


-- ======================================================================
-- F. SOCIAL (Avaliações, Comentários e Seguidores)
-- ======================================================================

-- 1. social.avaliacoes (IDs: 1, 2)
-- Avaliação 1: Cliente 1 avalia Transportador 2 por uma Ordem de Serviço (OS 2)
INSERT INTO social.avaliacoes (avaliador_id, avaliado_id, ordem_servico_id, pontuacao, comentario) VALUES
(1, 2, 2, 5, 'Serviço de coleta rápido e transportador muito profissional.');

-- Avaliação 2: Cliente 1 avalia o Produto 1 do Sucateiro 3
INSERT INTO social.avaliacoes (avaliador_id, avaliado_id, produto_id, pontuacao, comentario) VALUES
(1, 3, 1, 4, 'O notebook estava conforme a descrição. Bom negócio.');


-- 2. social.comentarios (IDs: 1, 2)
-- Comentário 1: Cliente 1 comenta no Produto 1 (ID gerado: 1)
INSERT INTO social.comentarios (autor_id, produto_id, texto_comentario) VALUES
(1, 1, 'Ainda tem garantia?');

-- Comentário 2: Sucateiro 3 responde ao Comentário 1 (reply) (ID gerado: 2)
INSERT INTO social.comentarios (autor_id, produto_id, texto_comentario, comentario_pai_id) VALUES
(3, 1, 'Sim, oferecemos 3 meses de garantia pós-venda.', 1);


-- 3. social.seguidores (Cliente 1 segue Sucateiro 3)
INSERT INTO social.seguidores (seguidor_id, seguido_id) VALUES
(1, 3);


-- ======================================================================
-- G. CORE - CHAT P2P (Conversas, Participantes e Mensagens)
-- Usuários: Pessoa 1 (Cliente) e Pessoa 3 (Sucateiro)
-- ======================================================================

-- 1. core.conversas (ID gerado: 1)
INSERT INTO core.conversas (tipo_conversa) VALUES
('PRIVADA');

-- 2. core.participantes_conversa
INSERT INTO core.participantes_conversa (conversa_id, pessoa_id) VALUES
(1, 1), -- Cliente Ana Silva
(1, 3); -- Sucateiro Sucata Forte Eireli

-- 3. core.mensagens (IDs gerados: 1, 2)
-- Mensagem 1: Cliente 1 pergunta
INSERT INTO core.mensagens (conversa_id, autor_id, conteudo) VALUES
(1, 1, 'Olá, o notebook ainda está disponível? Tenho interesse na compra e retirada.');

-- Mensagem 2: Sucateiro 3 responde
INSERT INTO core.mensagens (conversa_id, autor_id, conteudo) VALUES
(1, 3, 'Boa tarde! Sim, ainda temos unidades. Podemos agendar a visita?');


-- ======================================================================
-- H. MARKETPLACE - Q&A (Perguntas e Respostas do Produto)
-- Produto: 1 (Notebook Dell), Autor da Pergunta: 1 (Ana), Autor da Resposta: 3 (Sucateiro)
-- ======================================================================

-- 1. Pergunta (ID gerado: 1)
INSERT INTO marketplace.perguntas_produto (produto_id, autor_id, texto_conteudo) VALUES
(1, 1, 'Qual a versão do sistema operacional que vem instalada?');

-- 2. Resposta (ID gerado: 2, pergunta_pai_id: 1)
INSERT INTO marketplace.perguntas_produto (produto_id, autor_id, texto_conteudo, pergunta_pai_id) VALUES
(1, 3, 'Ele vem com Windows 10 Home (ativado) e pronto para uso.', 1);
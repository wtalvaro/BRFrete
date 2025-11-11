package br.com.wta.frete.marketplace.service.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import br.com.wta.frete.marketplace.controller.dto.PerguntaProdutoRequest;
import br.com.wta.frete.marketplace.controller.dto.PerguntaProdutoResponse;
import br.com.wta.frete.marketplace.entity.PerguntaProduto;

/**
 * Interface Mapper MapStruct para a entidade PerguntaProduto.
 * * ATUALIZAÇÃO: Alinhamento com o modelo de threading (pergunta_pai_id) e
 * campos corrigidos.
 */
@Mapper(componentModel = "spring", uses = { ProdutoMapper.class })
public interface PerguntaProdutoMapper {

	// =================================================================
	// 1. Mapeamento de Entity para Response (com auto-referência)
	// =================================================================
	/**
	 * Converte a entidade PerguntaProduto (Entity) para o DTO de Resposta
	 * (Response).
	 * O MapStruct gerará a lógica recursiva para mapear a lista 'respostas'.
	 *
	 * @param entity A entidade PerguntaProduto.
	 * @return O DTO PerguntaProdutoResponse.
	 */
	@Mapping(source = "id", target = "perguntaId")
	@Mapping(source = "produto.id", target = "produtoId")
	@Mapping(source = "autor.id", target = "autorId")
	@Mapping(source = "autor.nome", target = "nomeAutor")
	@Mapping(source = "perguntaPai.id", target = "perguntaPaiId") // Mapeia o ID do pai
	@Mapping(source = "textoConteudo", target = "textoConteudo") // Novo nome
	@Mapping(source = "dataPublicacao", target = "dataPublicacao") // Novo nome/tipo
	@Mapping(source = "isPublica", target = "isPublica")
	PerguntaProdutoResponse toResponse(PerguntaProduto entity);

	/**
	 * Converte uma lista de entidades para uma lista de DTOs de Resposta.
	 */
	List<PerguntaProdutoResponse> toResponseList(List<PerguntaProduto> entities);

	// =================================================================
	// 2. Mapeamento de Request para Entity (Criação)
	// =================================================================
	/**
	 * Converte o DTO de Requisição (Request) para a entidade PerguntaProduto.
	 *
	 * Os objetos de relacionamento 'produto', 'autor' e 'perguntaPai' (para
	 * respostas)
	 * devem ser ignorados aqui e resolvidos na Camada de Serviço.
	 */
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "produto", ignore = true)
	@Mapping(target = "autor", ignore = true)
	@Mapping(target = "perguntaPai", ignore = true) // Setado no Serviço usando perguntaPaiId
	@Mapping(target = "respostas", ignore = true) // Não há respostas ao criar
	@Mapping(target = "dataPublicacao", ignore = true) // Definido pela Entidade/BD
	@Mapping(source = "textoConteudo", target = "textoConteudo")
	@Mapping(source = "isPublica", target = "isPublica")
	PerguntaProduto toEntity(PerguntaProdutoRequest request);

	// =================================================================
	// 3. Mapeamento de ATUALIZAÇÃO (update)
	// =================================================================
	/**
	 * Atualiza uma Entidade PerguntaProduto existente com os dados do Request.
	 * Usado primariamente para moderação (mudar isPublica) ou edição de conteúdo.
	 */
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "produto", ignore = true)
	@Mapping(target = "autor", ignore = true)
	@Mapping(target = "perguntaPai", ignore = true)
	@Mapping(target = "respostas", ignore = true)
	@Mapping(target = "dataPublicacao", ignore = true) // Mantém a data de criação
	void updateEntity(PerguntaProdutoRequest request, @MappingTarget PerguntaProduto entity);
}
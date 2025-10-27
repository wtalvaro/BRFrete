package br.com.wta.frete.marketplace.service.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import br.com.wta.frete.marketplace.controller.dto.PerguntaProdutoRequest;
import br.com.wta.frete.marketplace.controller.dto.PerguntaProdutoResponse;
import br.com.wta.frete.marketplace.entity.PerguntaProduto;

/**
 * Interface Mapper MapStruct para a entidade PerguntaProduto. Gerencia a
 * conversão entre PerguntaProduto, PerguntaProdutoRequest e
 * PerguntaProdutoResponse.
 */
@Mapper(componentModel = "spring")
public interface PerguntaProdutoMapper {

	/**
	 * Converte a entidade PerguntaProduto (Entity) para o DTO de Resposta
	 * (Response).
	 * <p>
	 * Realiza o mapeamento de campos simples e a conversão de IDs de objetos
	 * aninhados (Produto e Autor).
	 *
	 * @param entity A entidade PerguntaProduto.
	 * @return O DTO PerguntaProdutoResponse.
	 */
	@Mapping(source = "id", target = "perguntaId")
	@Mapping(source = "produto.id", target = "produtoId")
	@Mapping(source = "autor.id", target = "autorId")
	// Mapeamento de nomeAutor (assumindo que Pessoa tem um campo 'nome')
	@Mapping(source = "autor.nome", target = "nomeAutor")
	PerguntaProdutoResponse toResponse(PerguntaProduto entity);

	/**
	 * Converte uma lista de entidades para uma lista de DTOs de Resposta.
	 *
	 * @param entities A lista de entidades PerguntaProduto.
	 * @return A lista de DTOs PerguntaProdutoResponse.
	 */
	List<PerguntaProdutoResponse> toResponseList(List<PerguntaProduto> entities);

	/**
	 * Converte o DTO de Requisição (Request) para a entidade PerguntaProduto
	 * (Entity).
	 * <p>
	 * O campo 'id' é ignorado (autogerado). Os objetos de relacionamento 'produto'
	 * e 'autor' são ignorados, pois a busca e atribuição dessas entidades (usando
	 * produtoId e autorId do Request) será realizada na Camada de Serviço
	 * (Service).
	 *
	 * @param request O DTO PerguntaProdutoRequest.
	 * @return A entidade PerguntaProduto.
	 */
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "produto", ignore = true)
	@Mapping(target = "autor", ignore = true)
	// Os campos dataPergunta e dataResposta serão definidos pelo Hibernate/Serviço
	@Mapping(target = "dataPergunta", ignore = true)
	@Mapping(target = "dataResposta", ignore = true)
	PerguntaProduto toEntity(PerguntaProdutoRequest request);
}
package br.com.wta.frete.marketplace.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget; // Importação essencial para o método de atualização
import org.mapstruct.ReportingPolicy;

import br.com.wta.frete.marketplace.controller.dto.ProdutoRequest;
import br.com.wta.frete.marketplace.controller.dto.ProdutoResponse;
import br.com.wta.frete.marketplace.entity.Produto;

/**
 * Interface Mapper MapStruct para a conversão entre a Entidade Produto
 * (marketplace.produtos) e seus DTOs (Request/Response).
 *
 * * ATUALIZAÇÃO: Confirmação do método de UPDATE (updateProduto) e alinhamento
 * com todos os campos da Entidade.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProdutoMapper {

	// =================================================================
	// 1. Mapeamento de CRIAÇÃO (Request para Entity)
	// =================================================================
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "lojista", ignore = true) // Setado no Serviço (FK)
	@Mapping(target = "categoria", ignore = true) // Setado no Serviço (FK)
	@Mapping(target = "dataPublicacao", ignore = true) // Setado automaticamente
	@Mapping(target = "isDisponivel", source = "isDisponivel") // Mapeamento explícito para booleans
	@Mapping(target = "isDoacao", source = "isDoacao") // Mapeamento explícito para booleans
	@Mapping(target = "unidadeMedida", source = "unidadeMedida") // MapStruct converte String para Enum
	Produto toEntity(ProdutoRequest request);

	// =================================================================
	// 2. Mapeamento de RESPOSTA (Entity para Response DTO)
	// =================================================================
	@Mapping(source = "lojista.pessoaId", target = "lojistaPessoaId")
	@Mapping(source = "categoria.id", target = "categoriaId")
	@Mapping(source = "id", target = "produtoId")
	// Mapeamentos para Response:
	@Mapping(source = "titulo", target = "titulo")
	@Mapping(source = "dataPublicacao", target = "dataPublicacao")
	@Mapping(source = "quantidade", target = "quantidade")
	@Mapping(source = "unidadeMedida", target = "unidadeMedida") // MapStruct converte Enum para String
	@Mapping(source = "isDoacao", target = "isDoacao")
	@Mapping(source = "isDisponivel", target = "isDisponivel")
	ProdutoResponse toResponse(Produto entity);

	// =================================================================
	// 3. Mapeamento de ATUALIZAÇÃO (updateEntity / updateProduto)
	// =================================================================
	/**
	 * Atualiza (faz o merge) de uma Entidade Produto existente com os dados de um
	 * ProdutoRequest.
	 *
	 * @param request O DTO de requisição com os novos dados.
	 * @param produto A Entidade Produto alvo da atualização (anotada
	 *                com @MappingTarget).
	 */
	@Mapping(target = "id", ignore = true) // Nunca altera o ID
	@Mapping(target = "lojista", ignore = true) // Nunca altera a FK do Lojista (vendedor)
	@Mapping(target = "categoria", ignore = true) // Nunca altera a FK da Categoria
	@Mapping(target = "dataPublicacao", ignore = true) // Não altera a data de criação
	@Mapping(target = "isDisponivel", source = "isDisponivel")
	@Mapping(target = "isDoacao", source = "isDoacao")
	@Mapping(target = "unidadeMedida", source = "unidadeMedida")
	void updateEntity(ProdutoRequest request, @MappingTarget Produto produto); // Renomeado para 'updateProduto' para
																				// ser descritivo.

}
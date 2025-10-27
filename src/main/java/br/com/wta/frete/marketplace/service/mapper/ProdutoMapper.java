package br.com.wta.frete.marketplace.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import br.com.wta.frete.marketplace.controller.dto.ProdutoRequest;
import br.com.wta.frete.marketplace.controller.dto.ProdutoResponse;
import br.com.wta.frete.marketplace.entity.Produto;

/**
 * Interface Mapper MapStruct para a conversão entre a Entidade Produto
 * (marketplace.produtos) e seus DTOs (Request/Response).
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProdutoMapper {

	// Mapeamento de Request para Entity (mantido)
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "lojista", ignore = true)
	@Mapping(target = "categoria", ignore = true)
	@Mapping(target = "dataListagem", ignore = true)
	Produto toEntity(ProdutoRequest request);

	/**
	 * Mapeia a Entidade Produto para o DTO de Resposta de Produto.
	 *
	 * @param entity A Entidade Produto.
	 * @return O DTO de resposta (ProdutoResponse).
	 */
	// Mapeamento encadeado: O MapStruct chamará entity.getLojista().getPessoaId()
	@Mapping(source = "lojista.pessoaId", target = "lojistaPessoaId") // <--- SOLUÇÃO IMUTÁVEL: Define o campo no
																		// construtor.
	@Mapping(source = "categoria.id", target = "categoriaId")
	@Mapping(source = "id", target = "produtoId")
	ProdutoResponse toResponse(Produto entity);
}
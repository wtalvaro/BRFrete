package br.com.wta.frete.colaboradores.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import br.com.wta.frete.colaboradores.controller.dto.CatadorRequest;
import br.com.wta.frete.colaboradores.controller.dto.CatadorResponse;
import br.com.wta.frete.colaboradores.entity.Catador;
import br.com.wta.frete.core.controller.dto.PessoaResponse;
import br.com.wta.frete.core.entity.Pessoa;

/**
 * Interface Mapper para conversão entre Entidade Catador e seus DTOs,
 * utilizando a biblioteca MapStruct.
 *
 * @Mapper(componentModel = "spring") garante que o Spring crie uma instância
 *                        injetável (@Component) deste Mapper.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE) // <-- ADICIONADO!
public interface CatadorMapper {

	// Instância estática para uso fora do contexto Spring, se necessário.
	CatadorMapper INSTANCE = Mappers.getMapper(CatadorMapper.class);

	// ====================================================================
	// Mapeamento de Entidade para Response DTO
	// ====================================================================

	/**
	 * Converte a Entidade Catador para o DTO de Resposta CatadorResponse.
	 *
	 * @param catador A entidade Catador.
	 * @return O DTO CatadorResponse correspondente.
	 */
	@Mapping(source = "pessoaId", target = "pessoaId")
	@Mapping(source = "areaAtuacaoGeografica", target = "areaAtuacaoGeografica")
	@Mapping(source = "dataNascimento", target = "dataNascimento")
	@Mapping(source = "associacaoId", target = "associacaoId")
	// Mapeia o objeto Pessoa aninhado na Entidade Catador para o DTO PessoaResponse
	@Mapping(source = "pessoa", target = "dadosPessoa")
	CatadorResponse toResponse(Catador catador);

	/**
	 * Mapeamento específico para a Entidade Pessoa para o DTO PessoaResponse.
	 *
	 * @param pessoa A entidade Pessoa.
	 * @return O DTO PessoaResponse correspondente.
	 */
	@Mapping(source = "id", target = "id") // ID da Pessoa no DTO de Pessoa
	@Mapping(source = "nome", target = "nomeCompleto")
	PessoaResponse toPessoaResponse(Pessoa pessoa);

	// ====================================================================
	// Mapeamento de Request DTO para Entidade
	// ====================================================================

	/**
	 * Converte o DTO de Requisição CatadorRequest para a Entidade Catador. Este é
	 * usado para a CRIAÇÃO de um novo Catador.
	 *
	 * NOTA: O campo 'pessoa' é deixado de fora do mapeamento, pois será populado no
	 * Service após buscar/criar a entidade Pessoa.
	 *
	 * @param request O DTO CatadorRequest.
	 * @return A nova entidade Catador.
	 */
	@Mapping(source = "pessoaId", target = "pessoaId")
	@Mapping(source = "areaAtuacaoGeografica", target = "areaAtuacaoGeografica")
	@Mapping(source = "dataNascimento", target = "dataNascimento")
	@Mapping(source = "associacaoId", target = "associacaoId")
	@Mapping(target = "pessoa", ignore = true) // A Pessoa deve ser setada pelo Service
	Catador toEntity(CatadorRequest request);

	/**
	 * Atualiza uma Entidade Catador existente com dados de um CatadorRequest.
	 *
	 * NOTA: Ignoramos 'pessoaId' e 'pessoa' aqui, pois não devem ser alterados.
	 *
	 * @param request O DTO CatadorRequest com dados de atualização.
	 * @param catador A entidade Catador a ser atualizada.
	 */
	@Mapping(source = "dataNascimento", target = "dataNascimento")
	@Mapping(source = "associacaoId", target = "associacaoId")
	@Mapping(source = "areaAtuacaoGeografica", target = "areaAtuacaoGeografica")
	@Mapping(target = "pessoaId", ignore = true) // Chave primária não é atualizável
	@Mapping(target = "pessoa", ignore = true) // Relacionamento não é atualizável
	void updateEntityFromRequest(CatadorRequest request, @MappingTarget Catador catador);
}
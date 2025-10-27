package br.com.wta.frete.core.service.mapper;

import br.com.wta.frete.core.controller.dto.MensagemRequest;
import br.com.wta.frete.core.controller.dto.MensagemResponse;
import br.com.wta.frete.core.entity.Mensagem;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * Mapper para conversão entre a Entidade Mensagem (core.mensagens) e seus DTOs.
 * Usa o MapStruct com componentModel = "spring" para injeção de dependência.
 */
@Mapper(componentModel = "spring")
public interface MensagemMapper {

	/**
	 * Converte um MensagemRequest em uma entidade Mensagem.
	 * <p>
	 * Nota: Os campos 'conversa' e 'autor' são ignorados pois o DTO fornece apenas
	 * os IDs. A camada de Serviço deve ser responsável por buscar as entidades
	 * 'Conversa' e 'Pessoa' e setá-las na entidade 'Mensagem' antes de salvar. Os
	 * campos 'id', 'dataEnvio' e 'isLida' também são ignorados pois são gerados
	 * pela Entity ou pelo banco de dados.
	 *
	 * @param request O DTO de requisição da Mensagem.
	 * @return A entidade Mensagem, parcialmente preenchida.
	 */
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "conversa", ignore = true) // Ignorado: O Service fará a busca de Conversa por ID
	@Mapping(target = "autor", ignore = true) // Ignorado: O Service fará a busca de Pessoa por ID
	@Mapping(target = "dataEnvio", ignore = true) // Ignorado: A Entidade já tem @CreationTimestamp ou valor padrão
	@Mapping(target = "isLida", ignore = true) // Ignorado: A Entidade já tem valor padrão
	Mensagem toEntity(MensagemRequest request);

	/**
	 * Converte a entidade Mensagem para o DTO de Resposta (MensagemResponse).
	 *
	 * @param entity A entidade Mensagem
	 * @return O DTO de resposta MensagemResponse.
	 */
	@Mapping(source = "id", target = "mensagemId")
	@Mapping(source = "conversa.id", target = "conversaId")
	@Mapping(source = "autor.id", target = "autorId")
	MensagemResponse toResponse(Mensagem entity);

	/**
	 * Converte uma lista de entidades Mensagem para uma lista de DTOs de Resposta.
	 *
	 * @param entities A lista de entidades Mensagem.
	 * @return A lista de MensagemResponse.
	 */
	List<MensagemResponse> toResponseList(List<Mensagem> entities);
}
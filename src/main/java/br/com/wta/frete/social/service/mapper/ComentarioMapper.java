package br.com.wta.frete.social.service.mapper;

import br.com.wta.frete.social.controller.dto.ComentarioRequest;
import br.com.wta.frete.social.controller.dto.ComentarioResponse;
import br.com.wta.frete.social.entity.Comentario;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

/**
 * Mapper para conversão entre a Entidade Comentario e os DTOs
 * (Request/Response). Utiliza a biblioteca MapStruct para geração automática de
 * código.
 */
@Mapper(
		// Define o tipo de componente para injeção de dependência (ex: Spring, CDI)
		componentModel = "spring",
		// Ignora propriedades alvo que não possuem um campo de origem correspondente.
		// Isso resolve o warning "Unmapped target property" se não for corrigido com
		// @Mapping.
		unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ComentarioMapper {

	/**
	 * Converte a Entidade Comentario para o DTO ComentarioResponse. * Mapeamentos
	 * explícitos: - 'id' da Entidade -> 'comentarioId' do DTO. - 'autor.id' da
	 * Entidade -> 'autorId' do DTO. - 'produto.id' da Entidade -> 'produtoId' do
	 * DTO. - 'comentarioPai.id' da Entidade -> 'comentarioPaiId' do DTO. -
	 * 'respostas' (List<Comentario>) é mapeada recursivamente para
	 * List<ComentarioResponse>. * @param comentario A entidade Comentario a ser
	 * convertida.
	 * 
	 * @return O DTO ComentarioResponse.
	 */
	@Mapping(source = "id", target = "comentarioId")
	// Assumindo que Comentario tem um campo 'autor' (tipo Pessoa) com um método
	// 'getId()'
	@Mapping(source = "autor.id", target = "autorId")
	// Assumindo que Comentario tem um campo 'produto' (tipo Produto) com um método
	// 'getId()'
	@Mapping(source = "produto.id", target = "produtoId")
	// Assumindo que Comentario tem um campo 'comentarioPai' (tipo Comentario) com
	// um método 'getId()'
	@Mapping(source = "comentarioPai.id", target = "comentarioPaiId")
	ComentarioResponse toResponse(Comentario comentario);

	/**
	 * Converte uma lista de Entidades Comentario para uma lista de DTOs
	 * ComentarioResponse. O MapStruct utiliza automaticamente o método 'toResponse'
	 * para cada item da lista. * @param comentarios A lista de entidades
	 * Comentario.
	 * 
	 * @return A lista de DTOs ComentarioResponse.
	 */
	List<ComentarioResponse> toResponseList(List<Comentario> comentarios);

	/**
	 * Converte o DTO ComentarioRequest para a Entidade Comentario. * Mapeamentos
	 * explícitos: - 'autorId' é ignorado, pois o autor geralmente é definido num
	 * serviço de forma segura. - 'produtoId' é ignorado, pelo mesmo motivo. -
	 * 'comentarioPaiId' é ignorado. * Campos ignorados na Entidade: - 'id': Será
	 * gerado pela base de dados. - 'dataComentario': Será gerada pelo sistema
	 * (ex: @CreationTimestamp). - 'respostas': Não é enviado no Request. * @param
	 * request O DTO ComentarioRequest a ser convertido.
	 * 
	 * @return A Entidade Comentario.
	 */
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "dataComentario", ignore = true)
	@Mapping(target = "respostas", ignore = true)
	// Se precisar de preencher 'autor', 'produto' ou 'comentarioPai' na Entidade:
	// @Mapping(target = "autor", ignore = true)
	// @Mapping(target = "produto", ignore = true)
	// @Mapping(target = "comentarioPai", ignore = true)
	Comentario toEntity(ComentarioRequest request);

}
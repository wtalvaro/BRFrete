package br.com.wta.frete.clientes.service.mapper;

import java.util.Arrays;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import br.com.wta.frete.clientes.controller.dto.DetalheClienteRequest;
import br.com.wta.frete.clientes.controller.dto.DetalheClienteResponse;
import br.com.wta.frete.clientes.entity.DetalheCliente;
import br.com.wta.frete.core.entity.Pessoa;
import br.com.wta.frete.core.entity.enums.TipoCliente;
import br.com.wta.frete.core.service.mapper.PessoaMapper;
import br.com.wta.frete.shared.exception.InvalidDataException;

/**
 * Interface Mapper para converter DetalheClienteRequest/Response e a entidade
 * DetalheCliente. O 'uses = { PessoaMapper.class }' permite que este Mapper
 * chame o PessoaMapper.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = { PessoaMapper.class })
public interface DetalheClienteMapper {

	// --- Mapeamento de Requisição (DTO -> Entidade) ---

	/**
	 * Converte o DetalheClienteRequest no DetalheCliente para persistência.
	 * 
	 * @param dto O DTO de entrada.
	 * @return A Entidade DetalheCliente.
	 */
	// 1. Mapeia o 'pessoaId' do DTO para a Entidade Pessoa (Chave Compartilhada)
	@Mapping(target = "pessoa", expression = "java(DetalheClienteMapper.mapPessoa(dto.pessoaId()))")

	// 2. Mapeia o String 'tipoCliente' para o Enum 'TipoCliente'
	@Mapping(target = "tipoCliente", source = "tipoCliente")

	// 3. O 'pessoaId' da entidade será setado automaticamente via @MapsId/Pessoa,
	// então ignoramos o target direto.
	@Mapping(target = "pessoaId", ignore = true)
	DetalheCliente toEntity(DetalheClienteRequest dto);

	// --- Mapeamento de Resposta (Entidade -> DTO) ---

	/**
	 * Converte a Entidade DetalheCliente no DetalheClienteResponse.
	 * 
	 * @param entity A Entidade DetalheCliente retornada do banco.
	 * @return O DTO de resposta.
	 */
	// 1. Mapeia o ID diretamente da Entidade
	@Mapping(target = "pessoaId", source = "pessoaId")

	// 2. Delega o mapeamento da Pessoa (Entidade) para o dadosPessoa (DTO composto)
	@Mapping(target = "dadosPessoa", source = "pessoa")

	// 3. Mapeia o Enum 'TipoCliente' para o String 'tipoCliente'
	@Mapping(target = "tipoCliente", source = "tipoCliente")
	DetalheClienteResponse toResponse(DetalheCliente entity);

	// --- Métodos Auxiliares/Manuais para o MapStruct ---

	/**
	 * Método estático auxiliar para MapStruct: Cria uma Entidade Pessoa de
	 * referência. Essencial para o JPA/Hibernate reconhecer a chave compartilhada
	 * (@MapsId).
	 */
	static Pessoa mapPessoa(Long pessoaId) {
		if (pessoaId == null) {
			return null;
		}
		Pessoa p = new Pessoa();
		p.setId(pessoaId);
		return p;
	}

	/**
	 * Conversor para MapStruct (String -> Enum TipoCliente). Trata o caso de erro,
	 * evitando falhas se a string não for válida.
	 */
	default TipoCliente map(String tipoCliente) {
		if (tipoCliente == null) {
			return null;
		}
		try {
			return TipoCliente.valueOf(tipoCliente.toUpperCase());
		} catch (IllegalArgumentException e) {
			// Lança a exceção de produção para interromper o processo
			throw new InvalidDataException("Tipo de cliente inválido: '%s'. Valores permitidos: %s".formatted(
                    tipoCliente, Arrays.toString(TipoCliente.values())), "TIPO_CLIENTE_INVALIDO");
		}
	}

	/**
	 * Conversor para MapStruct (Enum TipoCliente -> String).
	 */
	default String map(TipoCliente tipoCliente) {
		return tipoCliente != null ? tipoCliente.name() : null;
	}
}
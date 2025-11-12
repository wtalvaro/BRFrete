package br.com.wta.frete.clientes.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget; // Importação essencial para MapStruct Update
import org.mapstruct.ReportingPolicy;

import br.com.wta.frete.clientes.controller.dto.PedidoColetaRequest;
import br.com.wta.frete.clientes.controller.dto.PedidoColetaResponse;
import br.com.wta.frete.clientes.entity.DetalheCliente;
import br.com.wta.frete.clientes.entity.PedidoColeta;

/**
 * Interface Mapper para converter PedidoColetaRequest/Response e a entidade
 * PedidoColeta. Não depende do PessoaMapper, mas utiliza lógica estática para a
 * FK de DetalheCliente.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PedidoColetaMapper {

	// --- Mapeamento de Requisição (DTO -> Entidade) ---

	/**
	 * Converte o PedidoColetaRequest no PedidoColeta para persistência (CREATE).
	 * * @param dto O DTO de entrada.
	 * 
	 * @return A Entidade PedidoColeta.
	 */
	@Mapping(target = "id", ignore = true) // ID é SERIAL/IDENTITY, ignoramos na Request
	@Mapping(target = "dataSolicitacao", ignore = true) // Setado por DEFAULT no DB ou no @PrePersist

	// Mapeia o ID do Cliente para a Entidade DetalheCliente de referência
	@Mapping(target = "cliente", expression = "java(PedidoColetaMapper.mapDetalheCliente(dto.clientePessoaId()))")
	PedidoColeta toEntity(PedidoColetaRequest dto);

	// --- Mapeamento de Atualização (DTO -> Entidade Existente) ---

	/**
	 * Atualiza uma Entidade PedidoColeta existente com os dados do Request
	 * (UPDATE).
	 * * @param dto O DTO de entrada.
	 * 
	 * @param entity A entidade PedidoColeta existente a ser atualizada.
	 */
	// Campos que não devem ser alterados pelo DTO:
	@Mapping(target = "id", ignore = true) // Chave Primária não muda
	@Mapping(target = "dataSolicitacao", ignore = true) // Data de criação/solicitação original não muda

	// A FK do cliente não deve ser alterada no UPDATE via este DTO, então
	// ignoramos.
	@Mapping(target = "cliente", ignore = true)
	void updateEntity(PedidoColetaRequest dto, @MappingTarget PedidoColeta entity);

	// --- Mapeamento de Resposta (Entidade -> DTO) ---

	/**
	 * Converte a Entidade PedidoColeta no PedidoColetaResponse.
	 * * @param entity A Entidade PedidoColeta retornada do banco.
	 * 
	 * @return O DTO de resposta.
	 */
	@Mapping(target = "pedidoId", source = "id") // Renomeia 'id' para 'pedidoId'

	// O ID do Cliente está diretamente em DetalheCliente (pessoaId)
	@Mapping(target = "clientePessoaId", source = "cliente.pessoaId")
	PedidoColetaResponse toResponse(PedidoColeta entity);

	// --- Método Auxiliar/Estático ---

	/**
	 * Método estático auxiliar para MapStruct: Cria uma Entidade DetalheCliente de
	 * referência. Necessário para setar a Chave Estrangeira (FK) na Entidade
	 * PedidoColeta.
	 */
	static DetalheCliente mapDetalheCliente(Long clientePessoaId) {
		if (clientePessoaId == null) {
			return null;
		}

		// Criamos a Entidade DetalheCliente de referência
		DetalheCliente dc = new DetalheCliente();
		// Apenas setamos o ID da Pessoa, que é a Chave Primária/Estrangeira
		dc.setPessoaId(clientePessoaId);

		return dc;
	}
}
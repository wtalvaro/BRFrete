package br.com.wta.frete.clientes.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import br.com.wta.frete.clientes.controller.dto.PedidoColetaRequest;
import br.com.wta.frete.clientes.controller.dto.PedidoColetaResponse;
import br.com.wta.frete.clientes.entity.DetalheCliente;
import br.com.wta.frete.clientes.entity.PedidoColeta;
import br.com.wta.frete.core.entity.Pessoa;

/**
 * Interface Mapper para converter PedidoColetaRequest/Response e a entidade
 * PedidoColeta. Não depende do PessoaMapper, mas utiliza lógica estática para a
 * FK de DetalheCliente.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PedidoColetaMapper {

	// --- Mapeamento de Requisição (DTO -> Entidade) ---

	/**
	 * Converte o PedidoColetaRequest no PedidoColeta para persistência.
	 * 
	 * @param dto O DTO de entrada.
	 * @return A Entidade PedidoColeta.
	 */
	@Mapping(target = "id", ignore = true) // ID é SERIAL/IDENTITY, ignoramos na Request
	@Mapping(target = "dataSolicitacao", ignore = true) // Setado por DEFAULT no DB ou no @PrePersist

	// Mapeia o ID do Cliente para a Entidade DetalheCliente de referência
	@Mapping(target = "cliente", expression = "java(PedidoColetaMapper.mapDetalheCliente(dto.clientePessoaId()))")
	PedidoColeta toEntity(PedidoColetaRequest dto);

	// --- Mapeamento de Resposta (Entidade -> DTO) ---

	/**
	 * Converte a Entidade PedidoColeta no PedidoColetaResponse.
	 * 
	 * @param entity A Entidade PedidoColeta retornada do banco.
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

		// Criamos o esqueleto da Pessoa para satisfazer a Entidade DetalheCliente
		Pessoa pessoa = new Pessoa();
		pessoa.setId(clientePessoaId);

		// Criamos a Entidade DetalheCliente e setamos a chave
		DetalheCliente dc = new DetalheCliente();
		dc.setPessoaId(clientePessoaId);
		dc.setPessoa(pessoa);

		return dc;
	}
}
package br.com.wta.frete.logistica.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import br.com.wta.frete.colaboradores.entity.Transportador;
import br.com.wta.frete.logistica.controller.dto.LanceRequest;
import br.com.wta.frete.logistica.controller.dto.LanceResponse;
import br.com.wta.frete.logistica.entity.Frete;
import br.com.wta.frete.logistica.entity.Lance;

/**
 * Mapper para a entidade Lance, responsável por converter entre
 * LanceRequest/Response e Lance (Entidade). Utiliza o MapStruct para
 * simplificar as conversões de DTO para Entity.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LanceMapper {

	// Opcional: Para uso fora do contexto Spring (embora componentModel="spring"
	// seja recomendado)
	LanceMapper INSTANCE = Mappers.getMapper(LanceMapper.class);

	// --- Mapeamento de Request para Entidade (Criação de Lance) ---

	/**
	 * Converte um LanceRequest para a entidade Lance. - Mapeia os IDs de FKs para
	 * as entidades correspondentes (Frete e Transportador) utilizando os métodos
	 * default toFrete e toTransportador. - Ignora 'id' pois é gerado pelo BD.
	 */
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "frete", source = "ordemServicoId")
	@Mapping(target = "transportador", source = "transportadorPessoaId")
	@Mapping(target = "dataLance", ignore = true) // Deixa a entidade setar o default: ZonedDateTime.now()
	@Mapping(target = "vencedor", ignore = true) // Deixa a entidade setar o default: false
	Lance toEntity(LanceRequest request);

	// --- Mapeamento de Entidade para Response (Leitura de Lance) ---

	/**
	 * Converte a entidade Lance para o DTO de Resposta LanceResponse. - Mapeia o PK
	 * da entidade Lance (id) para lanceId (do DTO). - Mapeia a PK da entidade Frete
	 * (frete.ordemServicoId) para ordemServicoId (do DTO). - Mapeia a PK da
	 * entidade Transportador (transportador.pessoaId) para transportadorPessoaId
	 * (do DTO).
	 */
	@Mapping(target = "lanceId", source = "id")
	@Mapping(target = "ordemServicoId", source = "frete.ordemServicoId")
	@Mapping(target = "transportadorPessoaId", source = "transportador.pessoaId") // CORREÇÃO: Mapeado para o PK
																					// 'pessoaId'
	LanceResponse toResponse(Lance entity);

	// --- Métodos Auxiliares para Injeção de Entidades por ID ---

	/**
	 * Método auxiliar para o MapStruct criar a entidade Frete a partir do ID
	 * (ordemServicoId). Essencial para criar a referência de FK sem buscar no banco
	 * de dados.
	 * 
	 * @param ordemServicoId O ID da Ordem de Serviço (Frete).
	 * @return Uma nova entidade Frete com apenas o ID preenchido.
	 */
	default Frete toFrete(Long ordemServicoId) {
		if (ordemServicoId == null) {
			return null;
		}
		Frete frete = new Frete();
		// A chave primária é 'ordemServicoId'
		frete.setOrdemServicoId(ordemServicoId);
		return frete;
	}

	/**
	 * Método auxiliar para o MapStruct criar a entidade Transportador a partir do
	 * ID (transportadorPessoaId). Essencial para criar a referência de FK sem
	 * buscar no banco de dados.
	 * 
	 * @param transportadorPessoaId O ID do Transportador.
	 * @return Uma nova entidade Transportador com apenas o ID preenchido.
	 */
	default Transportador toTransportador(Long transportadorPessoaId) {
		if (transportadorPessoaId == null) {
			return null;
		}
		Transportador transportador = new Transportador();
		// CORREÇÃO: A chave primária é 'pessoaId' e o setter é 'setPessoaId'
		transportador.setPessoaId(transportadorPessoaId);
		return transportador;
	}
}
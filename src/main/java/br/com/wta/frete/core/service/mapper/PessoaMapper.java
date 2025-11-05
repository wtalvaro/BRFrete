package br.com.wta.frete.core.service.mapper;

// 💡 Importação Adicionada para resolver o erro "UUID cannot be resolved"
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import br.com.wta.frete.core.controller.dto.CadastroSimplificadoRequest;
import br.com.wta.frete.core.controller.dto.PessoaRequest;
import br.com.wta.frete.core.controller.dto.PessoaResponse;
import br.com.wta.frete.core.entity.Pessoa;

/**
 * Interface de Mapeamento (Mapper) usando MapStruct.
 * Responsável por converter DTOs para Entidades e
 * vice-versa.java(UUID.randomUUID().toString().substring(0, 18))
 * * @implNote uses = {}, componentModel = "spring" (componentModel="spring"
 * injeta o Mapper como um Bean Spring)
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PessoaMapper {

	/**
	 * Mapeia de PessoaRequest (Dados de Entrada) para a Entidade Pessoa.
	 * * @param request O DTO de requisição.
	 * * @return A Entidade Pessoa (com senha ainda não criptografada, ativo=false,
	 * etc.)
	 * * @Mapping target="nome" source="nomeCompleto": Garante que
	 * 'nomeCompleto' no DTO mapeia para 'nome' na Entidade.
	 * 
	 * @Mapping target="id": O ID é gerado pelo banco, então não mapeamos do DTO.
	 */
	@Mapping(target = "nome", source = "nomeCompleto")
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "ativo", ignore = true) // O status 'ativo' é definido no Service como 'false'.
	@Mapping(target = "cliente", constant = "true") // NOVO: Define isCliente=true no momento do mapeamento.
	Pessoa toEntity(PessoaRequest request);

	// NOVO MÉTODO: Mapeamento do Cadastro Simplificado
	/**
	 * Mapeia de CadastroSimplificadoRequest (apenas e-mail/senha) para a Entidade
	 * Pessoa.
	 * * @param request O DTO de requisição simplificado.
	 * * @return A Entidade Pessoa (com placeholders para nome/documento).
	 */
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "ativo", ignore = true)
	// Placeholder 1: Usa o próprio e-mail como nome (poderá ser atualizado depois)
	@Mapping(target = "nome", source = "email")
	// Placeholder 2: Usa um UUID como documento para garantir unicidade.
	@Mapping(target = "documento", expression = "java(this.generateDocumentPlaceholder())")
	@Mapping(target = "cliente", constant = "true") // NOVO: Define isCliente=true no momento do mapeamento.
	// Outros campos devem ser ignorados ou terão valor padrão (ex: telefone=null)
	Pessoa toEntity(CadastroSimplificadoRequest request);

	/**
	 * Mapeia da Entidade Pessoa (Dados do BD) para o PessoaResponse (Dados de
	 * Saída).
	 * * @param pessoa A Entidade Pessoa salva no banco.
	 * * @return O DTO de resposta seguro.
	 * * @Mapping target="nomeCompleto" source="nome": Garante que 'nome' na
	 * Entidade mapeia para 'nomeCompleto' no DTO.
	 */
	@Mapping(target = "nomeCompleto", source = "nome")
	PessoaResponse toResponse(Pessoa pessoa);

	// Método auxiliar para conversão reversa, se necessário
	// Pessoa toEntity(PessoaResponse response);

	// =========================================================================
	// 🛠️ NOVO MÉTODO: Lógica de Geração de Placeholder para o campo 'documento'
	// =========================================================================

	/**
	 * Método default para gerar um placeholder único (UUID) para o campo documento.
	 * * @implNote Este método é um método Java normal, o que garante que o 'import
	 * java.util.UUID'
	 * seja reconhecido e utilizado pela IDE, eliminando o aviso de import não
	 * usado.
	 * * @return Os 18 primeiros caracteres de um novo UUID.
	 */
	default String generateDocumentPlaceholder() {
		// O UUID agora é usado DENTRO do corpo do método, o que resolve o aviso da IDE.
		return UUID.randomUUID().toString().substring(0, 18);
	}
}
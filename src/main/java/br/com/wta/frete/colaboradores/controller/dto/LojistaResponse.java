package br.com.wta.frete.colaboradores.controller.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import br.com.wta.frete.core.controller.dto.PessoaResponse;
import lombok.AllArgsConstructor;

/**
 * DTO para retornar detalhes do perfil do Lojista.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LojistaResponse {

	private Long pessoaId;

	private String nomeLoja;
	private String enderecoPrincipal;

	private PessoaResponse dadosPessoa;
}
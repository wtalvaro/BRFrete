package br.com.wta.frete.inventario.controller.dto;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

import br.com.wta.frete.inventario.entity.enums.TipoMaterialEnum;

/**
 * DTO de Resposta para a entidade EstoqueSucata (inventario.estoque).
 */
public record EstoqueSucataResponse(
		Long id,
		Long sucateiroPessoaId,
		TipoMaterialEnum tipoMaterial, // ALTERADO: Usa o novo Enum
		String tipoMaterialDescricao, // Adicionado para facilitar a visualização
		BigDecimal quantidadePesoKg,
		String statusQualidade,
		String localizacao,
		ZonedDateTime dataAtualizacao) {
}
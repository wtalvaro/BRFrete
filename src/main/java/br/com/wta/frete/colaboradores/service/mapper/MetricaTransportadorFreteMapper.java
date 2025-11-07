package br.com.wta.frete.colaboradores.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import br.com.wta.frete.colaboradores.controller.dto.MetricaTransportadorFreteRequest;
import br.com.wta.frete.colaboradores.controller.dto.MetricaTransportadorFreteResponse;
import br.com.wta.frete.colaboradores.entity.MetricaTransportadorFrete;
import br.com.wta.frete.logistica.service.mapper.ModalidadeFreteMapper;

/**
 * Mapper para conversão entre a Entidade MetricaTransportadorFrete e seus DTOs.
 * Usa o ModalidadeFreteMapper para mapeamento de entidades relacionadas.
 */
@Mapper(componentModel = "spring", uses = { ModalidadeFreteMapper.class })
public interface MetricaTransportadorFreteMapper {

    /**
     * Converte Request DTO para Entidade.
     * Ignora o Transportador, pois será associado pelo Service.
     */
    @Mapping(target = "transportador", ignore = true)
    @Mapping(target = "modalidadeFrete", ignore = true) // Será associada pelo Service
    @Mapping(target = "metricaId", ignore = true)
    @Mapping(target = "versao", ignore = true)
    MetricaTransportadorFrete toEntity(MetricaTransportadorFreteRequest dto);

    /**
     * Converte Entidade para Response DTO.
     * Mapeia o ID do Transportador.
     */
    @Mapping(source = "transportador.pessoaId", target = "transportadorPessoaId")
    MetricaTransportadorFreteResponse toResponse(MetricaTransportadorFrete entity);

    /**
     * Atualiza a Entidade com dados do Request DTO.
     * Mantém a Entidade gerenciada, ignora o Transportador e o ID da Métrica.
     */
    @Mapping(target = "transportador", ignore = true)
    @Mapping(target = "modalidadeFrete", ignore = true) // Será atualizada pelo Service
    @Mapping(target = "metricaId", ignore = true)
    @Mapping(target = "versao", ignore = true)
    void updateEntityFromRequest(MetricaTransportadorFreteRequest request,
            @MappingTarget MetricaTransportadorFrete target);
}
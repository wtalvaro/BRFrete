package br.com.wta.frete.colaboradores.service.mapper;

import br.com.wta.frete.colaboradores.controller.dto.HorarioOperacaoRequest;
import br.com.wta.frete.colaboradores.controller.dto.HorarioOperacaoResponse;
import br.com.wta.frete.colaboradores.entity.HorarioOperacao;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

/**
 * Interface Mapper para converter entre a Entidade HorarioOperacao e seus DTOs.
 */
@Mapper(componentModel = "spring")
public interface HorarioOperacaoMapper {

    HorarioOperacaoMapper INSTANCE = Mappers.getMapper(HorarioOperacaoMapper.class);

    /**
     * Documentação: Converte o DTO de Requisição para a Entidade.
     * Deve ignorar os campos que serão preenchidos pelo Service (id) ou
     * pelo relacionamento (pessoa).
     */
    @Mapping(target = "horarioId", ignore = true)
    @Mapping(target = "pessoa", ignore = true) // A entidade Pessoa será associada no Service
    HorarioOperacao toEntity(HorarioOperacaoRequest dto);

    /**
     * Documentação: Converte a Entidade para o DTO de Resposta.
     * Mapeia o ID da Pessoa aninhada diretamente para o campo pessoaId do DTO.
     */
    @Mapping(source = "pessoa.id", target = "pessoaId")
    HorarioOperacaoResponse toResponse(HorarioOperacao entity);

    /**
     * Documentação: Atualiza uma Entidade existente com base no DTO de Requisição.
     * Ignora a PK e o relacionamento.
     */
    @Mapping(target = "horarioId", ignore = true)
    @Mapping(target = "pessoa", ignore = true)
    void updateEntityFromRequest(HorarioOperacaoRequest request, @MappingTarget HorarioOperacao target);
}
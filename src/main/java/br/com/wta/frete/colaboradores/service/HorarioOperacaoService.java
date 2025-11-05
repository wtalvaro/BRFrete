package br.com.wta.frete.colaboradores.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.wta.frete.colaboradores.controller.dto.HorarioOperacaoRequest;
import br.com.wta.frete.colaboradores.controller.dto.HorarioOperacaoResponse;
import br.com.wta.frete.colaboradores.entity.HorarioOperacao;
import br.com.wta.frete.colaboradores.repository.HorarioOperacaoRepository;
import br.com.wta.frete.colaboradores.service.mapper.HorarioOperacaoMapper;
import br.com.wta.frete.core.entity.Pessoa;
import br.com.wta.frete.core.repository.PessoaRepository;
import br.com.wta.frete.shared.exception.InvalidDataException;
import br.com.wta.frete.shared.exception.ResourceNotFoundException;

/**
 * Service dedicado à gestão de HorariosOperacao para colaboradores.
 * Lida com validação e persistência de horários de funcionamento.
 */
@Service
public class HorarioOperacaoService {

    private final HorarioOperacaoRepository horarioOperacaoRepository;
    private final HorarioOperacaoMapper horarioOperacaoMapper;
    private final PessoaRepository pessoaRepository;

    public HorarioOperacaoService(HorarioOperacaoRepository horarioOperacaoRepository,
            HorarioOperacaoMapper horarioOperacaoMapper, PessoaRepository pessoaRepository) {
        this.horarioOperacaoRepository = horarioOperacaoRepository;
        this.horarioOperacaoMapper = horarioOperacaoMapper;
        this.pessoaRepository = pessoaRepository;
    }

    /**
     * Documentação: Cadastra um novo horário de operação para uma
     * Pessoa/Colaborador.
     * * @param request O DTO de requisição.
     * 
     * @return O DTO de resposta com o horário salvo.
     */
    @SuppressWarnings("null")
    @Transactional
    public HorarioOperacaoResponse cadastrarHorario(HorarioOperacaoRequest request) {
        // 1. Validação de Regra de Negócio: Abertura deve ser anterior ao Fechamento
        if (!request.horaAbertura().isBefore(request.horaFechamento())) {
            throw new InvalidDataException("A hora de abertura deve ser anterior à hora de fechamento.", "HO_001");
        }

        // 2. Validação de Pessoa: Garante que a Pessoa exista antes de associar o
        // horário
        Pessoa pessoa = pessoaRepository.findById(request.pessoaId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Pessoa com ID " + request.pessoaId() + " não encontrada."));

        // 3. Mapeamento e Associação
        HorarioOperacao novoHorario = horarioOperacaoMapper.toEntity(request);
        novoHorario.setPessoa(pessoa); // Associa a entidade Pessoa encontrada

        // 4. Persistência
        novoHorario = horarioOperacaoRepository.save(novoHorario);

        // 5. Retorna
        return horarioOperacaoMapper.toResponse(novoHorario);
    }
}
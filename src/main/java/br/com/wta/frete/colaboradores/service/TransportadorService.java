package br.com.wta.frete.colaboradores.service;

import br.com.wta.frete.colaboradores.controller.dto.TransportadorResponse;
import br.com.wta.frete.colaboradores.entity.Transportador;
import br.com.wta.frete.colaboradores.repository.TransportadorRepository;
import br.com.wta.frete.colaboradores.service.mapper.TransportadorMapper;
import br.com.wta.frete.core.entity.Pessoa;
import br.com.wta.frete.core.entity.Perfil;
import br.com.wta.frete.core.entity.PessoaPerfil;
import br.com.wta.frete.core.entity.PessoaPerfilId;
import br.com.wta.frete.core.repository.PessoaRepository;
import br.com.wta.frete.core.repository.PerfilRepository;
import br.com.wta.frete.core.repository.PessoaPerfilRepository;
import br.com.wta.frete.shared.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class TransportadorService {

    private final TransportadorRepository transportadorRepository;
    private final TransportadorMapper transportadorMapper;
    private final PessoaRepository pessoaRepository;
    private final PerfilRepository perfilRepository;
    private final PessoaPerfilRepository pessoaPerfilRepository;

    public TransportadorService(TransportadorRepository transportadorRepository,
            TransportadorMapper transportadorMapper,
            PessoaRepository pessoaRepository,
            PerfilRepository perfilRepository,
            PessoaPerfilRepository pessoaPerfilRepository) {
        this.transportadorRepository = transportadorRepository;
        this.transportadorMapper = transportadorMapper;
        this.pessoaRepository = pessoaRepository;
        this.perfilRepository = perfilRepository;
        this.pessoaPerfilRepository = pessoaPerfilRepository;
    }

    /**
     * Documentação: Método para ADICIONAR o perfil de Transportador a um usuário
     * Pessoa existente.
     * O fluxo garante que a Pessoa exista e, caso o registro de Transportador
     * (entidade) não exista, ele será criado.
     * 
     * @param pessoaId O ID da pessoa existente que receberá o perfil.
     * 
     * @return TransportadorResponse com os dados do novo transportador.
     * @throws ResourceNotFoundException se a Pessoa não for encontrada.
     * @throws IllegalStateException     se o Perfil 'TRANSPORTADOR' não existir.
     */
    @Transactional
    public TransportadorResponse adicionarPerfilTransportador(Long pessoaId) {
        // 1. Busca e Validação da Pessoa
        Pessoa pessoa = pessoaRepository.findById(pessoaId)
                .orElseThrow(() -> new ResourceNotFoundException("Pessoa com ID " + pessoaId + " não encontrada."));

        // 2. Busca o Perfil 'TRANSPORTADOR'
        Perfil perfilTransportador = perfilRepository.findByNomePerfil("TRANSPORTADOR")
                .orElseThrow(() -> new IllegalStateException(
                        "Perfil 'TRANSPORTADOR' não encontrado. Verifique a inicialização de dados."));

        // 3. Verifica se o registro de Transportador JÁ EXISTE e cria se necessário.
        Optional<Transportador> transportadorExistente = transportadorRepository.findByPessoaId(pessoaId);
        Transportador transportador = transportadorExistente.orElseGet(() -> {
            Transportador novoTransportador = new Transportador();
            novoTransportador.setPessoa(pessoa);
            // Defina outros campos iniciais, se houver
            return transportadorRepository.save(novoTransportador);
        });

        // Se já existe, apenas prossegue para adicionar o perfil.

        // 4. Adiciona/Atualiza o Perfil de Transportador à Pessoa
        // Usamos PessoaPerfilId para garantir a unicidade da associação
        PessoaPerfilId pessoaPerfilId = new PessoaPerfilId(pessoaId, perfilTransportador.getId());

        // Verifica se a associação já existe antes de tentar criá-la (Idempotência)
        if (pessoaPerfilRepository.findById(pessoaPerfilId).isEmpty()) {
            pessoaPerfilRepository.save(new PessoaPerfil(pessoa, perfilTransportador));
        }

        // 5. Atualiza o flag isColaborador na Pessoa (se já não for colaborador)
        if (!pessoa.isColaborador()) {
            pessoa.setColaborador(true);
            pessoaRepository.save(pessoa);
        }

        // 6. Mapeia e retorna a resposta
        return transportadorMapper.toResponse(transportador);
    }
}
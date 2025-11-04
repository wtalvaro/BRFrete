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
import br.com.wta.frete.shared.exception.InvalidDataException;
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
     * Documentação: Método para converter um usuário (Lead/Pessoa) em um
     * Transportador.
     * O fluxo garante que a Pessoa exista, não seja um Transportador, cria a
     * entidade Transportador,
     * associa o perfil 'TRANSPORTADOR' e marca a Pessoa como colaboradora.
     *
     * @param pessoaId O ID da pessoa existente (Lead) no sistema.
     * @return TransportadorResponse com os dados do novo transportador.
     * @throws InvalidDataException se a Pessoa não for encontrada, já for um
     *                              Transportador ou o Perfil não existir.
     */
    @SuppressWarnings("null")
    @Transactional
    public TransportadorResponse converterLeadEmTransportador(Long pessoaId) {
        // 1. Busca e Validação da Pessoa
        Pessoa pessoa = pessoaRepository.findById(pessoaId)
                .orElseThrow(() -> new InvalidDataException("Pessoa com ID " + pessoaId + " não encontrada.",
                        "PERSON_NOT_FOUND"));

        // 2. Valida se a Pessoa já é um Transportador
        Optional<Transportador> transportadorExistente = transportadorRepository.findByPessoaId(pessoaId);
        if (transportadorExistente.isPresent()) {
            throw new InvalidDataException("A Pessoa com ID " + pessoaId + " já está cadastrada como Transportador.",
                    "ALREADY_TRANSPORTADOR");
        }

        // 3. Busca o Perfil 'TRANSPORTADOR'
        // Agora usa Optional<Perfil> do repositório corrigido (Passo 1B)
        Perfil perfilTransportador = perfilRepository.findByNomePerfil("TRANSPORTADOR")
                .orElseThrow(() -> new InvalidDataException(
                        "Perfil 'TRANSPORTADOR' não encontrado no sistema. Verifique a tabela 'core.perfis'.",
                        "PROFILE_NOT_FOUND"));

        // 4. Cria a nova entidade Transportador
        Transportador novoTransportador = new Transportador();
        novoTransportador.setPessoa(pessoa);
        // dataCadastro foi removido, pois a entidade Transportador.java não o possui.

        // 5. Salva o Transportador
        novoTransportador = transportadorRepository.save(novoTransportador);

        // 6. Adiciona o Perfil de Transportador à Pessoa
        PessoaPerfilId pessoaPerfilId = new PessoaPerfilId(pessoaId, perfilTransportador.getId());

        PessoaPerfil pessoaPerfil = new PessoaPerfil();
        pessoaPerfil.setId(pessoaPerfilId);
        pessoaPerfil.setPessoa(pessoa);
        pessoaPerfil.setPerfil(perfilTransportador);

        pessoaPerfilRepository.save(pessoaPerfil);

        // 7. Atualiza o flag isColaborador na Pessoa
        pessoa.setColaborador(true); // Correção do setter Lombok
        pessoaRepository.save(pessoa);

        // 8. Mapeia e retorna a resposta
        return transportadorMapper.toResponse(novoTransportador);
    }
}
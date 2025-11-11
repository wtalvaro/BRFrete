package br.com.wta.frete.colaboradores.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.wta.frete.colaboradores.controller.dto.TransportadorResponse;
import br.com.wta.frete.colaboradores.entity.Transportador;
import br.com.wta.frete.colaboradores.repository.TransportadorRepository;
import br.com.wta.frete.colaboradores.service.mapper.TransportadorMapper;
import br.com.wta.frete.core.entity.Pessoa;

@Service
public class TransportadorService {

    private final TransportadorRepository transportadorRepository;
    private final TransportadorMapper transportadorMapper;
    private final PerfilAssociacaoService perfilServiceComum; // NOVO: Injeção do Service Comum

    // Construtor ATUALIZADO
    public TransportadorService(TransportadorRepository transportadorRepository,
            TransportadorMapper transportadorMapper,
            PerfilAssociacaoService perfilServiceComum) { // Apenas dependências essenciais
        this.transportadorRepository = transportadorRepository;
        this.transportadorMapper = transportadorMapper;
        this.perfilServiceComum = perfilServiceComum;
    }

    /**
     * Método para ADICIONAR o perfil de Transportador a um usuário Pessoa
     * existente.
     * Usa o Service Comum para gerenciar a associação de perfil e o flag
     * isColaborador.
     * * @param pessoaId O ID da pessoa existente que receberá o perfil.
     * * @return TransportadorResponse com os dados do novo transportador.
     */
    @Transactional
    public TransportadorResponse adicionarPerfilTransportador(Long pessoaId) {

        // 1. UTILIZA O SERVIÇO COMUM (Substitui os passos 1, 2, 4 e 5 originais)
        // Valida Pessoa, busca Perfil 'TRANSPORTADOR' e faz a associação/atualização do
        // isColaborador.
        Pessoa pessoa = perfilServiceComum.associarPerfilColaborador(pessoaId, "TRANSPORTADOR");

        // 2. Verifica se o registro de Transportador JÁ EXISTE e cria se necessário.
        // (Lógica Específica)
        Optional<Transportador> transportadorExistente = transportadorRepository.findByPessoaId(pessoaId);
        Transportador transportador = transportadorExistente.orElseGet(() -> {
            Transportador novoTransportador = new Transportador();
            novoTransportador.setPessoa(pessoa);
            // Defina outros campos iniciais, se houver
            return transportadorRepository.save(novoTransportador);
        });

        // 3. Mapeia e retorna a resposta
        return transportadorMapper.toResponse(transportador);
    }
}
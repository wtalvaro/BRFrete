package br.com.wta.frete.colaboradores.service;

import br.com.wta.frete.colaboradores.entity.Transportador;
import br.com.wta.frete.colaboradores.repository.TransportadorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransportadorService {

    private final TransportadorRepository transportadorRepository;

    public TransportadorService(TransportadorRepository transportadorRepository) {
        this.transportadorRepository = transportadorRepository;
    }

    /**
     * Cria o registro específico na tabela 'colaboradores.transportadores'. O ID da
     * Pessoa deve existir em core.pessoas antes desta chamada. * @param pessoaId O
     * ID da pessoa (chave primária e estrangeira).
     * 
     * @param licenca O número da licença de transporte.
     * @return A entidade Transportador criada.
     */
    @SuppressWarnings("null")
    @Transactional
    public Transportador criarRegistro(Long pessoaId, String licenca) {

        // 1. Verificação de Unicidade (Opcional, mas seguro):
        // Garante que a pessoa ainda não é um transportador.
        if (transportadorRepository.existsById(pessoaId)) {
            // Lançar exceção ou tratar o caso de re-registro.
            throw new IllegalArgumentException("O ID da Pessoa já está registrado como Transportador.");
        }

        // 2. Criação da Entidade
        Transportador transportador = new Transportador();

        // Mapeia o ID: Esta é a chave primária e a FK para core.pessoas.
        transportador.setPessoaId(pessoaId);

        // Registra o campo específico do colaborador
        transportador.setLicencaTransporte(licenca); // Mapeia para licenca_transporte

        // 3. Persistência
        return transportadorRepository.save(transportador);
    }
}
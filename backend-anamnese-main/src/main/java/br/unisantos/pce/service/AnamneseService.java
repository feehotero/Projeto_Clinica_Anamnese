package br.unisantos.pce.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.unisantos.pce.model.*;
import br.unisantos.pce.repository.*;
import br.unisantos.pce.user.User;

@Service
public class AnamneseService {

    private final AnamneseRepository anamneseRepository;
    private final PacienteRepository pacienteRepository;
    private final UserRepository userRepository;

    // Novos repositórios necessários
    @Autowired(required = false)
    private EscolaridadeRepository escolaridadeRepository;

    @Autowired(required = false)
    private ProfissaoRepository profissaoRepository;

    @Autowired(required = false)
    private RendaFamiliarRepository rendaFamiliarRepository;

    @Autowired(required = false)
    private EvacuacaoRepository evacuacaoRepository;

    @Autowired(required = false)
    private RefeicaoRepository refeicaoRepository;

    @Autowired(required = false)
    private AlimentoRepository alimentoRepository;

    @Autowired
    AnamneseService(
            AnamneseRepository anamneseRepository,
            PacienteRepository pacienteRepository,
            UserRepository userRepository) {
        this.anamneseRepository = anamneseRepository;
        this.pacienteRepository = pacienteRepository;
        this.userRepository = userRepository;
    }

    public List<Anamnese> listarAnamneses() {
        return anamneseRepository.findAllByOrderByCriadoEmDesc();
    }

    public List<Anamnese> listarAnamnesesByPacienteId(Integer pacienteId) {
        return anamneseRepository.findAllByPacienteId(pacienteId);
    }

    public List<Anamnese> listarAnamnesesByPacienteNome(String pacienteNome) {
        return anamneseRepository.findByPacienteNome(pacienteNome.toUpperCase());
    }

    public List<Anamnese> listarAnamnesesByUsuarioId(Integer usuarioId) {
        return anamneseRepository.findAllByUsuarioId(usuarioId);
    }

    public Optional<Anamnese> consultarAnamnese(Integer id) {
        return anamneseRepository.findById(id);
    }

    @Transactional
    public Anamnese criarAnamnese(Anamnese anamnese) {
        // 1. Buscar Paciente gerenciado
        if (anamnese.getPaciente() != null && anamnese.getPaciente().getId() != null) {
            Paciente paciente = pacienteRepository.findById(anamnese.getPaciente().getId())
                    .orElseThrow(() -> new RuntimeException("Paciente não encontrado"));
            anamnese.setPaciente(paciente);
        }

        // 2. Buscar Usuario gerenciado
        if (anamnese.getUsuario() != null && anamnese.getUsuario().getId() != null) {
            User usuario = userRepository.findById(anamnese.getUsuario().getId())
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
            anamnese.setUsuario(usuario);
        }

        // 3. Buscar entidades de domínio gerenciadas
        if (escolaridadeRepository != null && anamnese.getEscolaridade() != null
                && anamnese.getEscolaridade().getId() != null) {
            anamnese.setEscolaridade(escolaridadeRepository.findById(anamnese.getEscolaridade().getId()).orElse(null));
        }

        if (profissaoRepository != null && anamnese.getProfissao() != null && anamnese.getProfissao().getId() != null) {
            anamnese.setProfissao(profissaoRepository.findById(anamnese.getProfissao().getId()).orElse(null));
        }

        if (rendaFamiliarRepository != null && anamnese.getRendaFamiliar() != null
                && anamnese.getRendaFamiliar().getId() != null) {
            anamnese.setRendaFamiliar(
                    rendaFamiliarRepository.findById(anamnese.getRendaFamiliar().getId()).orElse(null));
        }

        if (evacuacaoRepository != null && anamnese.getEvacuacao() != null && anamnese.getEvacuacao().getId() != null) {
            anamnese.setEvacuacao(evacuacaoRepository.findById(anamnese.getEvacuacao().getId()).orElse(null));
        }

        // 4. Processar Refeições
        if (refeicaoRepository != null && anamnese.getRefeicoes() != null && !anamnese.getRefeicoes().isEmpty()) {
            List<Refeicao> refeicoesGerenciadas = new ArrayList<>();
            for (Refeicao r : anamnese.getRefeicoes()) {
                if (r.getId() != null) {
                    refeicaoRepository.findById(r.getId()).ifPresent(refeicoesGerenciadas::add);
                }
            }
            anamnese.setRefeicoes(refeicoesGerenciadas);
        }

        // 5. Processar Alimentos
        if (alimentoRepository != null && anamnese.getAlimentos() != null && !anamnese.getAlimentos().isEmpty()) {
            List<AnamneseAlimento> alimentosGerenciados = new ArrayList<>();
            for (AnamneseAlimento aa : anamnese.getAlimentos()) {
                if (aa.getAlimento() != null && aa.getAlimento().getId() != null) {
                    Optional<Alimento> alimentoOpt = alimentoRepository.findById(aa.getAlimento().getId());
                    if (alimentoOpt.isPresent()) {
                        aa.setAlimento(alimentoOpt.get());
                        aa.setAnamnese(anamnese);

                        // Criar chave composta
                        AnamneseAlimentoKey key = new AnamneseAlimentoKey();
                        key.setAlimentoId(aa.getAlimento().getId());
                        aa.setId(key);

                        alimentosGerenciados.add(aa);
                    }
                }
            }
            anamnese.setAlimentos(alimentosGerenciados);
        }

        // 6. Processar DadosFisiologicos
        if (anamnese.getDadosFisiologicos() != null) {
            anamnese.getDadosFisiologicos().setAnamnese(anamnese);
        }

        return anamneseRepository.save(anamnese);
    }

    @Transactional
    public Anamnese alterarAnamnese(Anamnese anamnese) {
        // Mesma lógica do criarAnamnese para garantir entidades gerenciadas
        return criarAnamnese(anamnese);
    }

    public Boolean deletarAnamnese(Integer id) {
        anamneseRepository.deleteById(id);
        return true;
    }
}
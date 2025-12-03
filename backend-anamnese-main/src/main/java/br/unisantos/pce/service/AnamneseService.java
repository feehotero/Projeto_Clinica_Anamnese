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

    @Autowired(required = false)
    private EscolaridadeRepository escolaridadeRepository;

    // ProfissaoRepository REMOVIDO pois agora é String

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

        // LÓGICA DE PROFISSÃO REMOVIDA (String é salva diretamente)

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
    public Anamnese alterarAnamnese(Anamnese anamneseAtualizado) {
        Optional<Anamnese> anamneseExistenteOpt = anamneseRepository.findById(anamneseAtualizado.getId());

        if (!anamneseExistenteOpt.isPresent()) {
            throw new RuntimeException("Anamnese não encontrada");
        }

        Anamnese anamneseExistente = anamneseExistenteOpt.get();

        if (anamneseAtualizado.getPaciente() != null && anamneseAtualizado.getPaciente().getId() != null) {
            Paciente paciente = pacienteRepository.findById(anamneseAtualizado.getPaciente().getId())
                    .orElseThrow(() -> new RuntimeException("Paciente não encontrado"));
            anamneseAtualizado.setPaciente(paciente);
        }

        if (anamneseAtualizado.getUsuario() != null && anamneseAtualizado.getUsuario().getId() != null) {
            User usuario = userRepository.findById(anamneseAtualizado.getUsuario().getId())
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
            anamneseAtualizado.setUsuario(usuario);
        }

        if (escolaridadeRepository != null && anamneseAtualizado.getEscolaridade() != null
                && anamneseAtualizado.getEscolaridade().getId() != null) {
            anamneseAtualizado.setEscolaridade(
                    escolaridadeRepository.findById(anamneseAtualizado.getEscolaridade().getId()).orElse(null));
        }

        // LÓGICA DE PROFISSÃO REMOVIDA

        if (rendaFamiliarRepository != null && anamneseAtualizado.getRendaFamiliar() != null
                && anamneseAtualizado.getRendaFamiliar().getId() != null) {
            anamneseAtualizado.setRendaFamiliar(
                    rendaFamiliarRepository.findById(anamneseAtualizado.getRendaFamiliar().getId()).orElse(null));
        }

        if (evacuacaoRepository != null && anamneseAtualizado.getEvacuacao() != null
                && anamneseAtualizado.getEvacuacao().getId() != null) {
            anamneseAtualizado.setEvacuacao(
                    evacuacaoRepository.findById(anamneseAtualizado.getEvacuacao().getId()).orElse(null));
        }

        if (refeicaoRepository != null && anamneseAtualizado.getRefeicoes() != null) {
            List<Refeicao> refeicoesGerenciadas = new ArrayList<>();
            for (Refeicao r : anamneseAtualizado.getRefeicoes()) {
                if (r.getId() != null) {
                    refeicaoRepository.findById(r.getId()).ifPresent(refeicoesGerenciadas::add);
                }
            }
            anamneseAtualizado.setRefeicoes(refeicoesGerenciadas);
        }

        if (anamneseExistente.getAlimentos() != null) {
            anamneseExistente.getAlimentos().clear();
            anamneseRepository.flush();
        }

        if (alimentoRepository != null && anamneseAtualizado.getAlimentos() != null
                && !anamneseAtualizado.getAlimentos().isEmpty()) {
            List<AnamneseAlimento> alimentosGerenciados = new ArrayList<>();

            for (AnamneseAlimento aa : anamneseAtualizado.getAlimentos()) {
                if (aa.getAlimento() != null && aa.getAlimento().getId() != null) {
                    Optional<Alimento> alimentoOpt = alimentoRepository.findById(aa.getAlimento().getId());
                    if (alimentoOpt.isPresent()) {
                        aa.setAlimento(alimentoOpt.get());
                        aa.setAnamnese(anamneseAtualizado);

                        AnamneseAlimentoKey key = new AnamneseAlimentoKey();
                        key.setAnamneseId(anamneseAtualizado.getId());
                        key.setAlimentoId(aa.getAlimento().getId());
                        aa.setId(key);

                        alimentosGerenciados.add(aa);
                    }
                }
            }
            anamneseAtualizado.setAlimentos(alimentosGerenciados);
        }

        if (anamneseAtualizado.getDadosFisiologicos() != null) {
            if (anamneseExistente.getDadosFisiologicos() != null) {
                anamneseAtualizado.getDadosFisiologicos()
                        .setId(anamneseExistente.getDadosFisiologicos().getId());
            }
            anamneseAtualizado.getDadosFisiologicos().setAnamnese(anamneseAtualizado);
        }

        anamneseAtualizado.setCriadoEm(anamneseExistente.getCriadoEm());

        return anamneseRepository.save(anamneseAtualizado);
    }

    public Boolean deletarAnamnese(Integer id) {
        anamneseRepository.deleteById(id);
        return true;
    }
}
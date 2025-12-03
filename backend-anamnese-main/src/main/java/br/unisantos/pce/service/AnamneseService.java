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
    public Anamnese alterarAnamnese(Anamnese anamneseAtualizado) {
        // 1. Buscar a anamnese existente
        Optional<Anamnese> anamneseExistenteOpt = anamneseRepository.findById(anamneseAtualizado.getId());

        if (!anamneseExistenteOpt.isPresent()) {
            throw new RuntimeException("Anamnese não encontrada");
        }

        Anamnese anamneseExistente = anamneseExistenteOpt.get();

        // 2. Buscar Paciente gerenciado
        if (anamneseAtualizado.getPaciente() != null && anamneseAtualizado.getPaciente().getId() != null) {
            Paciente paciente = pacienteRepository.findById(anamneseAtualizado.getPaciente().getId())
                    .orElseThrow(() -> new RuntimeException("Paciente não encontrado"));
            anamneseAtualizado.setPaciente(paciente);
        }

        // 3. Buscar Usuario gerenciado
        if (anamneseAtualizado.getUsuario() != null && anamneseAtualizado.getUsuario().getId() != null) {
            User usuario = userRepository.findById(anamneseAtualizado.getUsuario().getId())
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
            anamneseAtualizado.setUsuario(usuario);
        }

        // 4. Buscar entidades de domínio gerenciadas
        if (escolaridadeRepository != null && anamneseAtualizado.getEscolaridade() != null
                && anamneseAtualizado.getEscolaridade().getId() != null) {
            anamneseAtualizado.setEscolaridade(
                    escolaridadeRepository.findById(anamneseAtualizado.getEscolaridade().getId()).orElse(null));
        }

        if (profissaoRepository != null && anamneseAtualizado.getProfissao() != null
                && anamneseAtualizado.getProfissao().getId() != null) {
            anamneseAtualizado.setProfissao(
                    profissaoRepository.findById(anamneseAtualizado.getProfissao().getId()).orElse(null));
        }

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

        // 5. Processar Refeições
        if (refeicaoRepository != null && anamneseAtualizado.getRefeicoes() != null) {
            List<Refeicao> refeicoesGerenciadas = new ArrayList<>();
            for (Refeicao r : anamneseAtualizado.getRefeicoes()) {
                if (r.getId() != null) {
                    refeicaoRepository.findById(r.getId()).ifPresent(refeicoesGerenciadas::add);
                }
            }
            anamneseAtualizado.setRefeicoes(refeicoesGerenciadas);
        }

        // 6. CORREÇÃO CRÍTICA: Limpar alimentos antigos antes de adicionar novos
        if (anamneseExistente.getAlimentos() != null) {
            anamneseExistente.getAlimentos().clear();
            anamneseRepository.flush(); // Força a sincronização com o banco
        }

        // Processar novos Alimentos
        if (alimentoRepository != null && anamneseAtualizado.getAlimentos() != null
                && !anamneseAtualizado.getAlimentos().isEmpty()) {
            List<AnamneseAlimento> alimentosGerenciados = new ArrayList<>();

            for (AnamneseAlimento aa : anamneseAtualizado.getAlimentos()) {
                if (aa.getAlimento() != null && aa.getAlimento().getId() != null) {
                    Optional<Alimento> alimentoOpt = alimentoRepository.findById(aa.getAlimento().getId());
                    if (alimentoOpt.isPresent()) {
                        aa.setAlimento(alimentoOpt.get());
                        aa.setAnamnese(anamneseAtualizado);

                        // Criar chave composta
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

        // 7. CORREÇÃO: Processar DadosFisiologicos mantendo ID existente
        if (anamneseAtualizado.getDadosFisiologicos() != null) {
            // Se já existia dados no banco, usa o ID antigo
            if (anamneseExistente.getDadosFisiologicos() != null) {
                anamneseAtualizado.getDadosFisiologicos()
                        .setId(anamneseExistente.getDadosFisiologicos().getId());
            }
            // Garante o vínculo bidirecional
            anamneseAtualizado.getDadosFisiologicos().setAnamnese(anamneseAtualizado);
        }

        // 8. Manter data de criação original
        anamneseAtualizado.setCriadoEm(anamneseExistente.getCriadoEm());

        return anamneseRepository.save(anamneseAtualizado);
    }

    public Boolean deletarAnamnese(Integer id) {
        anamneseRepository.deleteById(id);
        return true;
    }
}
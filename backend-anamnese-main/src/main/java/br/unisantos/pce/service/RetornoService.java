package br.unisantos.pce.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.unisantos.pce.model.Anamnese;
import br.unisantos.pce.model.Paciente;
import br.unisantos.pce.model.Retorno;
import br.unisantos.pce.user.User;
import br.unisantos.pce.repository.AnamneseRepository;
import br.unisantos.pce.repository.PacienteRepository;
import br.unisantos.pce.repository.RetornoRepository;
import br.unisantos.pce.repository.UserRepository;

@Service
public class RetornoService {

    private final RetornoRepository repository;
    private final PacienteRepository pacienteRepository;
    private final UserRepository userRepository;
    private final AnamneseRepository anamneseRepository;

    @Autowired
    public RetornoService(RetornoRepository retornoRepository,
            PacienteRepository pacienteRepository,
            UserRepository userRepository,
            AnamneseRepository anamneseRepository) {
        this.repository = retornoRepository;
        this.pacienteRepository = pacienteRepository;
        this.userRepository = userRepository;
        this.anamneseRepository = anamneseRepository;
    }

    public List<Retorno> listarRetornos() {
        return (List<Retorno>) repository.findAllByOrderByCriadoEmDesc();
    }

    public List<Retorno> listarRetornosByPacienteId(Integer pacienteId) {
        return (List<Retorno>) repository.findAllByPacienteId(pacienteId);
    }

    public List<Retorno> listarRetornosByUsuarioId(Integer usuarioId) {
        return (List<Retorno>) repository.findAllByUsuarioId(usuarioId);
    }

    public List<Retorno> listarRetornosByPacienteNome(String pacienteNome) {
        return (List<Retorno>) repository.findByPacienteNome(pacienteNome.toUpperCase());
    }

    public List<Retorno> listarRetornosByAnamneseId(Integer anamneseId) {
        return (List<Retorno>) repository.findAllByAnamneseIdOrderByCriadoEmDesc(anamneseId);
    }

    public Optional<Retorno> consultarRetorno(Integer id) {
        return repository.findById(id);
    }

    @Transactional
    public Retorno criarRetorno(Retorno retorno) {
        // 1. Validar e setar Paciente existente
        if (retorno.getPaciente() != null && retorno.getPaciente().getId() != null) {
            Paciente paciente = pacienteRepository.findById(retorno.getPaciente().getId())
                    .orElseThrow(() -> new RuntimeException("Paciente não encontrado"));
            retorno.setPaciente(paciente);
        }

        // 2. Validar e setar Usuário existente
        if (retorno.getUsuario() != null && retorno.getUsuario().getId() != null) {
            User usuario = userRepository.findById(retorno.getUsuario().getId())
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
            retorno.setUsuario(usuario);
        }

        // 3. Validar e setar Anamnese existente
        // Isso garante que estamos usando a referência correta do banco e evita
        // que o JPA tente fazer updates indesejados na Anamnese ou seus filhos.
        if (retorno.getAnamnese() != null && retorno.getAnamnese().getId() != null) {
            Anamnese anamnese = anamneseRepository.findById(retorno.getAnamnese().getId())
                    .orElseThrow(() -> new RuntimeException("Anamnese não encontrada"));
            retorno.setAnamnese(anamnese);
        }

        return repository.save(retorno);
    }

    @Transactional
    public Retorno alterarRetorno(Retorno retorno) {
        return repository.save(retorno);
    }

    public Boolean deletarRetorno(Integer id) {
        repository.deleteById(id);
        return true;
    }
}
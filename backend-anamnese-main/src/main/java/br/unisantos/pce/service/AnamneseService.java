package br.unisantos.pce.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.unisantos.pce.model.Anamnese;
import br.unisantos.pce.repository.AnamneseRepository;

@Service
public class AnamneseService {

    private final AnamneseRepository repository;

    @Autowired
    AnamneseService(AnamneseRepository anamneseRepository) {
        this.repository = anamneseRepository;
    }

    public List<Anamnese> listarAnamneses() {
        return (List<Anamnese>) repository.findAllByOrderByCriadoEmDesc();
    }

    public List<Anamnese> listarAnamnesesByPacienteId(Integer pacienteId) {
        return (List<Anamnese>) repository.findAllByPacienteId(pacienteId);
    }

    public List<Anamnese> listarAnamnesesByPacienteNome(String pacienteNome) {
        return (List<Anamnese>) repository.findByPacienteNome(pacienteNome.toUpperCase());
    }

    public List<Anamnese> listarAnamnesesByUsuarioId(Integer usuarioId) {
        return (List<Anamnese>) repository.findAllByUsuarioId(usuarioId);
    }

    public Optional<Anamnese> consultarAnamnese(Integer id) {
        return repository.findById(id);
    }

    public Anamnese criarAnamnese(Anamnese anamnese) {
        repository.save(anamnese);
        return anamnese;
    }

    public Anamnese alterarAnamnese(Anamnese anamnese) {
        repository.save(anamnese);
        return anamnese;
    }

    public Boolean deletarAnamnese(Integer id) {
        repository.deleteById(id);
        return true;
    }
}

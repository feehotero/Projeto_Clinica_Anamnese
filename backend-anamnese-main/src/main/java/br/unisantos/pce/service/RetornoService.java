package br.unisantos.pce.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.unisantos.pce.model.Anamnese;
import br.unisantos.pce.model.Retorno;
import br.unisantos.pce.repository.RetornoRepository;

@Service
public class RetornoService {

    private final RetornoRepository repository;

    @Autowired
    RetornoService(RetornoRepository retornoRepository) {
        this.repository = retornoRepository;
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

    public Retorno criarRetorno(Retorno retorno) {
        repository.save(retorno);
        return retorno;
    }

    public Retorno alterarRetorno(Retorno retorno) {
        repository.save(retorno);
        return retorno;
    }

    public Boolean deletarRetorno(Integer id) {
        repository.deleteById(id);
        return true;
    }
}

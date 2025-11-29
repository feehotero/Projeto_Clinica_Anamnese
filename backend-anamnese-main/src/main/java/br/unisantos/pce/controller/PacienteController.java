package br.unisantos.pce.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import br.unisantos.pce.model.Anamnese;
import br.unisantos.pce.model.Paciente;
import br.unisantos.pce.model.Retorno;
import br.unisantos.pce.service.AnamneseService;
import br.unisantos.pce.service.PacienteService;
import br.unisantos.pce.service.RetornoService;

@RestController
// @CrossOrigin removido para usar a configuração global do
// SecurityConfigurations
@RequestMapping(value = "/pacientes", produces = MediaType.APPLICATION_JSON_VALUE)
public class PacienteController {

	private final PacienteService pacienteService;
	private final AnamneseService anamneseService;
	private final RetornoService retornoService;

	@Autowired
	public PacienteController(PacienteService pacienteService, AnamneseService anamneseService,
			RetornoService retornoService) {
		this.pacienteService = pacienteService;
		this.anamneseService = anamneseService;
		this.retornoService = retornoService;
	}

	@GetMapping
	public ResponseEntity<List<Paciente>> listarPacientes(
			@RequestParam(required = false, defaultValue = "") String nome) {
		return ResponseEntity.ok(pacienteService.listarPacientesPorNome(nome));
	}

	@GetMapping("/{id}")
	public ResponseEntity<Optional<Paciente>> consultarPaciente(@PathVariable Integer id) {
		Optional<Paciente> paciente = pacienteService.consultarPaciente(id);
		if (paciente.isPresent()) {
			return ResponseEntity.ok(pacienteService.consultarPaciente(id));
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}

	@PostMapping
	public ResponseEntity<?> criarPaciente(@RequestBody @Valid Paciente novopaciente, BindingResult result) {
		if (result.hasErrors()) {
			return ResponseEntity.badRequest().body(result.getAllErrors());
		}
		return ResponseEntity.status(HttpStatus.CREATED).body(pacienteService.criarPaciente(novopaciente));
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> alterarPaciente(@PathVariable Integer id,
			@RequestBody @Valid Paciente pacienteAtualizado, BindingResult result) {

		if (result.hasErrors()) {
			return ResponseEntity.badRequest().body(result.getAllErrors());
		}

		Optional<Paciente> pacienteOptional = pacienteService.consultarPaciente(id);
		List<Anamnese> anamneses = anamneseService.listarAnamnesesByPacienteId(id);
		List<Retorno> retornos = retornoService.listarRetornosByPacienteId(id);

		if (pacienteOptional.isPresent()) {
			Paciente paciente = pacienteOptional.get();

			paciente.setNome(pacienteAtualizado.getNome());
			paciente.setCpf(pacienteAtualizado.getCpf());
			paciente.setIdSexo(pacienteAtualizado.getIdSexo());
			paciente.setDataNascimento(pacienteAtualizado.getDataNascimento());

			if (!anamneses.isEmpty()) {
				for (Anamnese anamnese : anamneses) {
					anamnese.setPacienteNome(paciente.getNome());
					anamneseService.alterarAnamnese(anamnese);
				}
			}
			if (!retornos.isEmpty()) {
				for (Retorno retorno : retornos) {
					retorno.setPacienteNome(paciente.getNome());
					retornoService.alterarRetorno(retorno);
				}
			}
			return ResponseEntity.ok(pacienteService.alterarPaciente(paciente));
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Paciente não encontrado");
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deletarPaciente(@PathVariable Integer id) {
		Optional<Paciente> paciente = pacienteService.consultarPaciente(id);
		if (paciente.isPresent()) {
			pacienteService.deletarPaciente(id);
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}
}
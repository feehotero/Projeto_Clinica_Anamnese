package br.unisantos.pce.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.unisantos.pce.model.Anamnese;
import br.unisantos.pce.model.Paciente;
import br.unisantos.pce.model.Retorno;
import br.unisantos.pce.service.AnamneseService;
import br.unisantos.pce.service.PacienteService;
import br.unisantos.pce.service.RetornoService;
import jakarta.validation.Valid;

@RestController
@CrossOrigin("*")
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
			return ResponseEntity.ok(paciente);
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}

	@PostMapping
	public ResponseEntity<Paciente> criarPaciente(@Valid @RequestBody Paciente novoPaciente) {
		// Recebe { "nome": "...", "sexo": { "id": 1 }, ... }
		return ResponseEntity.status(HttpStatus.CREATED).body(pacienteService.criarPaciente(novoPaciente));
	}

	@PutMapping("/{id}")
	public ResponseEntity<Paciente> alterarPaciente(@PathVariable Integer id,
			@RequestBody Paciente pacienteAtualizado) {
		Optional<Paciente> pacienteOptional = pacienteService.consultarPaciente(id);

		// Buscamos formulários vinculados apenas se precisarmos atualizar cascata,
		// mas com o novo modelo relacional, o nome não é duplicado, então não
		// precisamos atualizar Anamneses/Retornos manualmente!

		if (pacienteOptional.isPresent()) {
			Paciente paciente = pacienteOptional.get();
			paciente.setNome(pacienteAtualizado.getNome());
			paciente.setCpf(pacienteAtualizado.getCpf());
			paciente.setSexo(pacienteAtualizado.getSexo()); // Objeto Sexo
			paciente.setDataNascimento(pacienteAtualizado.getDataNascimento());

			// NÃO é mais necessário iterar sobre anamneses e retornos para mudar o nome do
			// paciente,
			// pois agora eles possuem uma FK para a tabela Paciente.

			return ResponseEntity.ok(pacienteService.alterarPaciente(paciente));
		}

		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
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
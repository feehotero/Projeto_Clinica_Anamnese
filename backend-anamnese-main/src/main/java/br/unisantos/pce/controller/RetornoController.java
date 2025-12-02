package br.unisantos.pce.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.unisantos.pce.model.Retorno;
import br.unisantos.pce.service.RetornoService;
import jakarta.validation.Valid;

@RestController
@CrossOrigin("*")
@RequestMapping(value = "/retornos", produces = MediaType.APPLICATION_JSON_VALUE)
public class RetornoController {

	private final RetornoService retornoService;

	@Autowired
	public RetornoController(RetornoService retornoService) {
		this.retornoService = retornoService;
	}

	@GetMapping
	public ResponseEntity<List<Retorno>> listarRetornos() {
		return ResponseEntity.ok(retornoService.listarRetornos());
	}

	@GetMapping("/usuarios/{id}")
	public ResponseEntity<List<Retorno>> listarRetornosDoUsuario(@PathVariable Integer id) {
		return ResponseEntity.ok(retornoService.listarRetornosByUsuarioId(id));
	}

	@GetMapping("/pacientes/{id}")
	public ResponseEntity<List<Retorno>> listarRetornosDoPaciente(@PathVariable Integer id) {
		return ResponseEntity.ok(retornoService.listarRetornosByPacienteId(id));
	}

	@GetMapping("/{id}")
	public ResponseEntity<Optional<Retorno>> consultarRetorno(@PathVariable Integer id) {
		Optional<Retorno> retorno = retornoService.consultarRetorno(id);
		if (retorno.isPresent()) {
			return ResponseEntity.ok(retorno);
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}

	@PostMapping
	public ResponseEntity<Retorno> criarRetorno(@RequestBody @Valid Retorno newRetorno) {
		// O JSON deve vir com { "anamnese": { "id": X }, "paciente": { "id": Y }, ... }
		Retorno retornoSalvo = retornoService.criarRetorno(newRetorno);
		return ResponseEntity.status(HttpStatus.CREATED).body(retornoSalvo);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Retorno> alterarRetorno(@PathVariable Integer id,
			@RequestBody @Valid Retorno retornoAtualizado) {
		Optional<Retorno> retornoExistente = retornoService.consultarRetorno(id);

		if (retornoExistente.isPresent()) {
			retornoAtualizado.setId(id);
			retornoAtualizado.setCriadoEm(retornoExistente.get().getCriadoEm());

			return ResponseEntity.status(HttpStatus.OK).body(retornoService.alterarRetorno(retornoAtualizado));
		}

		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deletarRetorno(@PathVariable Integer id) {
		Optional<Retorno> retornoOptional = retornoService.consultarRetorno(id);
		if (retornoOptional.isPresent()) {
			retornoService.deletarRetorno(id);
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}
}
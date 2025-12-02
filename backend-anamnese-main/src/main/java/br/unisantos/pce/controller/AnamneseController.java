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

import br.unisantos.pce.model.Anamnese;
import br.unisantos.pce.service.AnamneseService;
import jakarta.validation.Valid;

@RestController
@CrossOrigin("*")
@RequestMapping(value = "/anamneses", produces = MediaType.APPLICATION_JSON_VALUE)
public class AnamneseController {

	private final AnamneseService anamneseService;

	@Autowired
	public AnamneseController(AnamneseService anamneseService) {
		this.anamneseService = anamneseService;
	}

	@GetMapping
	public ResponseEntity<List<Anamnese>> listarAnamneses() {
		return ResponseEntity.ok(anamneseService.listarAnamneses());
	}

	@GetMapping("/usuarios/{id}")
	public ResponseEntity<List<Anamnese>> listarAnamnesesDoUsuario(@PathVariable Integer id) {
		return ResponseEntity.ok(anamneseService.listarAnamnesesByUsuarioId(id));
	}

	@GetMapping("/pacientes/{id}")
	public ResponseEntity<List<Anamnese>> listarAnamnesesDoPaciente(@PathVariable Integer id) {
		return ResponseEntity.ok(anamneseService.listarAnamnesesByPacienteId(id));
	}

	@GetMapping("/{id}")
	public ResponseEntity<Optional<Anamnese>> consultarAnamnese(@PathVariable Integer id) {
		Optional<Anamnese> anamnese = anamneseService.consultarAnamnese(id);
		if (anamnese.isPresent()) {
			return ResponseEntity.ok(anamnese);
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}

	// Dentro de AnamneseController.java

	// No método criarAnamnese:
	@PostMapping
	public ResponseEntity<Anamnese> criarAnamnese(@RequestBody Anamnese newAnamnese) { // Removi @Valid temporariamente
																						// para teste
		try {
			// Vinculos (Pai <-> Filho)
			if (newAnamnese.getDadosFisiologicos() != null) {
				newAnamnese.getDadosFisiologicos().setAnamnese(newAnamnese);
			}

			if (newAnamnese.getAlimentos() != null) {
				for (var item : newAnamnese.getAlimentos()) {
					item.setAnamnese(newAnamnese);
					if (item.getAlimento() != null) {
						item.getId().setAlimentoId(item.getAlimento().getId());
					}
				}
			}

			Anamnese anamneseSalva = anamneseService.criarAnamnese(newAnamnese);
			return ResponseEntity.status(HttpStatus.CREATED).body(anamneseSalva);
		} catch (Exception e) {
			e.printStackTrace(); // Log no console do Java para ver o erro real
			return ResponseEntity.badRequest().build();
		}
	}

	@PutMapping("/{id}")
	public ResponseEntity<Anamnese> alterarAnamnese(@PathVariable Integer id,
			@RequestBody @Valid Anamnese anamneseAtualizado) {
		Optional<Anamnese> anamneseExistente = anamneseService.consultarAnamnese(id);

		if (anamneseExistente.isPresent()) {
			// Garante que o ID da URL seja mantido
			anamneseAtualizado.setId(id);

			// Mantém a data de criação original
			anamneseAtualizado.setCriadoEm(anamneseExistente.get().getCriadoEm());

			// Se o JSON vier com dadosFisiologicos, garante o vínculo do ID para
			// atualização
			if (anamneseAtualizado.getDadosFisiologicos() != null
					&& anamneseExistente.get().getDadosFisiologicos() != null) {
				anamneseAtualizado.getDadosFisiologicos().setId(anamneseExistente.get().getDadosFisiologicos().getId());
				anamneseAtualizado.getDadosFisiologicos().setAnamnese(anamneseAtualizado);
			}

			return ResponseEntity.ok(anamneseService.alterarAnamnese(anamneseAtualizado));
		}

		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deletarAnamnese(@PathVariable Integer id) {
		Optional<Anamnese> anamneseOptional = anamneseService.consultarAnamnese(id);
		if (anamneseOptional.isPresent()) {
			anamneseService.deletarAnamnese(id);
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}
}
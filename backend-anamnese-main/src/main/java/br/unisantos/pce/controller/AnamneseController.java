package br.unisantos.pce.controller;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import br.unisantos.pce.model.Anamnese;
import br.unisantos.pce.service.AnamneseService;

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

	@GetMapping("/{id}")
	public ResponseEntity<Optional<Anamnese>> consultarAnamnese(@PathVariable Integer id) {
		Optional<Anamnese> anamnese = anamneseService.consultarAnamnese(id);
		return anamnese.isPresent() ? ResponseEntity.ok(anamnese) : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}

	@PostMapping
	public ResponseEntity<?> criarAnamnese(@RequestBody Anamnese newAnamnese) {
		try {
			Anamnese anamneseSalva = anamneseService.criarAnamnese(newAnamnese);
			return ResponseEntity.status(HttpStatus.CREATED).body(anamneseSalva);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro no servidor: " + e.getMessage());
		}
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> alterarAnamnese(@PathVariable Integer id,
			@RequestBody Anamnese anamneseAtualizado) {

		try {
			Optional<Anamnese> anamneseExistenteOpt = anamneseService.consultarAnamnese(id);

			if (anamneseExistenteOpt.isPresent()) {
				// Garante o ID da Anamnese
				anamneseAtualizado.setId(id);

				// Salva (o service já trata toda a lógica)
				Anamnese anamneseSalva = anamneseService.alterarAnamnese(anamneseAtualizado);
				return ResponseEntity.ok(anamneseSalva);
			}

			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body("Anamnese não encontrada");

		} catch (RuntimeException e) {
			// Log do erro completo no servidor
			System.err.println("Erro ao atualizar anamnese: " + e.getMessage());
			e.printStackTrace();

			// Retorna mensagem mais específica para o frontend
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Erro ao atualizar: " + e.getMessage());

		} catch (Exception e) {
			System.err.println("Erro inesperado: " + e.getMessage());
			e.printStackTrace();

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Erro inesperado ao atualizar anamnese");
		}
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
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
			// O Service agora vai tratar de buscar as entidades gerenciadas
			Anamnese anamneseSalva = anamneseService.criarAnamnese(newAnamnese);
			return ResponseEntity.status(HttpStatus.CREATED).body(anamneseSalva);
		} catch (Exception e) {
			e.printStackTrace(); // Log do erro completo
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro no servidor: " + e.getMessage());
		}
	}

	@PutMapping("/{id}")
	public ResponseEntity<Anamnese> alterarAnamnese(@PathVariable Integer id,
			@RequestBody Anamnese anamneseAtualizado) {
		Optional<Anamnese> anamneseExistente = anamneseService.consultarAnamnese(id);
		if (anamneseExistente.isPresent()) {
			anamneseAtualizado.setId(id);
			anamneseAtualizado.setCriadoEm(anamneseExistente.get().getCriadoEm());
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
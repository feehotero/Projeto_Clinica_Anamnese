package br.unisantos.pce.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import br.unisantos.pce.service.UserService;
import br.unisantos.pce.user.User;
import jakarta.validation.Valid;

@RestController
@CrossOrigin("*")
@RequestMapping(value = "/usuarios", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

	private final UserService userService;

	@Autowired
	public UserController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping
	public ResponseEntity<List<User>> listarUsuarios(@RequestParam(required = false, defaultValue = "") String nome) {
		return ResponseEntity.ok(userService.listarUsuariosPorNomeOuMatricula(nome));
	}

	@GetMapping("/{id}")
	public ResponseEntity<Optional<User>> consultarUsuario(@PathVariable Integer id) {
		Optional<User> usuario = userService.consultarUsuarioPorId(id);
		if (usuario.isPresent()) {
			return ResponseEntity.ok(usuario);
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}

	@PostMapping
	public ResponseEntity<User> criarUsuario(@Valid @RequestBody User user) {
		if (userService.consultarUsuarioPorLogin(user.getLogin()) != null)
			return ResponseEntity.badRequest().build();

		String encryptedPassword = new BCryptPasswordEncoder().encode(user.getPassword());
		User newUser = new User(user.getNome(), user.getLogin(), encryptedPassword, user.getRole());

		return ResponseEntity.status(HttpStatus.CREATED).body(userService.criarUsuario(newUser));
	}

	@PutMapping("/{id}")
	public ResponseEntity<User> alterarUsuario(@PathVariable Integer id, @RequestBody Map<String, Object> atributos) {
		Optional<User> usuarioOptional = userService.consultarUsuarioPorId(id);

		if (usuarioOptional.isPresent()) {
			User usuario = usuarioOptional.get();

			if (atributos.containsKey("nome")) {
				usuario.setNome((String) atributos.get("nome"));
			}

			if (atributos.containsKey("login")) {
				usuario.setLogin((String) atributos.get("login"));
			}

			if (atributos.containsKey("password") && atributos.containsKey("passwordConfirm")) {
				if (atributos.get("password").equals(atributos.get("passwordConfirm"))) {
					String encryptedPassword = new BCryptPasswordEncoder().encode((String) atributos.get("password"));
					usuario.setPassword(encryptedPassword);
				} else {
					return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
				}
			}

			// Remoção da lógica de atualização em cascata de nomes em Anamnese/Retorno.
			// O banco de dados normalizado resolve isso automaticamente.

			return ResponseEntity.ok(userService.alterarUsuario(usuario));
		}

		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deletarUsuario(@PathVariable Integer id) {
		Optional<User> usuarioOptional = userService.consultarUsuarioPorId(id);
		if (usuarioOptional.isPresent()) {
			userService.deletarUsuario(id);
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}
}
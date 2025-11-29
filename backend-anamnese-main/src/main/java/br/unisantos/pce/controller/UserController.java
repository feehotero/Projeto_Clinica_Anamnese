package br.unisantos.pce.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
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
import br.unisantos.pce.model.Retorno;
import br.unisantos.pce.service.AnamneseService;
import br.unisantos.pce.service.RetornoService;
import br.unisantos.pce.service.UserService;
import br.unisantos.pce.user.User;

@RestController
@CrossOrigin("*")
@RequestMapping(value = "/usuarios", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

	private final UserService userService;
	private final AnamneseService anamneseService;
	private final RetornoService retornoService;

	@Autowired
	public UserController(UserService userService, AnamneseService anamneseService, RetornoService retornoService) {
		this.userService = userService;
		this.anamneseService = anamneseService;
		this.retornoService = retornoService;
	}

	@GetMapping
	public ResponseEntity<List<User>> listarUsuarios(@RequestParam(required = false, defaultValue = "") String nome) {
		return ResponseEntity.ok(userService.listarUsuariosPorNomeOuMatricula(nome));
	}

	@GetMapping("/{id}")
	public ResponseEntity<Optional<User>> consultarUsuario(@PathVariable Integer id) {
		Optional<User> usuario = userService.consultarUsuarioPorId(id);

		if (usuario.isPresent()) {
			return ResponseEntity.ok(userService.consultarUsuarioPorId(id));
		}

		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}

	@PostMapping
	public ResponseEntity<User> criarUsuario(@Valid @RequestBody User user) {
		if (userService.consultarUsuarioPorLogin(user.getLogin()) != null)
			return ResponseEntity.badRequest().build();

		String encryptedPassword = new BCryptPasswordEncoder().encode(user.getPassword());
		User newUser = new User(user.getLogin(), encryptedPassword, user.getNome(), user.getRole());

		return ResponseEntity.status(HttpStatus.CREATED).body(userService.criarUsuario(newUser));
	}

	@PutMapping("/{id}")
	public ResponseEntity<User> alterarUsuario(@PathVariable Integer id, @RequestBody Map<String, Object> atributos) {
		Optional<User> usuarioOptional = userService.consultarUsuarioPorId(id);
		List<Anamnese> anamneses = anamneseService.listarAnamnesesByUsuarioId(id);
		List<Retorno> retornos = retornoService.listarRetornosByUsuarioId(id);

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

			if (!anamneses.isEmpty()) {
				for (Anamnese anamnese : anamneses) {
					anamnese.setUsuarioNome(usuario.getLogin());
					anamneseService.alterarAnamnese(anamnese);
				}
			}

			if (!retornos.isEmpty()) {
				for (Retorno retorno : retornos) {
					retorno.setUsuarioNome(usuario.getLogin());
					retornoService.alterarRetorno(retorno);
				}
			}

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
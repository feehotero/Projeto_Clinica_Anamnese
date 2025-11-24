package br.unisantos.pce.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.unisantos.pce.repository.UserRepository;
import br.unisantos.pce.user.User;

@Service
public class UserService {
    
    @Autowired
	private UserRepository repository;

    public List<User> listarUsuarios() {
		return (List<User>) repository.findAllByOrderByCriadoEmDesc();
	}

	public List<User> listarUsuariosPorNomeOuMatricula(String nome) {
		return (List<User>) repository.findAllByNomeOrMatriculaOrderByCriadoEmDesc(nome);
	}
	
	public Optional<User> consultarUsuarioPorId (Integer id) {
		return repository.findById(id);
	}

	public User consultarUsuarioPorLogin (String login) {
		return (User) repository.findByLogin(login);
	}
	
	public User criarUsuario (User usuario) {
		repository.save(usuario);
		return usuario;
	}
	
	public User alterarUsuario (User usuario) {
		repository.save(usuario);
		return usuario;
	}

	public Boolean deletarUsuario (Integer id) {
		repository.deleteById(id);
		return true;
	}

}

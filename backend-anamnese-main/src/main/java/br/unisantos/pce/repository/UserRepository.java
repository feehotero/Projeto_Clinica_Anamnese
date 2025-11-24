package br.unisantos.pce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.userdetails.UserDetails;

import br.unisantos.pce.user.User;

public interface UserRepository extends JpaRepository<User, Integer> {
    UserDetails findByLogin(String login);

    @Query(value = "SELECT * FROM tb_usuario ORDER BY dt_criacao DESC", nativeQuery = true)
    List<User> findAllByOrderByCriadoEmDesc();

    @Query(value = "SELECT * FROM tb_usuario WHERE UPPER(nm_usuario) ILIKE CONCAT('%', :nome, '%') OR UPPER(cd_matricula) ILIKE CONCAT('%', :nome, '%') ORDER BY dt_criacao DESC", nativeQuery = true)
    List<User> findAllByNomeOrMatriculaOrderByCriadoEmDesc(@Param("nome") String nome);
}
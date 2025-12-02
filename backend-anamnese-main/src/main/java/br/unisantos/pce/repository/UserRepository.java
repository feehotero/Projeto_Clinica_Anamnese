package br.unisantos.pce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.userdetails.UserDetails;

import br.unisantos.pce.user.User;

public interface UserRepository extends JpaRepository<User, Integer> {

    // O JPA usa o nome do atributo na classe, então 'login' mapeia para
    // 'nm_usuario' automaticamente
    UserDetails findByLogin(String login);

    List<User> findAllByOrderByCriadoEmDesc();

    // Query Nativa atualizada
    @Query(value = "SELECT * FROM tb_usuario WHERE UPPER(nm_nome_completo) LIKE CONCAT('%', UPPER(:nome), '%') OR UPPER(nm_usuario) LIKE CONCAT('%', UPPER(:nome), '%') ORDER BY dt_criacao DESC", nativeQuery = true)
    List<User> findAllByNomeOrMatriculaOrderByCriadoEmDesc(@Param("nome") String nome);
}
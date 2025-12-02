package br.unisantos.pce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.unisantos.pce.model.Retorno;

public interface RetornoRepository extends JpaRepository<Retorno, Integer> {

    List<Retorno> findAllByPacienteId(Integer pacienteId);

    List<Retorno> findAllByUsuarioId(Integer usuarioId);

    List<Retorno> findAllByOrderByCriadoEmDesc();

    List<Retorno> findAllByAnamneseIdOrderByCriadoEmDesc(Integer anamneseId);

    // Usando JPQL para navegar no objeto Paciente e filtrar pelo nome
    @Query("SELECT r FROM Retorno r WHERE UPPER(r.paciente.nome) LIKE CONCAT('%', UPPER(:nome), '%') ORDER BY r.criadoEm DESC")
    List<Retorno> findByPacienteNome(@Param("nome") String pacienteNome);
}
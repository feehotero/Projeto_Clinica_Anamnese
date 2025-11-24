package br.unisantos.pce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import br.unisantos.pce.model.Paciente;

public interface PacienteRepository extends JpaRepository<Paciente, Integer> {

    @Query(value = "SELECT * FROM tb_paciente ORDER BY dt_criacao DESC", nativeQuery = true)
    List<Paciente> findAllByOrderByCriadoEmDesc();

    @Query(value = "SELECT * FROM tb_paciente WHERE UPPER(nm_paciente) ILIKE CONCAT('%', :nome, '%') ORDER BY dt_criacao DESC", nativeQuery = true)
    List<Paciente> findByNomeOrderByCriadoEmDesc(@Param("nome") String pacienteNome);

}
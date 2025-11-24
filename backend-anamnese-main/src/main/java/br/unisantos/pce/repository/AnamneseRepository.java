package br.unisantos.pce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.unisantos.pce.model.Anamnese;

public interface AnamneseRepository extends JpaRepository<Anamnese, Integer> {

    @Query(value = "SELECT * FROM tb_anamnese WHERE id_paciente = :pacienteId ORDER BY dt_criacao DESC", nativeQuery = true)
    List<Anamnese> findAllByPacienteId(@Param("pacienteId") Integer pacienteId);

    @Query(value = "SELECT * FROM tb_anamnese WHERE id_matricula = :usuarioId ORDER BY dt_criacao DESC", nativeQuery = true)
    List<Anamnese> findAllByUsuarioId(@Param("usuarioId") Integer usuarioId);

    @Query(value = "SELECT * FROM tb_anamnese ORDER BY dt_criacao DESC", nativeQuery = true)
    List<Anamnese> findAllByOrderByCriadoEmDesc();

    @Query(value = "SELECT * FROM tb_anamnese WHERE UPPER(paciente_nome) ILIKE CONCAT('%', :nome, '%') ORDER BY dt_criacao DESC", nativeQuery = true)
    List<Anamnese> findByPacienteNome(@Param("nome") String pacienteNome);

}
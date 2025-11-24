package br.unisantos.pce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.unisantos.pce.model.Retorno;

public interface RetornoRepository extends JpaRepository<Retorno, Integer> {

    @Query(value = "SELECT * FROM tb_retorno WHERE id_paciente = :pacienteId ORDER BY dt_criacao DESC", nativeQuery = true)
    List<Retorno> findAllByPacienteId(@Param("pacienteId") Integer pacienteId);

    @Query(value = "SELECT * FROM tb_retorno WHERE id_matricula = :usuarioId ORDER BY dt_criacao DESC", nativeQuery = true)
    List<Retorno> findAllByUsuarioId(@Param("usuarioId") Integer usuarioId);

    @Query(value = "SELECT * FROM tb_retorno ORDER BY dt_criacao DESC", nativeQuery = true)
    List<Retorno> findAllByOrderByCriadoEmDesc();

    @Query(value = "SELECT * FROM tb_retorno WHERE id_anamnese = :anamneseId ORDER BY dt_criacao DESC", nativeQuery = true)
    List<Retorno> findAllByAnamneseIdOrderByCriadoEmDesc(@Param("anamneseId") Integer anamneseId);

    @Query(value = "SELECT * FROM tb_retorno WHERE UPPER(paciente_nome) ILIKE CONCAT('%', :nome, '%') ORDER BY dt_criacao DESC", nativeQuery = true)
    List<Retorno> findByPacienteNome(@Param("nome") String pacienteNome);
}
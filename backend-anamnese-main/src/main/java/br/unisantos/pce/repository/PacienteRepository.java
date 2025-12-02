package br.unisantos.pce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import br.unisantos.pce.model.Paciente;

public interface PacienteRepository extends JpaRepository<Paciente, Integer> {

    // JPA resolve automaticamente baseado na Entidade (que já está mapeada para
    // tb_paciente)
    List<Paciente> findAllByOrderByCriadoEmDesc();

    // Query Nativa atualizada para a nova estrutura do banco
    @Query(value = "SELECT * FROM tb_paciente WHERE UPPER(nm_paciente) LIKE CONCAT('%', UPPER(:nome), '%') ORDER BY dt_criacao DESC", nativeQuery = true)
    List<Paciente> findByNomeOrderByCriadoEmDesc(@Param("nome") String pacienteNome);
}
package br.unisantos.pce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.unisantos.pce.model.Anamnese;

public interface AnamneseRepository extends JpaRepository<Anamnese, Integer> {

    // Query Nativa atualizada
    @Query(value = "SELECT * FROM tb_anamnese WHERE id_paciente = :id ORDER BY dt_criacao DESC", nativeQuery = true)
    List<Anamnese> findAllByPacienteId(@Param("id") Integer pacienteId);

    // JPA traduz automaticamente para o campo 'usuario' (User) que tem id
    List<Anamnese> findAllByUsuarioId(Integer usuarioId);

    List<Anamnese> findAllByOrderByCriadoEmDesc();

    // Como agora temos um JOIN implicito, é mais seguro usar JPQL (HQL) ao invés de
    // Native Query
    // para buscar pelo nome do paciente através do relacionamento
    @Query("SELECT a FROM Anamnese a WHERE UPPER(a.paciente.nome) LIKE CONCAT('%', UPPER(:nome), '%') ORDER BY a.criadoEm DESC")
    List<Anamnese> findByPacienteNome(@Param("nome") String pacienteNome);

}
package br.unisantos.pce.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "tb_paciente")
public class Paciente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_paciente")
    private Integer id;

    @Column(name = "nm_paciente", length = 60, nullable = false)
    @JsonProperty("nome")
    private String nome;

    @Column(name = "cd_cpf", length = 11, nullable = true)
    @JsonProperty("cpf")
    private String cpf;

    @Column(name = "id_sexo", nullable = false)
    @JsonProperty("idSexo") // Garante leitura do JSON { "idSexo": 1 }
    private Integer idSexo;

    @Column(name = "dt_nascimento", nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd") // Garante formato "2004-06-09"
    @JsonProperty("dataNascimento")
    private LocalDate dataNascimento;

    @Column(name = "dt_criacao", nullable = false)
    private LocalDateTime criadoEm;

    @PrePersist
    protected void onCreate() {
        criadoEm = LocalDateTime.now();
    }
}
package br.unisantos.pce.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_paciente")
public class Paciente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_paciente")
    private Integer id;

    @Column(name = "nm_paciente", nullable = false)
    private String nome;

    @Column(name = "cd_cpf")
    private String cpf;

    @Column(name = "dt_nascimento", nullable = false)
    private LocalDate dataNascimento;

    @Column(name = "dt_criacao", nullable = false)
    private LocalDateTime criadoEm;

    @ManyToOne
    @JoinColumn(name = "id_sexo", nullable = false)
    private Sexo sexo;

    @PrePersist
    protected void onCreate() {
        criadoEm = LocalDateTime.now();
    }
}
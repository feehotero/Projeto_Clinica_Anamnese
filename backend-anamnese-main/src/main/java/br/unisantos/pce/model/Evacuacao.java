package br.unisantos.pce.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "tb_evacuacao")
public class Evacuacao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_evacuacao")
    private Integer id;

    @Column(name = "ds_evacuacao", nullable = false, unique = true, length = 100)
    private String descricao;
}
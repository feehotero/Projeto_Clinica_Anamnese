package br.unisantos.pce.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "tb_profissao")
public class Profissao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_profissao")
    private Integer id;

    @Column(name = "ds_profissao", nullable = false, unique = true, length = 100)
    private String descricao;
}
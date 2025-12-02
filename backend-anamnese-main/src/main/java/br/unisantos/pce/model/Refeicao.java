package br.unisantos.pce.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "tb_refeicao")
public class Refeicao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_refeicao")
    private Integer id;

    @Column(name = "ds_refeicao", nullable = false, unique = true, length = 100)
    private String descricao;
}
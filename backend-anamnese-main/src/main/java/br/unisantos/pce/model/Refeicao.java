package br.unisantos.pce.model;

import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_refeicao")
public class Refeicao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_refeicao")
    private Integer id;

    @Column(name = "ds_refeicao")
    private String descricao;
}
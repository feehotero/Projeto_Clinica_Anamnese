package br.unisantos.pce.model;

import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_evacuacao")
public class Evacuacao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_evacuacao")
    private Integer id;

    @Column(name = "ds_evacuacao")
    private String descricao;
}
package br.unisantos.pce.model;

import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_escolaridade")
public class Escolaridade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_escolaridade")
    private Integer id;

    @Column(name = "ds_escolaridade")
    private String descricao;
}
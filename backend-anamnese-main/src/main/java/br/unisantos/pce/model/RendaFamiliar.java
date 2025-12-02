package br.unisantos.pce.model;

import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_renda_familiar")
public class RendaFamiliar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_renda_familiar")
    private Integer id;

    @Column(name = "ds_renda_familiar")
    private String descricao;
}
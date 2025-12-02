package br.unisantos.pce.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_anamnese_alimento")
public class AnamneseAlimento {
    public enum Frequencia {
        diario, semanal, mensal, nao
    }

    @EmbeddedId
    private AnamneseAlimentoKey id = new AnamneseAlimentoKey();

    @JsonBackReference
    @ManyToOne
    @MapsId("anamneseId")
    @JoinColumn(name = "id_anamnese")
    private Anamnese anamnese;

    @ManyToOne
    @MapsId("alimentoId")
    @JoinColumn(name = "id_alimento")
    private Alimento alimento;

    @Enumerated(EnumType.STRING)
    @Column(name = "ds_frenquencia_consumo")
    private Frequencia frequencia;
}
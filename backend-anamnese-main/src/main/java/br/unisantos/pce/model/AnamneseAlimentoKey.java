package br.unisantos.pce.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class AnamneseAlimentoKey implements Serializable {
    @Column(name = "id_anamnese")
    private Integer anamneseId;

    @Column(name = "id_alimento")
    private Integer alimentoId;
}
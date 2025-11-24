package br.unisantos.pce.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "tb_dados_fisiologicos")

public class DadosFisiologicos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_dados_fisiologicos", nullable = false)
    private Integer id;

    @Column(name = "id_anamnese", nullable = false)
    private Integer idAnamnese;

    @Column(name = "nr_peso")
    private Float nrPeso;

    @Column(name = "nr_estatura")
    private Float nrEstatura;

    @Column(name = "nr_imc")
    private Float nrImc;

    @Column(name = "nr_cb")
    private Float nrCb;

    @Column(name = "nr_dct")
    private Float nrDct;

    @Column(name = "nr_dcb")
    private Float nrDcb;

    @Column(name = "nr_dcse")
    private Float nrDcse;

    @Column(name = "nr_dcsi")
    private Float nrDcsi;

    @Column(name = "nr_somatoria_4_dobras")
    private Float nrSomatoria4Dobras;

    @Column(name = "nr_percentual_gordura_calculado")
    private Float nrPercentualGorduraCalculado;

    @Column(name = "nr_peso_gordura")
    private Float nrPesoGordura;

    @Column(name = "nr_peso_massa_magra")
    private Float nrPesoMassaMagra;

    @Column(name = "nr_total_agua")
    private Float nrTotalAgua;

    @Column(name = "nr_resistencia")
    private Float nrResistencia;

    @Column(name = "nr_reactancia")
    private Float nrReactancia;

    @Column(name = "nr_angulo_de_fase")
    private Float nrAnguloDeFase;

    @Column(name = "nr_circunferencia_cintura")
    private Float nrCircunferenciaCintura;

    @Column(name = "nr_circunferencia_quadril")
    private Float nrCircunferenciaQuadril;

    @Column(name = "nr_circunferencia_panturrilha")
    private Float nrCircunferenciaPanturrilha;

    @Column(name = "nr_porcentagem_agua_massa_magra")
    private Float nrPorcentagemAguaMassaMagra;

    @Column(name = "nr_emap_direita")
    private Float nrEmapDireita;

    @Column(name = "nr_emap_esquerda")
    private Float nrEmapEsquerda;

    @Column(name = "nr_forca_preencao_manual_direita")
    private Float nrForcaPreencaoManualDireita;

    @Column(name = "nr_forca_preencao_manual_esquerda")
    private Float nrForcaPreencaoManualEsquerda;

}

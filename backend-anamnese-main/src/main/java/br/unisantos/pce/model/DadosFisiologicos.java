package br.unisantos.pce.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_dados_fisiologicos")
public class DadosFisiologicos {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_dados_fisiologicos")
    private Integer id;

    @JsonBackReference
    @OneToOne
    @JoinColumn(name = "id_anamnese", nullable = false)
    private Anamnese anamnese;

    @Column(name = "nr_peso")
    private Float peso;
    @Column(name = "nr_estatura")
    private Float estatura;
    @Column(name = "nr_imc")
    private Float imc;
    @Column(name = "nr_circunferencia_cintura")
    private Float circunferenciaCintura;

    // Outros campos do banco se necessários
    @Column(name = "nr_cb")
    private Float cb;
    @Column(name = "nr_dct")
    private Float dct;
    @Column(name = "nr_dcb")
    private Float dcb;
    @Column(name = "nr_dcse")
    private Float dcse;
    @Column(name = "nr_dcsi")
    private Float dcsi;
    @Column(name = "nr_somatoria_4_dobras")
    private Float somatoria4Dobras;
    @Column(name = "nr_percentual_gordura_calculado")
    private Float percentualGorduraCalculado;
    @Column(name = "nr_peso_gordura")
    private Float pesoGordura;
    @Column(name = "nr_peso_massa_magra")
    private Float pesoMassaMagra;
    @Column(name = "nr_total_agua")
    private Float totalAgua;
    @Column(name = "nr_resistencia")
    private Float resistencia;
    @Column(name = "nr_reactancia")
    private Float reactancia;
    @Column(name = "nr_angulo_de_fase")
    private Float anguloDeFase;
    @Column(name = "nr_circunferencia_quadril")
    private Float circunferenciaQuadril;
    @Column(name = "nr_circunferencia_panturrilha")
    private Float circunferenciaPanturrilha;
    @Column(name = "nr_porcentagem_agua_massa_magra")
    private Float porcentagemAguaMassaMagra;
    @Column(name = "nr_emap_direita")
    private Float emapDireita;
    @Column(name = "nr_emap_esquerda")
    private Float emapEsquerda;
    @Column(name = "nr_forca_preencao_manual_direita")
    private Float forcaPreencaoManualDireita;
    @Column(name = "nr_forca_preencao_manual_esquerda")
    private Float forcaPreencaoManualEsquerda;
}
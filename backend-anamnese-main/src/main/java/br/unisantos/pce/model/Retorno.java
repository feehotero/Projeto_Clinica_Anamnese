package br.unisantos.pce.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "tb_retorno")
public class Retorno {

    private enum Opcao {
        sim,
        nao,
        mais_ou_menos;
    }

    private enum AtividadeFisica {
        manteve_o_que_ja_fazia,
        aumentei_a_frequencia_intensidade,
        ainda_nao_consegui_praticar,
        nao_iniciei_e_nao_pretendo_iniciar;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_retorno", nullable = false)
    private Integer id;

    @Column(name = "id_anamnese", nullable = false)
    private Integer anamneseId;

    @Column(name = "id_paciente", nullable = false)
    private Integer pacienteId;

    @Column(name = "paciente_nome", length = 60, nullable = false)
    private String pacienteNome;

    @Column(name = "id_matricula", nullable = false)
    private Integer usuarioId;

    @Column(name = "usuario_nome", length = 60, nullable = false)
    private String usuarioNome;

    @Column(name = "ds_metas_ultimas_consultas", nullable = true)
    private String dsMetasUltimasConsultas;

    @Column(name = "ds_comentarios_observacao", nullable = true)
    private String dsComentariosObservacao;

    @Column(name = "ds_metas_foram_cumpridas", nullable = true)
    @Enumerated(EnumType.STRING)
    private Opcao dsMetasForamCumpridas;

    @Column(name = "nr_desempenho_cumprimento_metas", columnDefinition = "TINYINT", nullable = true)
    private Integer nrDesempenhoCumprimentoMetas;

    @Column(name = "ds_motivo_assinalado_cumprimento_metas", nullable = true)
    private String dsMotivoAssinaladoCumprimentoMetas;

    @Column(name = "ds_como_sentiu_mudanca_habitos", nullable = true)
    private String dsComoSentiuMudancaHabitos;

    @Column(name = "ds_adaptacao_mudanca_habitos", nullable = true)
    private String dsAdaptacaoMudancaHabitos;

    @Column(name = "ds_motivos_dificuldade_adaptacao", nullable = true)
    private String dsMotivosDificuldadeAdaptacao;

    @Column(name = "ds_sente_precisa_melhorar_alimentacao", nullable = true)
    private String dsSentePrecisaMelhorarAlimentacao;

    @Column(name = "ds_habito_intestinal", nullable = true)
    private String dsHabitoIntestinal;

    @Column(name = "ds_atividade_fisica", nullable = true)
    @Enumerated(EnumType.STRING)
    private AtividadeFisica dsAtividadeFisica;

    @Column(name = "ds_metas_proximo_retorno", nullable = true)
    private String dsMetasProximoRetorno;

    @Column(name = "nr_peso")
    private Float nrPeso;

    @Column(name = "nr_imc")
    private Float nrImc;

    @Column(name = "nr_circunferencia_abdominal")
    private Float nrCircunferenciaAbdominal;

    @Column(name = "ds_valores_bioimpedancia", nullable = true)
    private String dsValoresBioimpedancia;

    @Column(name = "ds_observacoes_bioimpedancia", nullable = true)
    private String dsObservacoesBioimpedancia;

    @Column(name = "dt_criacao", nullable = false)
    private LocalDateTime criadoEm;

    @PrePersist
    protected void onCreate() {
        criadoEm = LocalDateTime.now();
    }

}
package br.unisantos.pce.model;

import java.time.LocalDateTime;
import jakarta.persistence.*;
import lombok.*;
import br.unisantos.pce.user.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties; // Importação Necessária

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_retorno")
public class Retorno {
    public enum Opcao {
        sim, nao, mais_ou_menos
    }

    public enum AtividadeFisica {
        manteve_o_que_ja_fazia, aumentei_a_frequencia_intensidade, ainda_nao_consegui_praticar,
        nao_iniciei_e_nao_pretendo_iniciar
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_retorno")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_anamnese", nullable = false)
    @JsonIgnoreProperties("retornos") // CORREÇÃO: Evita o loop infinito ignorando a lista 'retornos' dentro deste
                                      // objeto anamnese
    private Anamnese anamnese;

    @ManyToOne
    @JoinColumn(name = "id_paciente", nullable = false)
    private Paciente paciente;

    @ManyToOne
    @JoinColumn(name = "id_matricula", nullable = false)
    private User usuario;

    @Column(name = "ds_metas_ultimas_consultas", columnDefinition = "TEXT")
    private String metasUltimasConsultas;
    @Enumerated(EnumType.STRING)
    @Column(name = "ds_metas_foram_cumpridas")
    private Opcao metasForamCumpridas;
    @Column(name = "ds_comentarios_observacao", columnDefinition = "TEXT")
    private String comentariosObservacao;
    @Column(name = "nr_desempenho_cumprimento_metas")
    private Integer desempenhoMetas;
    @Column(name = "ds_motivo_assinalado_cumprimento_metas", columnDefinition = "TEXT")
    private String motivoCumprimentoMetas;
    @Column(name = "ds_como_sentiu_mudanca_habitos", columnDefinition = "TEXT")
    private String sentiuMudancaHabitos;
    @Column(name = "ds_adaptacao_mudanca_habitos", columnDefinition = "TEXT")
    private String adaptacaoMudanca;
    @Column(name = "ds_motivos_dificuldade_adaptacao", columnDefinition = "TEXT")
    private String dificuldadeAdaptacao;
    @Column(name = "ds_sente_precisa_melhorar_alimentacao", columnDefinition = "TEXT")
    private String melhorarAlimentacao;
    @Column(name = "ds_habito_intestinal", columnDefinition = "TEXT")
    private String habitoIntestinal;
    @Enumerated(EnumType.STRING)
    @Column(name = "ds_atividade_fisica")
    private AtividadeFisica atividadeFisica;
    @Column(name = "ds_metas_proximo_retorno", columnDefinition = "TEXT")
    private String metasProximoRetorno;

    @Column(name = "nr_peso")
    private Float peso;
    @Column(name = "nr_imc")
    private Float imc;
    @Column(name = "nr_circunferencia_abdominal")
    private Float circunferenciaAbdominal;
    @Column(name = "ds_valores_bioimpedancia", columnDefinition = "TEXT")
    private String valoresBioimpedancia;
    @Column(name = "ds_observacoes_bioimpedancia", columnDefinition = "TEXT")
    private String observacoesBioimpedancia;

    @Column(name = "dt_criacao", nullable = false)
    private LocalDateTime criadoEm;

    // --- CAMPOS ADICIONAIS PARA O FRONTEND ---
    public String getTipoFormulario() {
        return "Retorno";
    }

    public String getPacienteNome() {
        return paciente != null ? paciente.getNome() : "";
    }

    @PrePersist
    protected void onCreate() {
        criadoEm = LocalDateTime.now();
    }
}
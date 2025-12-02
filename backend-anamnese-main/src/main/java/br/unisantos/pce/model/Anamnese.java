package br.unisantos.pce.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import br.unisantos.pce.user.User;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_anamnese")
public class Anamnese {
	public enum Periodo {
		manha, tarde, noite, NA
	}

	public enum Opcao {
		sim, nao, as_vezes
	}

	public enum Companhia {
		sozinho, acompanhado
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_anamnese")
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "id_paciente", nullable = false)
	private Paciente paciente;

	@ManyToOne
	@JoinColumn(name = "id_matricula", nullable = false)
	private User usuario;

	// --- Campos ---
	@Column(name = "ds_motivo", columnDefinition = "TEXT")
	private String motivo;
	@Column(name = "ds_doenca", columnDefinition = "TEXT")
	private String doenca;
	@Column(name = "ds_antecedentes", columnDefinition = "TEXT")
	private String antecedentes;
	@Column(name = "ds_medicamento", columnDefinition = "TEXT")
	private String medicamento;
	@Column(name = "ds_suplemento", columnDefinition = "TEXT")
	private String suplemento;
	@Enumerated(EnumType.STRING)
	@Column(name = "nm_periodo_estudo")
	private Periodo periodoEstudo;
	@Enumerated(EnumType.STRING)
	@Column(name = "nm_periodo_trabalho")
	private Periodo periodoTrabalho;
	@Column(name = "nr_pessoa_domicilio")
	private Integer numPessoasDomicilio;
	@Column(name = "ds_quem_cozinha", columnDefinition = "TEXT")
	private String quemCozinha;
	@Enumerated(EnumType.STRING)
	@Column(name = "ds_necessidade_comer_estressado_ansioso_triste")
	private Opcao necessidadeComerEmocional;
	@Enumerated(EnumType.STRING)
	@Column(name = "ds_realiza_refeicoes_sozinho_acompanhado")
	private Companhia companhiaRefeicoes;
	@Column(name = "ds_fome_fisiologica")
	private String fomeFisiologica;
	@Column(name = "ds_nao_modificar_plano_alimentar", columnDefinition = "TEXT")
	private String naoModificarPlano;
	@Column(name = "ds_aversao_alimentar", columnDefinition = "TEXT")
	private String aversaoAlimentar;
	@Column(name = "ds_tolera_alimentos_proteina_animal", columnDefinition = "TEXT")
	private String toleraProteinaAnimal;
	@Column(name = "ds_alergia_intolerancias_alimentares", columnDefinition = "TEXT")
	private String alergias;
	@Column(name = "nr_nota_saciedade_pos_refeicoes")
	private Integer notaSaciedade;
	@Column(name = "nr_nota_humor_pos_refeicoes")
	private Integer notaHumor;
	@Column(name = "ds_metas", columnDefinition = "TEXT")
	private String metas;
	@Column(name = "consistencia_evacuacao")
	private Integer consistenciaEvacuacao;
	@Column(name = "ds_necessidade_emocional_comer")
	private String necessidadeEmocionalComer;

	// --- FKs ---
	@ManyToOne
	@JoinColumn(name = "id_escolaridade")
	private Escolaridade escolaridade;
	@ManyToOne
	@JoinColumn(name = "id_profissao")
	private Profissao profissao;
	@ManyToOne
	@JoinColumn(name = "id_renda_familiar")
	private RendaFamiliar rendaFamiliar;
	@ManyToOne
	@JoinColumn(name = "id_evacuacao")
	private Evacuacao evacuacao;

	// --- Listas ---
	@JsonManagedReference
	@OneToOne(mappedBy = "anamnese", cascade = CascadeType.ALL)
	private DadosFisiologicos dadosFisiologicos;

	@ManyToMany
	@JoinTable(name = "tb_anamnese_refeicao", joinColumns = @JoinColumn(name = "id_anamnese"), inverseJoinColumns = @JoinColumn(name = "id_refeicao"))
	private List<Refeicao> refeicoes;

	@JsonManagedReference
	@OneToMany(mappedBy = "anamnese", cascade = CascadeType.ALL)
	private List<AnamneseAlimento> alimentos = new ArrayList<>();

	@Column(name = "dt_criacao", nullable = false)
	private LocalDateTime criadoEm;

	@PrePersist
	protected void onCreate() {
		criadoEm = LocalDateTime.now();
	}
}
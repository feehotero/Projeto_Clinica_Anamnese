package br.unisantos.pce.model;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "tb_anamnese")
public class Anamnese {

	private enum Periodo {
		manha,
		tarde,
		noite,
		NA;
	}

	private enum Opcao {
		sim,
		nao,
		as_vezes;
	}

	private enum Companhia {
		sozinho,
		acompanhado;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_anamnese", nullable = false)
	private Integer id;

	@Column(name = "id_paciente", nullable = false)
	private Integer pacienteId;

	@Column(name = "paciente_nome", length = 60, nullable = false)
	private String pacienteNome;

	@Column(name = "id_matricula", nullable = false)
	private Integer usuarioId;

	@Column(name = "usuario_nome", length = 60, nullable = false)
	private String usuarioNome;

	@Column(name = "id_escolaridade", nullable = true)
	private Integer idEscolaridade;

	@Column(name = "id_profissao", nullable = true)
	private Integer idProfissao;

	@Column(name = "id_renda_familiar", nullable = true)
	private Integer idRendaFamiliar;

	@Column(name = "nm_periodo_estudo", nullable = true)
	@Enumerated(EnumType.STRING)
	private Periodo nmPeriodoEstudo;

	@Column(name = "nm_periodo_trabalho", nullable = true)
	@Enumerated(EnumType.STRING)
	private Periodo nmPeriodoTrabalho;

	@Column(name = "nr_pessoa_domicilio", columnDefinition = "TINYINT", nullable = true)
	private Integer nrPessoaDomicilio;

	@Column(name = "ds_motivo", columnDefinition = "TEXT", nullable = true)
	private String dsMotivo;

	@Column(name = "ds_doenca", columnDefinition = "TEXT")
	private String dsDoenca;

	@Column(name = "ds_antecedentes", columnDefinition = "TEXT")
	private String dsAntecedentes;

	@Column(name = "ds_medicamento", columnDefinition = "TEXT")
	private String dsMedicamento;

	@Column(name = "ds_suplemento")
	private String dsSuplemento;

	@Column(name = "id_evacuacao", nullable = true)
	private Integer idEvacuacao;

	@Column(name = "ds_quem_cozinha")
	private String dsQuemCozinha;

	@Column(name = "ds_necessidade_comer_estressado_ansioso_triste")
	@Enumerated(EnumType.STRING)
	private Opcao dsNecessidadeComerEstressadoAnsiosoTriste;

	@Column(name = "ds_realiza_refeicoes_sozinho_acompanhado")
	@Enumerated(EnumType.STRING)
	private Companhia dsRealizaRefeicoesSozinhoAcompanhado;

	@Column(name = "ds_fome_fisiologica")
	private String dsFomeFisiologica;

	@Column(name = "ds_necessidade_emocional_comer")
	private String dsNecessidadeEmocionalComer;

	@Column(name = "ds_nao_modificar_plano_alimentar")
	private String dsNaoModificarPlanoAlimentar;

	@Column(name = "ds_aversao_alimentar")
	private String dsAversaoAlimentar;

	@Column(name = "ds_tolera_alimentos_proteina_animal")
	private String dsToleraAlimentosProteinaAnimal;

	@Column(name = "ds_alergia_intolerancias_alimentares")
	private String dsAlergiaIntoleranciasAlimentares;

	@Column(name = "nr_nota_saciedade_pos_refeicoes", columnDefinition = "TINYINT", nullable = true)
	private Integer nrNotaSaciedadePosRefeicoes;

	@Column(name = "nr_nota_humor_pos_refeicoes", columnDefinition = "TINYINT", nullable = true)
	private Integer nrNotaHumorPosRefeicoes;

	@Column(name = "ds_metas")
	private String dsMetas;

	@Column(name = "dt_criacao", nullable = false)
	private LocalDateTime criadoEm;

	@PrePersist
	protected void onCreate() {
		criadoEm = LocalDateTime.now();
	}

	@Transient
	private List<Retorno> retornos;
}
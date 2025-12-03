/* --- 1. CRIAÇÃO DAS TABELAS --- */

CREATE TABLE tb_sexo (
    id_sexo INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    ds_sexo VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE tb_escolaridade (
    id_escolaridade INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    ds_escolaridade VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE tb_profissao (
    id_profissao INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    ds_profissao VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE tb_renda_familiar (
    id_renda_familiar INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    ds_renda_familiar VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE tb_evacuacao (
    id_evacuacao INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    ds_evacuacao VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE tb_refeicao (
    id_refeicao INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    ds_refeicao VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE tb_alimento (
    id_alimento INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    ds_alimento VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE tb_usuario (
    id_matricula INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    nm_usuario VARCHAR(60) NOT NULL,
    nm_nome_completo VARCHAR(100) NOT NULL,
    nm_senha VARCHAR(60) NOT NULL,
    user_role ENUM('ADMIN', 'USER') NOT NULL,
    dt_criacao DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL
);

CREATE TABLE tb_paciente (
    id_paciente INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    nm_paciente VARCHAR(60) NOT NULL,
    cd_cpf VARCHAR(11) NULL,
    dt_nascimento DATE NOT NULL,
    dt_criacao DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL,
    id_sexo INT NOT NULL,
    FOREIGN KEY (id_sexo) REFERENCES tb_sexo(id_sexo)
);

CREATE TABLE tb_anamnese (
    id_anamnese INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    id_paciente INT NOT NULL,
    id_matricula INT NOT NULL,
    
    -- Dados Clínicos e Pessoais
    ds_motivo TEXT NULL,
    ds_doenca TEXT NULL,
    ds_antecedentes TEXT NULL,
    ds_medicamento TEXT NULL,
    ds_suplemento TEXT NULL,
    
    -- Rotina
    nm_periodo_estudo ENUM('manha', 'tarde', 'noite', 'NA') NULL,
    fl_lanche_estudo BOOLEAN NULL, 
    nm_periodo_trabalho ENUM('manha', 'tarde', 'noite', 'NA') NULL,
    fl_lanche_trabalho BOOLEAN NULL, 
    
    -- Dados Sociais
    nr_pessoa_domicilio TINYINT NULL,
    ds_quem_cozinha TEXT NULL,
    
    -- Comportamento Alimentar
    ds_necessidade_comer_estressado_ansioso_triste ENUM('sim', 'nao', 'as_vezes') NULL,
    ds_realiza_refeicoes_sozinho_acompanhado ENUM('sozinho', 'acompanhado') NULL,
    ds_fome_fisiologica VARCHAR(60) NULL,
    ds_necessidade_emocional_comer VARCHAR(60) NULL,
    ds_nao_modificar_plano_alimentar TEXT NULL,
    ds_aversao_alimentar TEXT NULL,
    ds_tolera_alimentos_proteina_animal TEXT NULL,
    ds_alergia_intolerancias_alimentares TEXT NULL,
    
    -- Novos Campos de Comportamento
    ds_sintomas_excesso_alimentos TEXT NULL,
    ds_dificuldade_rotina TEXT NULL,
    ds_consolo_alimentar ENUM('sim', 'nao', 'as_vezes') NULL,
    ds_dificuldade_parar_comer ENUM('sim', 'nao', 'as_vezes') NULL,

    -- Notas
    nr_nota_saciedade_pos_refeicoes TINYINT NULL,
    nr_nota_humor_pos_refeicoes TINYINT NULL,
    
    -- Intestino
    consistencia_evacuacao TINYINT NULL,
    
    -- Atividade Física
    fl_pratica_atividade_fisica BOOLEAN NULL,
    ds_atividade_fisica_descricao TEXT NULL,

    -- Descrição das Refeições
    ds_cafe_manha TEXT NULL,
    ds_lanche_manha TEXT NULL,
    ds_almoco TEXT NULL,
    ds_lanche_tarde TEXT NULL,
    ds_jantar TEXT NULL,
    ds_ceia TEXT NULL,

    -- Metas e Auditoria
    ds_metas TEXT NULL,
    dt_criacao DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL,
    
    -- Foreign Keys
    id_escolaridade INT NULL,
    id_profissao INT NULL,
    id_renda_familiar INT NULL,
    id_evacuacao INT NULL,
    
    FOREIGN KEY (id_paciente) REFERENCES tb_paciente(id_paciente),
    FOREIGN KEY (id_matricula) REFERENCES tb_usuario(id_matricula),
    FOREIGN KEY (id_escolaridade) REFERENCES tb_escolaridade(id_escolaridade),
    FOREIGN KEY (id_profissao) REFERENCES tb_profissao(id_profissao),
    FOREIGN KEY (id_renda_familiar) REFERENCES tb_renda_familiar(id_renda_familiar),
    FOREIGN KEY (id_evacuacao) REFERENCES tb_evacuacao(id_evacuacao)
);

CREATE TABLE tb_dados_fisiologicos (
    id_dados_fisiologicos INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    id_anamnese INT NOT NULL,
    nr_peso FLOAT NULL,
    nr_estatura FLOAT NULL,
    nr_imc FLOAT NULL,
    nr_cb FLOAT NULL,
    nr_dct FLOAT NULL,
    nr_dcb FLOAT NULL,
    nr_dcse FLOAT NULL,
    nr_dcsi FLOAT NULL,
    nr_somatoria_4_dobras FLOAT NULL,
    nr_percentual_gordura_calculado FLOAT NULL,
    nr_peso_gordura FLOAT NULL,
    nr_peso_massa_magra FLOAT NULL,
    nr_total_agua FLOAT NULL,
    nr_resistencia FLOAT NULL,
    nr_reactancia FLOAT NULL,
    nr_angulo_de_fase FLOAT NULL,
    nr_circunferencia_cintura FLOAT NULL,
    nr_circunferencia_quadril FLOAT NULL,
    nr_circunferencia_panturrilha FLOAT NULL,
    nr_porcentagem_agua_massa_magra FLOAT NULL,
    nr_emap_direita FLOAT NULL,
    nr_emap_esquerda FLOAT NULL,
    nr_forca_preencao_manual_direita FLOAT NULL,
    nr_forca_preencao_manual_esquerda FLOAT NULL,
    FOREIGN KEY (id_anamnese) REFERENCES tb_anamnese(id_anamnese)
);

CREATE TABLE tb_anamnese_refeicao (
    id_anamnese INT NOT NULL,
    id_refeicao INT NOT NULL,
    PRIMARY KEY (id_anamnese, id_refeicao),
    FOREIGN KEY (id_anamnese) REFERENCES tb_anamnese(id_anamnese),
    FOREIGN KEY (id_refeicao) REFERENCES tb_refeicao(id_refeicao)
);

CREATE TABLE tb_anamnese_alimento (
    id_anamnese INT NOT NULL,
    id_alimento INT NOT NULL,
    ds_frenquencia_consumo ENUM('diario', 'semanal', 'mensal', 'nao') NULL,
    PRIMARY KEY (id_anamnese, id_alimento),
    FOREIGN KEY (id_anamnese) REFERENCES tb_anamnese(id_anamnese),
    FOREIGN KEY (id_alimento) REFERENCES tb_alimento(id_alimento)
);

CREATE TABLE tb_retorno (
    id_retorno INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    id_anamnese INT NOT NULL,
    id_paciente INT NOT NULL,
    id_matricula INT NOT NULL,
    ds_metas_ultimas_consultas TEXT NULL,
    ds_metas_foram_cumpridas ENUM('sim', 'nao', 'mais_ou_menos') NULL,
    ds_comentarios_observacao TEXT NULL,
    nr_desempenho_cumprimento_metas TINYINT NULL,
    ds_motivo_assinalado_cumprimento_metas TEXT NULL,
    ds_como_sentiu_mudanca_habitos TEXT NULL,
    ds_adaptacao_mudanca_habitos TEXT NULL,
    ds_motivos_dificuldade_adaptacao TEXT NULL,
    ds_sente_precisa_melhorar_alimentacao TEXT NULL,
    ds_habito_intestinal TEXT NULL,
    ds_atividade_fisica ENUM('manteve_o_que_ja_fazia', 'aumentei_a_frequencia_intensidade', 'ainda_nao_consegui_praticar', 'nao_iniciei_e_nao_pretendo_iniciar') NULL,
    ds_metas_proximo_retorno TEXT NULL,
    nr_peso FLOAT NULL,
    nr_imc FLOAT NULL,
    nr_circunferencia_abdominal FLOAT NULL,
    ds_valores_bioimpedancia TEXT NULL,
    ds_observacoes_bioimpedancia TEXT NULL,
    dt_criacao DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL,
    FOREIGN KEY (id_anamnese) REFERENCES tb_anamnese(id_anamnese),
    FOREIGN KEY (id_paciente) REFERENCES tb_paciente(id_paciente),
    FOREIGN KEY (id_matricula) REFERENCES tb_usuario(id_matricula)
);

/* --- 2. INSERTS DE DOMÍNIO (DADOS OBRIGATÓRIOS) --- */

-- Usuário Admin
INSERT INTO tb_usuario (nm_usuario, nm_nome_completo, nm_senha, user_role) 
VALUES ('admin', 'Administrador', '$2a$12$nYYxMYVyQirARNSQBGLGOuVq7HAZkS9rJh7JmvUnKldrBkH1aFlSe', 'ADMIN');

-- Domínios Básicos
INSERT INTO tb_sexo (ds_sexo) VALUES ('Masculino'), ('Feminino'), ('Não informado');

INSERT INTO tb_escolaridade (ds_escolaridade) VALUES 
('Fundamental Completo'), ('Ensino Médio Completo'), ('Ensino Superior Incompleto'), ('Ensino Superior Completo'), ('Outra');

INSERT INTO tb_profissao (ds_profissao) VALUES ('Engenheiro'), ('Médico'), ('Estudante');

INSERT INTO tb_renda_familiar (ds_renda_familiar) VALUES 
('Menos de 1 salário mínimo'), ('De 1 a 2 salários mínimos'), ('De 3 a 5 salários mínimos'), ('Mais de 5 salários mínimos'), ('Não sei');

INSERT INTO tb_evacuacao (ds_evacuacao) VALUES 
('Diária'), ('Alternada'), ('Três vezes por semana'), ('Menos de 3 vezes por semana');

-- Refeições (IMPORTANTE: IDs 1 a 6)
INSERT INTO tb_refeicao (ds_refeicao) VALUES 
('Café da Manhã'), ('Lanche da Manhã'), ('Almoço'), 
('Lanche da Tarde'), ('Jantar'), ('Ceia');

-- Alimentos (Lista Completa)
INSERT INTO tb_alimento (ds_alimento) VALUES 
('Cenoura'), ('Beterraba'), ('Berinjela'), ('Pepino'), ('Abobrinha'), ('Chuchu'), ('Tomate'), -- IDs 1-7
('Acelga'), ('Agrião'), ('Alface'), ('Brócolis'), ('Chicória'), ('Couve Manteiga'), ('Couve-flor'), ('Espinafre'), ('Rúcula'), -- IDs 8-16
('Laranja'), ('Tangerina'), ('Limão'), ('Abacaxi'), ('Acerola'), ('Morango'), ('Kiwi'), ('Maracujá'), -- IDs 17-24
('Banana'), ('Goiaba'), ('Maçã'), ('Mamão'), ('Manga'), ('Melancia'), ('Melão'), ('Pera'), ('Pêssego'), ('Uva'), -- IDs 25-34
('Feijão'), ('Lentilha'), ('Ervilha'), ('Soja'), ('Grão-de-Bico'), ('Vagem'), -- IDs 35-40
('Bovina'), ('Suína'), ('Aves'), ('Peixe'), ('Frutos do Mar'), ('Ovo'), -- IDs 41-46
('Arroz Branco Polido'), ('Arroz Integral'), -- IDs 47-48
('Leite Integral/Desnatado/Semi'), ('Leite Sem Lactose'), ('Queijos'), ('Iogurtes'), -- IDs 49-52
('Aveia'), ('Granola'), ('Quinoa'), ('Linhaça/Chia'), -- IDs 53-56
('Pão Francês'), ('Pão de Forma'), ('Pão Integral'), -- IDs 57-59
('Requeijão'), ('Margarina'), ('Manteiga'), ('Ricota'), ('Cottage'), ('Doce de Leite'), ('Creme de Avelã'), ('Geleia'), -- IDs 60-67
('Mistinho/Esfiha'), ('Coxinha/Croquete'), ('Empadas'), ('Pão de Queijo'), -- IDs 68-71
('Salgadinho de Pacote'), ('Biscoito Salgado'), ('Biscoito Doce'), ('Chocolate'), ('Refrigerante'), ('Suco em Pó/Caixa'), ('Pratos Congelados'), ('Macarrão Instantâneo'), ('Bolinho'), ('Fast Food'), -- IDs 72-81
('Salame'), ('Presunto'), ('Apresuntado/Mortadela'), ('Linguiça'), ('Salsicha'), ('Peito de Peru'), ('Rosbife'), ('Nuggets'), ('Hambúrguer'), -- IDs 82-90
('Açúcar'), ('Adoçante'), ('Mel/Melado'), ('Confeitaria'), ('Guloseimas'), ('Achocolatados'), -- IDs 91-96
('Ketchup'), ('Mostarda'), ('Shoyu'), ('Tare'), ('Maionese'); -- IDs 97-101
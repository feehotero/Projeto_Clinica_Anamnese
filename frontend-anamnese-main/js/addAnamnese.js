const formAddAnamnese = document.querySelector(".formAddAnamnese");
const divStepButtons = document.querySelector(".div-step-buttons");
const nextBtn = document.getElementById("nextBtn");
const prevBtn = document.getElementById("prevBtn");
const tabs = document.getElementsByClassName("tab");
const fPacienteSelect = document.getElementById("pacienteSelect");
let currentTab = 0;

function cadastrarAnamnese() {
  const data = getData();
  let forbidden = false;
  return new Promise((resolve, reject) => {
    if (validateForm(formAddAnamnese)) {
      fetch(urlApi + endpointAnamneses, {
        headers: {
          "Content-Type": "application/json",
          "Authorization": `${token}`
        },
        method: "POST",
        body: JSON.stringify(data)
      })
        .then(response => {
          if (!response.ok) {
            forbidden = true;
            return Promise.reject();
          }
          goodWarning.textContent = "Anamnese cadastrada com sucesso!";
          resolve(response);
        })
        .catch(error => {
          if (forbidden) {
            badWarning.textContent = "Dados inválidos.";
          } else {
            badWarning.textContent = "Erro na comunicação com a API.";
          }
          reject(error);
        })
    } else {
      badWarning.textContent = "Preencha todos os campos obrigatórios";
    }
  })
}

function getData() {
  
  const inputs = document.querySelectorAll('.formAddAnamnese input, .formAddAnamnese select, .formAddAnamnese textarea');
  const data = { usuarioId: usuarioId, };

  
  const fieldMapping = {
   
    'idEscolaridade': 'idEscolaridade',
    'idProfissao': 'idProfissao',
    'idRendaFamiliar': 'idRendaFamiliar',
    'nmPeriodoEstudo': 'nmPeriodoEstudo',
    'nmPeriodoTrabalho': 'nmPeriodoTrabalho',
    'nrPessoaDomicilio': 'nrPessoaDomicilio',
    
    'dsMotivo': 'dsMotivo',
    'dsDoenca': 'dsDoenca',
    'dsAntecedentes': 'dsAntecedentes',
    'dsMedicamento': 'dsMedicamento',
    'dsSuplemento': 'dsSuplemento',
    'idEvacuacao': 'idEvacuacao',
    
    'dsQuemCozinha': 'dsQuemCozinha',
    'dsNecessidadeComerEstressadoAnsiosoTriste': 'dsNecessidadeComerEstressadoAnsiosoTriste',
    'dsRealizaRefeicoesSozinhoAcompanhado': 'dsRealizaRefeicoesSozinhoAcompanhado',
    'dsFomeFisiologica': 'dsFomeFisiologica',
    'dsNecessidadeEmocionalComer': 'dsNecessidadeEmocionalComer',
    'dsNaoModificarPlanoAlimentar': 'dsNaoModificarPlanoAlimentar',
    'dsAversaoAlimentar': 'dsAversaoAlimentar',
    'dsToleraAlimentosProteinaAnimal': 'dsToleraAlimentosProteinaAnimal',
    'dsAlergiaIntoleranciasAlimentares': 'dsAlergiaIntoleranciasAlimentares',
    'nrNotaSaciedadePosRefeicoes': 'nrNotaSaciedadePosRefeicoes',
    'nrNotaHumorPosRefeicoes': 'nrNotaHumorPosRefeicoes',
    
    'dsMetas': 'dsMetas',
    
    'pacienteId': 'pacienteId',
    'usuarioId': 'usuarioId'
    
    
  };

  
  inputs.forEach(input => {
    let key = input.name;
    let value = input.value;
   
    const newKey = fieldMapping[key] || key;

    
    if (newKey.startsWith('id') && input.type === 'select-one' && value !== "") {
        value = parseInt(value);
    }
    
    if (input.type === 'checkbox') {
     
      return; 
    } else if (input.type === 'radio') {
      if (input.checked) {
        
        data[newKey] = value; 
      } else {
        return;
      }
    } else if (value === "") {
      value = null;
    }

    if (newKey in fieldMapping || newKey === 'pacienteId' || newKey === 'usuarioId') {
      data[newKey] = value;
    }
  });
  

  data.usuarioId = usuarioId;

  
  delete data.escolaridade;
  delete data.periodoEstudo;
  delete data.lancheEstudo;
  delete data.periodoTrabalho;
  delete data.lancheTrabalho;
  delete data.profissao;
  delete data.rendaFamiliar;
  delete data.numPessoasDomicilio;
  delete data.motivo;
  delete data.apresentaDoenca;
  delete data.antecedentesFamiliares;
  delete data.medicamentosContinuos;
  delete data.suplementosComplementos;
  delete data.frequenciaEvacuacao;
  delete data.consistenciaEvacuacao;
  delete data.praticaAtvFisica;
  delete data.atvFisica;
  delete data.cafeDaManha;
  delete data.lancheDaManha;
  delete data.almoco;
  delete data.lancheDaTarde;
  delete data.jantar;
  delete data.ceia;
  delete data.quemCozinha;
  delete data.necessidadeComerEstressadoAnsiosoTriste;
  delete data.realizaRefeicoesSozinhoAcompanhado;
  delete data.excessoAlimentosNaoSaudaveisSintomas;
  delete data.dificuldadeRotinaAlimentarSaudavel;
  delete data.necessidadeConsoloAlimentar;
  delete data.dificuldadePararDeComer;
  delete data.frequenciaFomeFisiologica;
  delete data.frequenciaNecessidadeEmocionalComer;
  delete data.naoModificarPlanoAlimentar;
  delete data.aversaoAlimentar;
  delete data.toleraAlimentosProteinaAnimal;
  delete data.alergiaIntoleranciasAlimentares;
  delete data.notaSaciedadePosRefeicoes;
  delete data.notaHumorPosRefeicoes;
  delete data.pesoAtual;
  delete data.estatura;
  delete data.imc;
  delete data.cb;
  delete data.dct;
  delete data.dcb;
  delete data.dcse;
  delete data.dcsi;
  delete data.somatoria4Dobras;
  delete data.porcentagemGorduraCorporalSomatoria4Dobras;
  delete data.pesoGordura;
  delete data.pesoMassaMagra;
  delete data.totalAgua;
  delete data.porcentagemAguaMassaMagra;
  delete data.resistencia;
  delete data.reactancia;
  delete data.anguloDeFase;
  delete data.circunferenciaCintura;
  delete data.circunferenciaQuadril;
  delete data.circunferenciaPanturrilha;
  delete data.emapDireita;
  delete data.emapEsquerda;
  delete data.forcaPreencaoManualDireita;
  delete data.forcaPreencaoManualEsquerda;
  delete data.metas;

  return data;
}

verificarAutenticacao();
listarPacientesSelect(fPacienteSelect);
showTab(currentTab);

verificarAutenticacao();
listarPacientesSelect(fPacienteSelect);
showTab(currentTab);

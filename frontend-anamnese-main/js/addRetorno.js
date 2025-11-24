const formAddRetorno = document.querySelector(".formAddRetorno");
const divStepButtons = document.querySelector(".div-step-buttons");
const nextBtn = document.getElementById("nextBtn");
const prevBtn = document.getElementById("prevBtn");
const tabs = document.getElementsByClassName("tab");
const fPacienteSelect = document.getElementById("pacienteSelect");
const fAnamneseSelect = document.getElementById("anamneseSelect");
let currentTab = 0;

function cadastrarRetorno() {
  const data = getData();
  let forbidden = false;
  return new Promise((resolve, reject) => {
    if (validateForm(formAddRetorno)) {
      fetch(urlApi + endpointRetornos, {
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
          goodWarning.textContent = "Retorno cadastrada com sucesso!";
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
 
  const inputs = document.querySelectorAll('.formAddRetorno input, .formAddRetorno select, .formAddRetorno textarea');
  const data = { 
    usuarioId: usuarioId,

    anamneseId: document.getElementById('anamneseId').value ? parseInt(document.getElementById('anamneseId').value) : null,
    pacienteId: document.getElementById('pacienteId').value ? parseInt(document.getElementById('pacienteId').value) : null,
  };
  
  inputs.forEach(input => {
    let value = input.value;
    let name = input.name;
    
    
    switch (name) {
        case 'metasUltimasConsultas':
            name = 'dsMetasUltimasConsultas';
            break;
        case 'comentariosObservacao':
            name = 'dsComentariosObservacao';
            break;
        case 'metasForamCumpridas':
            name = 'dsMetasForamCumpridas';
            break;
        case 'desempenhoCumprimentoMetas':
            name = 'nrDesempenhoCumprimentoMetas';
            break;
        case 'motivoAssinaladoCumprimentoMetas':
            name = 'dsMotivoAssinaladoCumprimentoMetas';
            break;
        case 'comoSentiuMudancaHabitos':
            name = 'dsComoSentiuMudancaHabitos';
            break;
        case 'adaptacaoMudancaHabitos':
            name = 'dsAdaptacaoMudancaHabitos';
            break;
        case 'motivosDificuldadeAdaptacao':
            name = 'dsMotivosDificuldadeAdaptacao';
            break;
        case 'sentePrecisaMelhorarAlimentacao':
            name = 'dsSentePrecisaMelhorarAlimentacao';
            break;
        case 'habitoIntestinal':
            name = 'dsHabitoIntestinal';
            break;
        case 'atvFisica':
            name = 'dsAtividadeFisica';
            break;
        case 'metasProximoRetorno':
            name = 'dsMetasProximoRetorno';
            break;
        case 'pesoAtual':
            name = 'nrPeso';
            break;
        case 'imc':
            name = 'nrImc';
            break;
        case 'circunferenciaAbdominal':
            name = 'nrCircunferenciaAbdominal';
            break;
        case 'valoresBioimpedancia':
            name = 'dsValoresBioimpedancia';
            break;
        case 'observacoesBioimpedancia':
            name = 'dsObservacoesBioimpedancia';
            break;
    }
    
    if (input.type === 'radio') {
      if (input.checked) {
        value = input.value;
        data[name] = value;
      }
    } else if (name === "pacienteNome") {
        
        return;
    } else if (value !== "" && name !== 'anamneseId' && name !== 'pacienteId') {
      data[name] = value;
    }
  });

  return data;
}

async function adicionarAnamnese() {
  const urlParametros = new URLSearchParams(window.location.search);
  const id = urlParametros.get('id');
  if(!isNaN(id)) {
    document.getElementById("anamneseId").value = id;
    await preencherCampoPaciente(id);
  }
}

limparCampoPaciente();
verificarAutenticacao();
listarPacientesSelect(fPacienteSelect);
listarAnamnesesSelect(fAnamneseSelect);
adicionarAnamnese();
showTab(currentTab);

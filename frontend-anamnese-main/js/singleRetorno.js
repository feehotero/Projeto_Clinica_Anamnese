const formAddRetorno = document.querySelector(".formAddRetorno");
const pacienteNome = document.getElementById("pacienteNome");
const criadoPor = document.getElementById("criadoPor");
const retornoId = localStorage.getItem("retornoId");
const divStepButtons = document.querySelector(".div-step-buttons");
const nextBtn = document.getElementById("nextBtn");
const prevBtn = document.getElementById("prevBtn");
const tabs = document.getElementsByClassName("tab");
const fPacienteSelect = document.getElementById("pacienteSelect");
const fAnamneseSelect = document.getElementById("anamneseSelect");
const botaoDeletar = document.getElementById("botaoDeletar");
let currentTab = 0;

function consultarRetorno() {
    fetch(urlApi + endpointRetornos + "/" + retornoId, {
        headers: {
            "Authorization": `${token}`
        }
    })
        .then(response => response.json())
        .then(retorno => {
            preencherCampoPaciente(retorno.anamneseId);
            criadoPor.textContent = retorno.usuarioNome;
            

            const fieldMappingReverse = {
             
                'dsComentariosObservacao': 'dsComentariosObservacao',
                'dsMetasForamCumpridas': 'dsMetasForamCumpridas',
                'nrDesempenhoCumprimentoMetas': 'nrDesempenhoCumprimentoMetas',
                'dsMotivoAssinaladoCumprimentoMetas': 'dsMotivoAssinaladoCumprimentoMetas',
                'dsComoSentiuMudancaHabitos': 'dsComoSentiuMudancaHabitos',
                'dsAdaptacaoMudancaHabitos': 'dsAdaptacaoMudancaHabitos',
                'dsMotivosDificuldadeAdaptacao': 'dsMotivosDificuldadeAdaptacao',
                'dsSentePrecisaMelhorarAlimentacao': 'dsSentePrecisaMelhorarAlimentacao',
                'dsHabitoIntestinal': 'dsHabitoIntestinal',
                'dsAtividadeFisica': 'dsAtividadeFisica',
                'dsMetasProximoRetorno': 'dsMetasProximoRetorno',
                'nrPeso': 'nrPeso',
                'nrImc': 'nrImc',
                'nrCircunferenciaAbdominal': 'nrCircunferenciaAbdominal',
                'dsValoresBioimpedancia': 'dsValoresBioimpedancia',
                'dsObservacoesBioimpedancia': 'dsObservacoesBioimpedancia',
                
             
                'anamneseId': 'anamneseId',
                'pacienteId': 'pacienteId'
            };

            for (const key in fieldMappingReverse) {
                if (retorno.hasOwnProperty(key)) {
                    const value = retorno[key];
                    const inputName = fieldMappingReverse[key];
                    const input = document.querySelector(`[name=${inputName}]`);

                    if (input) {
                        switch (input.type) {
                            case 'radio':
                                const radioButton = document.querySelector(`input[type=radio][name=${inputName}][value="${value}"]`);
                                if (radioButton) {
                                    radioButton.checked = true;
                                }
                                break;
                            default:
                                input.value = value;
                        }
                    } 
                }
            }

        
            if(!pacienteNome.textContent) {
                pacienteNome.textContent = retorno.pacienteNome;
            }

        })
        .catch(error => {
            console.error(error);
        })
}

function atualizarRetorno() {
    const data = getData();
    const forbidden = false;
    return new Promise((resolve, reject) => {
        if (validateForm(formAddRetorno)) {
            fetch(urlApi + endpointRetornos + "/" + retornoId, {
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": `${token}`
                },
                method: "PUT",
                body: JSON.stringify(data)
            })
                .then(response => {
                    if (!response.ok) {
                        forbidden = true;
                        return Promise.reject();
                    }
                    goodWarning.textContent = "Retorno atualizada com sucesso!";
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
               
                return; 
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
            data[name] = input.value;
          }
        } else if (name !== 'pacienteNome' && value !== "") {
          data[name] = value;
        }
    });

    return data;
}

botaoDeletar.addEventListener("click", async () => {
    try {
        await deletarItem(retornoId, endpointRetornos);
        window.location.href = "formularios.html";
    } catch {
        verificarAutenticacao();
    }
});

verificarAutenticacao();
listarPacientesSelect(fPacienteSelect);
listarAnamnesesSelect(fAnamneseSelect);
consultarRetorno();
showTab(currentTab);

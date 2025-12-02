const formAddRetorno = document.querySelector(".formAddRetorno");
const pacienteNome = document.getElementById("pacienteNome");
const criadoPor = document.getElementById("criadoPor");
const retornoId = localStorage.getItem("retornoId");
const nextBtn = document.getElementById("nextBtn");
const prevBtn = document.getElementById("prevBtn");
const tabs = document.getElementsByClassName("tab");
const botaoDeletar = document.getElementById("botaoDeletar");
let currentTab = 0;

function consultarRetorno() {
    fetch(urlApi + endpointRetornos + "/" + retornoId, {
        headers: { "Authorization": `${token}` }
    })
    .then(response => response.json())
    .then(retorno => {
        // Preencher header e selects
        if(retorno.paciente) pacienteNome.textContent = retorno.paciente.nome;
        if(retorno.usuario) criadoPor.textContent = retorno.usuario.nome;
        
        // Helper
        const setVal = (id, v) => { if(document.getElementById(id)) document.getElementById(id).value = v || ""; };

        setVal("anamneseId", retorno.anamnese?.id);
        setVal("pacienteId", retorno.paciente?.id);
        setVal("pacienteNome", retorno.paciente?.nome);

        setVal("metasUltimasConsultas", retorno.metasUltimasConsultas);
        setVal("comentariosObservacao", retorno.comentariosObservacao);
        setVal("metasForamCumpridas", retorno.metasForamCumpridas);
        setVal("motivoAssinaladoCumprimentoMetas", retorno.motivoCumprimentoMetas);
        setVal("comoSentiuMudancaHabitos", retorno.sentiuMudancaHabitos);
        setVal("adaptacaoMudancaHabitos", retorno.adaptacaoMudanca);
        setVal("motivosDificuldadeAdaptacao", retorno.dificuldadeAdaptacao);
        setVal("sentePrecisaMelhorarAlimentacao", retorno.melhorarAlimentacao);
        setVal("habitoIntestinal", retorno.habitoIntestinal);
        setVal("atvFisica", retorno.atividadeFisica);
        setVal("metasProximoRetorno", retorno.metasProximoRetorno);
        
        setVal("pesoAtual", retorno.peso);
        setVal("imc", retorno.imc);
        setVal("circunferenciaAbdominal", retorno.circunferenciaAbdominal);
        
        setVal("valoresBioimpedancia", retorno.valoresBioimpedancia);
        setVal("observacoesBioimpedancia", retorno.observacoesBioimpedancia);

        // Radio desempenho
        if(retorno.desempenhoMetas) {
            const rad = document.querySelector(`input[name="desempenhoCumprimentoMetas"][value="${retorno.desempenhoMetas}"]`);
            if(rad) rad.checked = true;
        }
    })
    .catch(console.error);
}

// Atualizar (PUT) usando mesma lógica do addRetorno
function atualizarRetorno() {
    // ... Replicar getData() do addRetorno.js aqui ...
    const data = getDataLocal(); 
    return new Promise((resolve, reject) => {
        if (validateForm(formAddRetorno)) {
            fetch(urlApi + endpointRetornos + "/" + retornoId, {
                headers: { "Content-Type": "application/json", "Authorization": `${token}` },
                method: "PUT",
                body: JSON.stringify(data)
            })
            .then(r => {
                if(!r.ok) return Promise.reject();
                goodWarning.textContent = "Atualizado!";
                resolve(r);
            })
            .catch(reject);
        }
    });
}

function getDataLocal() {
    // Cópia local do getData do addRetorno.js para garantir funcionamento independente
    const getVal = (id) => document.getElementById(id).value || null;
    const usuarioIdLocal = localStorage.getItem("usuarioId");
    const data = {
        usuario: { id: usuarioIdLocal },
        paciente: { id: getVal("pacienteId") },
        anamnese: { id: getVal("anamneseId") },
        metasUltimasConsultas: getVal("metasUltimasConsultas"),
        comentariosObservacao: getVal("comentariosObservacao"),
        metasForamCumpridas: getVal("metasForamCumpridas"),
        desempenhoMetas: getVal("desempenhoCumprimentoMetas"),
        motivoCumprimentoMetas: getVal("motivoAssinaladoCumprimentoMetas"),
        sentiuMudancaHabitos: getVal("comoSentiuMudancaHabitos"),
        adaptacaoMudanca: getVal("adaptacaoMudancaHabitos"),
        dificuldadeAdaptacao: getVal("motivosDificuldadeAdaptacao"),
        melhorarAlimentacao: getVal("sentePrecisaMelhorarAlimentacao"),
        habitoIntestinal: getVal("habitoIntestinal"),
        atividadeFisica: getVal("atvFisica"),
        metasProximoRetorno: getVal("metasProximoRetorno"),
        peso: getVal("pesoAtual"),
        imc: getVal("imc"),
        circunferenciaAbdominal: getVal("circunferenciaAbdominal"),
        valoresBioimpedancia: getVal("valoresBioimpedancia"),
        observacoesBioimpedancia: getVal("observacoesBioimpedancia")
    };
    const desempenhoRadio = document.querySelector('input[name="desempenhoCumprimentoMetas"]:checked');
    if(desempenhoRadio) data.desempenhoMetas = desempenhoRadio.value;
    return data;
}

botaoDeletar.addEventListener("click", async () => {
    try {
        await deletarItem(retornoId, endpointRetornos);
        window.location.href = "formularios.html";
    } catch { verificarAutenticacao(); }
});

verificarAutenticacao();
consultarRetorno();
showTab(currentTab);
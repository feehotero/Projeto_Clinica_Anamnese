const formAddAnamnese = document.querySelector(".formAddAnamnese");
const nextBtn = document.getElementById("nextBtn");
const prevBtn = document.getElementById("prevBtn");
const tabs = document.getElementsByClassName("tab");
const fPacienteSelect = document.getElementById("pacienteId");
let currentTab = 0;

function cadastrarAnamnese() {
  const data = getData();
  
  const originalText = nextBtn.textContent;
  nextBtn.disabled = true;
  nextBtn.textContent = "Enviando...";

  return new Promise((resolve, reject) => {
    if (!data.paciente || !data.paciente.id) {
        badWarning.textContent = "Erro: Selecione um paciente na primeira etapa.";
        nextBtn.disabled = false;
        nextBtn.textContent = originalText;
        reject("Paciente não selecionado");
        return;
    }

    fetch(urlApi + endpointAnamneses, {
      headers: { "Content-Type": "application/json", "Authorization": `${token}` },
      method: "POST",
      body: JSON.stringify(data)
    })
    .then(response => {
      if (!response.ok) {
          return response.text().then(text => Promise.reject(text));
      }
      return response.json();
    })
    .then(data => {
      goodWarning.textContent = "Anamnese salva com sucesso!";
      badWarning.textContent = "";
      setTimeout(() => { window.location.href = "formularios.html"; }, 1500);
      resolve(data);
    })
    .catch(error => {
      console.error("Erro:", error);
      nextBtn.disabled = false;
      nextBtn.textContent = originalText;
      badWarning.textContent = "Erro ao salvar: " + error;
      reject(error);
    })
  })
}

function getData() {
  const getString = (id) => { const el = document.getElementById(id); return (el && el.value.trim() !== "") ? el.value.trim() : null; };
  const getInt = (id) => { const el = document.getElementById(id); if(!el || el.value.trim() === "") return null; const v = parseInt(el.value, 10); return isNaN(v) ? null : v; };
  const getFloat = (id) => { const el = document.getElementById(id); if(!el || el.value.trim() === "") return null; const v = parseFloat(el.value.replace(',', '.')); return isNaN(v) ? null : v; };
  
  // CORREÇÃO: Enviar apenas o ID, não objeto aninhado
  const getObjId = (id) => { 
    const v = getInt(id); 
    return v ? { id: v } : null; 
  };

  const usuarioIdLocal = localStorage.getItem("usuarioId");

  const data = {
    usuario: { id: parseInt(usuarioIdLocal) },
    paciente: { id: getInt("pacienteId") },
    escolaridade: getObjId("escolaridade"),
    profissao: getObjId("profissao"),
    rendaFamiliar: getObjId("rendaFamiliar"),
    evacuacao: getObjId("evacuacao"),
    motivo: getString("motivo"),
    doenca: getString("doenca"),
    antecedentes: getString("antecedentes"),
    medicamento: getString("medicamento"),
    suplemento: getString("suplemento"),
    periodoEstudo: getString("periodoEstudo"),
    periodoTrabalho: getString("periodoTrabalho"),
    quemCozinha: getString("quemCozinha"),
    necessidadeComerEmocional: getString("necessidadeComerEmocional"),
    companhiaRefeicoes: getString("companhiaRefeicoes"),
    fomeFisiologica: getString("fomeFisiologica"),
    necessidadeEmocionalComer: getString("necessidadeEmocionalComer"),
    naoModificarPlano: getString("naoModificarPlano"),
    aversaoAlimentar: getString("aversaoAlimentar"),
    toleraProteinaAnimal: getString("toleraProteinaAnimal"),
    alergias: getString("alergias"),
    metas: getString("metas"),
    numPessoasDomicilio: getInt("numPessoasDomicilio"),
    notaSaciedade: getInt("notaSaciedade"),
    notaHumor: getInt("notaHumor"),
    consistenciaEvacuacao: getInt("consistenciaEvacuacao"),
    dadosFisiologicos: {
        peso: getFloat("peso"),
        estatura: getFloat("estatura"),
        imc: getFloat("imc"),
        circunferenciaCintura: getFloat("circunferenciaCintura"),
        somatoria4Dobras: getFloat("somatoria4Dobras"),
        pesoGordura: getFloat("pesoGordura")
    },
    refeicoes: [],
    alimentos: []
  };

  // Refeições selecionadas
  document.querySelectorAll('input[name="refeicoes"]:checked').forEach(ck => {
    data.refeicoes.push({ id: parseInt(ck.value) });
  });

  // Alimentos selecionados
  document.querySelectorAll('.food-row').forEach(row => {
      const id = row.getAttribute('data-id');
      const rad = row.querySelector(`input[name="f_${id}"]:checked`);
      if(rad) {
        data.alimentos.push({ 
          alimento: { id: parseInt(id) }, 
          frequencia: rad.value 
        });
      }
  });

  return data;
}

function listarPacientesSelect(select) {
    fetch(urlApi + endpointPacientes, { headers: { "Authorization": `${token}` } })
      .then(r => r.json())
      .then(data => {
        select.innerHTML = '<option selected disabled value="">Selecione...</option>';
        data.forEach(p => {
          const opt = document.createElement('option');
          opt.value = p.id;
          opt.textContent = p.nome;
          select.appendChild(opt);
        });
      });
}

verificarAutenticacao();
if(fPacienteSelect) listarPacientesSelect(fPacienteSelect);
showTab(currentTab);
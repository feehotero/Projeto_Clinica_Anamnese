const formAddAnamnese = document.querySelector(".formAddAnamnese");
const pacienteNome = document.getElementById("pacienteNome");
const criadoPor = document.getElementById("criadoPor");
const anamneseId = localStorage.getItem("anamneseId");
const nextBtn = document.getElementById("nextBtn");
const prevBtn = document.getElementById("prevBtn");
const tabs = document.getElementsByClassName("tab");
const botaoDeletar = document.getElementById("botaoDeletar");
const botaoCriarRetorno = document.getElementById("botaoCriarRetorno");
let currentTab = 0;

function consultarAnamnese() {
    fetch(urlApi + endpointAnamneses + "/" + anamneseId, {
        headers: { "Authorization": `${token}` }
    })
    .then(response => response.json())
    .then(anamnese => {
        // Preencher Header se existirem elementos (no editar não tem header)
        // Preencher inputs
        const setVal = (id, v) => { const el=document.getElementById(id); if(el) el.value = v || ""; };
        
        setVal("pacienteId", anamnese.paciente?.id);
        setVal("escolaridade", anamnese.escolaridade?.id);
        setVal("profissao", anamnese.profissao?.id);
        setVal("rendaFamiliar", anamnese.rendaFamiliar?.id);
        setVal("evacuacao", anamnese.evacuacao?.id);

        setVal("motivo", anamnese.motivo);
        setVal("doenca", anamnese.doenca);
        setVal("antecedentes", anamnese.antecedentes);
        setVal("medicamento", anamnese.medicamento);
        setVal("suplemento", anamnese.suplemento);
        setVal("periodoEstudo", anamnese.periodoEstudo);
        setVal("periodoTrabalho", anamnese.periodoTrabalho);
        setVal("numPessoasDomicilio", anamnese.numPessoasDomicilio);
        setVal("quemCozinha", anamnese.quemCozinha);
        setVal("necessidadeComerEmocional", anamnese.necessidadeComerEmocional);
        setVal("companhiaRefeicoes", anamnese.companhiaRefeicoes);
        setVal("fomeFisiologica", anamnese.fomeFisiologica);
        setVal("naoModificarPlano", anamnese.naoModificarPlano);
        setVal("aversaoAlimentar", anamnese.aversaoAlimentar);
        setVal("toleraProteinaAnimal", anamnese.toleraProteinaAnimal);
        setVal("alergias", anamnese.alergias);
        setVal("notaSaciedade", anamnese.notaSaciedade);
        setVal("notaHumor", anamnese.notaHumor);
        setVal("metas", anamnese.metas);

        if(anamnese.dadosFisiologicos) {
            setVal("peso", anamnese.dadosFisiologicos.peso);
            setVal("estatura", anamnese.dadosFisiologicos.estatura);
            setVal("imc", anamnese.dadosFisiologicos.imc);
            setVal("circunferenciaCintura", anamnese.dadosFisiologicos.circunferenciaCintura);
        }

        if(anamnese.refeicoes) {
            anamnese.refeicoes.forEach(r => {
               const ck = document.querySelector(`input[name="refeicoes"][value="${r.id}"]`);
               if(ck) ck.checked = true;
            });
        }

        if(anamnese.alimentos) {
            anamnese.alimentos.forEach(item => {
               // item.alimento.id e item.frequencia
               const rd = document.querySelector(`input[name="f_${item.alimento.id}"][value="${item.frequencia}"]`);
               if(rd) rd.checked = true;
            });
        }
    });
}

function atualizarAnamnese() {
    const data = getData(); // Usa a mesma função do addAnamnese.js (deve estar importada ou copiada)
    return new Promise((resolve, reject) => {
        fetch(urlApi + endpointAnamneses + "/" + anamneseId, {
            headers: { "Content-Type": "application/json", "Authorization": `${token}` },
            method: "PUT",
            body: JSON.stringify(data)
        })
        .then(r => {
             if(!r.ok) return Promise.reject();
             goodWarning.textContent = "Atualizado!";
             resolve(r);
        })
        .catch(e => {
            badWarning.textContent = "Erro ao atualizar";
            reject(e);
        });
    });
}

// Cópia da função getData para garantir funcionamento isolado
function getData() {
  const getString = (id) => { const el = document.getElementById(id); return (el && el.value && el.value.trim() !== "") ? el.value : null; };
  const getInt = (id) => { const el = document.getElementById(id); if (el && el.value) return parseInt(el.value, 10); return null; };
  const getFloat = (id) => { const el = document.getElementById(id); if (el && el.value) return parseFloat(el.value.replace(',', '.')); return null; };
  const getObjId = (id) => { const val = getInt(id); return val ? { id: val } : null; };

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
    naoModificarPlano: getString("naoModificarPlano"),
    aversaoAlimentar: getString("aversaoAlimentar"),
    toleraProteinaAnimal: getString("toleraProteinaAnimal"),
    alergias: getString("alergias"),
    metas: getString("metas"),
    numPessoasDomicilio: getInt("numPessoasDomicilio"),
    notaSaciedade: getInt("notaSaciedade"),
    notaHumor: getInt("notaHumor"),
    dadosFisiologicos: {
        peso: getFloat("peso"),
        estatura: getFloat("estatura"),
        imc: getFloat("imc"),
        circunferenciaCintura: getFloat("circunferenciaCintura")
    },
    refeicoes: [],
    alimentos: []
  };
  document.querySelectorAll('input[name="refeicoes"]:checked').forEach((check) => { data.refeicoes.push({ id: parseInt(check.value) }); });
  document.querySelectorAll('.food-row').forEach(row => {
      const id = row.getAttribute('data-id');
      const radio = row.querySelector(`input[name="f_${id}"]:checked`);
      if (radio) { data.alimentos.push({ alimento: { id: parseInt(id) }, frequencia: radio.value }); }
  });
  return data;
}

function listarPacientesSelect(select) {
    fetch(urlApi + endpointPacientes, { headers: { "Authorization": `${token}` } })
      .then(r => r.json())
      .then(data => {
        data.forEach(p => {
          const opt = document.createElement('option');
          opt.value = p.id;
          opt.textContent = p.nome;
          select.appendChild(opt);
        });
        consultarAnamnese(); // Chama consulta após popular
      });
}

if(botaoDeletar) {
    botaoDeletar.addEventListener("click", async () => {
        try { await deletarItem(anamneseId, endpointAnamneses); window.location.href = "formularios.html"; } catch { }
    });
}

verificarAutenticacao();
listarPacientesSelect(document.getElementById("pacienteId"));
showTab(currentTab);
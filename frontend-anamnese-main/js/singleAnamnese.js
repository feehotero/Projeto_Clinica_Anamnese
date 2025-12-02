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
    fetch(urlApi + endpointAnamneses + "/" + anamneseId, { headers: { "Authorization": `${token}` } })
    .then(r => r.json())
    .then(a => {
        if(pacienteNome && a.paciente) pacienteNome.textContent = a.paciente.nome;
        if(criadoPor && a.usuario) criadoPor.textContent = a.usuario.nome;
        if(botaoCriarRetorno) botaoCriarRetorno.href = `./adicionar-retorno.html?id=${a.id}`;

        const set = (id, v) => { const el=document.getElementById(id); if(el) el.value = (v===null||v===undefined)?"":v; };

        set("pacienteId", a.paciente?.id);
        set("escolaridade", a.escolaridade?.id);
        set("profissao", a.profissao?.id);
        set("rendaFamiliar", a.rendaFamiliar?.id);
        set("evacuacao", a.evacuacao?.id);
        set("consistenciaEvacuacao", a.consistenciaEvacuacao);

        set("motivo", a.motivo);
        set("doenca", a.doenca);
        set("antecedentes", a.antecedentes);
        set("medicamento", a.medicamento);
        set("suplemento", a.suplemento);
        set("periodoEstudo", a.periodoEstudo);
        set("periodoTrabalho", a.periodoTrabalho);
        set("numPessoasDomicilio", a.numPessoasDomicilio);
        
        set("quemCozinha", a.quemCozinha);
        set("necessidadeComerEmocional", a.necessidadeComerEmocional);
        set("companhiaRefeicoes", a.companhiaRefeicoes);
        set("fomeFisiologica", a.fomeFisiologica);
        set("necessidadeEmocionalComer", a.necessidadeEmocionalComer);
        set("naoModificarPlano", a.naoModificarPlano);
        set("aversaoAlimentar", a.aversaoAlimentar);
        set("toleraProteinaAnimal", a.toleraProteinaAnimal);
        set("alergias", a.alergias);
        set("notaSaciedade", a.notaSaciedade);
        set("notaHumor", a.notaHumor);
        set("metas", a.metas);

        if(a.dadosFisiologicos) {
            set("peso", a.dadosFisiologicos.peso);
            set("estatura", a.dadosFisiologicos.estatura);
            set("imc", a.dadosFisiologicos.imc);
            set("circunferenciaCintura", a.dadosFisiologicos.circunferenciaCintura);
            set("somatoria4Dobras", a.dadosFisiologicos.somatoria4Dobras);
            set("pesoGordura", a.dadosFisiologicos.pesoGordura);
        }

        if(a.refeicoes) a.refeicoes.forEach(r => { const ck=document.querySelector(`input[name="refeicoes"][value="${r.id}"]`); if(ck) ck.checked=true; });
        if(a.alimentos) a.alimentos.forEach(item => { const rd=document.querySelector(`input[name="f_${item.alimento.id}"][value="${item.frequencia}"]`); if(rd) rd.checked=true; });
    });
}

function atualizarAnamnese() {
    const data = getData();
    return new Promise((resolve, reject) => {
        fetch(urlApi + endpointAnamneses + "/" + anamneseId, {
            headers: { "Content-Type": "application/json", "Authorization": `${token}` },
            method: "PUT",
            body: JSON.stringify(data)
        }).then(r => { if(!r.ok) return Promise.reject(); goodWarning.textContent="Atualizado!"; resolve(r); }).catch(reject);
    });
}

function getData() {
  // Cópia exata da função do addAnamnese.js
  const getString = (id) => { const el = document.getElementById(id); return (el && el.value.trim() !== "") ? el.value.trim() : null; };
  const getInt = (id) => { const el = document.getElementById(id); if(!el || el.value.trim() === "") return null; const v = parseInt(el.value, 10); return isNaN(v) ? null : v; };
  const getFloat = (id) => { const el = document.getElementById(id); if(!el || el.value.trim() === "") return null; const v = parseFloat(el.value.replace(',', '.')); return isNaN(v) ? null : v; };
  const getObjId = (id) => { const v = getInt(id); return v ? { id: v } : null; };
  const uid = localStorage.getItem("usuarioId");
  const data = {
    usuario: { id: parseInt(uid) },
    paciente: { id: getInt("pacienteId") },
    escolaridade: getObjId("escolaridade"),
    profissao: getObjId("profissao"),
    rendaFamiliar: getObjId("rendaFamiliar"),
    evacuacao: getObjId("evacuacao"),
    motivo: getString("motivo"), doenca: getString("doenca"), antecedentes: getString("antecedentes"), medicamento: getString("medicamento"), suplemento: getString("suplemento"), periodoEstudo: getString("periodoEstudo"), periodoTrabalho: getString("periodoTrabalho"), quemCozinha: getString("quemCozinha"), necessidadeComerEmocional: getString("necessidadeComerEmocional"), companhiaRefeicoes: getString("companhiaRefeicoes"), fomeFisiologica: getString("fomeFisiologica"), necessidadeEmocionalComer: getString("necessidadeEmocionalComer"), naoModificarPlano: getString("naoModificarPlano"), aversaoAlimentar: getString("aversaoAlimentar"), toleraProteinaAnimal: getString("toleraProteinaAnimal"), alergias: getString("alergias"), metas: getString("metas"),
    numPessoasDomicilio: getInt("numPessoasDomicilio"), notaSaciedade: getInt("notaSaciedade"), notaHumor: getInt("notaHumor"), consistenciaEvacuacao: getInt("consistenciaEvacuacao"),
    dadosFisiologicos: { peso: getFloat("peso"), estatura: getFloat("estatura"), imc: getFloat("imc"), circunferenciaCintura: getFloat("circunferenciaCintura"), somatoria4Dobras: getFloat("somatoria4Dobras"), pesoGordura: getFloat("pesoGordura") },
    refeicoes: [], alimentos: []
  };
  document.querySelectorAll('input[name="refeicoes"]:checked').forEach(ck => data.refeicoes.push({ id: parseInt(ck.value) }));
  document.querySelectorAll('.food-row').forEach(row => {
      const id = row.getAttribute('data-id');
      const rad = row.querySelector(`input[name="f_${id}"]:checked`);
      if(rad) data.alimentos.push({ alimento: { id: parseInt(id) }, frequencia: rad.value });
  });
  return data;
}

if(botaoDeletar) botaoDeletar.addEventListener("click", async () => { try { await deletarItem(anamneseId, endpointAnamneses); window.location.href = "formularios.html"; } catch {} });
function listarPacientesSelect(select) {
    fetch(urlApi + endpointPacientes, { headers: { "Authorization": `${token}` } }).then(r => r.json()).then(d => {
        d.forEach(p => { const o = document.createElement('option'); o.value = p.id; o.textContent = p.nome; select.appendChild(o); });
        consultarAnamnese();
    });
}
verificarAutenticacao();
if(document.getElementById("pacienteId")) listarPacientesSelect(document.getElementById("pacienteId"));
showTab(currentTab);
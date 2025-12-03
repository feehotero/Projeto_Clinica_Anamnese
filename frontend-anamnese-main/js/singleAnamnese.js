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
        // Cabeçalho
        if(pacienteNome && a.paciente) pacienteNome.textContent = a.paciente.nome;
        if(criadoPor && a.usuario) criadoPor.textContent = a.usuario.nome;
        if(botaoCriarRetorno) botaoCriarRetorno.href = `./adicionar-retorno.html?id=${a.id}`;

        // Funções auxiliares
        const set = (id, v) => { 
            const el = document.getElementById(id); 
            if(el) el.value = (v === null || v === undefined) ? "" : v; 
        };
        
        const setRadio = (name, v) => {
            if (v !== null && v !== undefined) {
                const el = document.querySelector(`input[name="${name}"][value="${v}"]`);
                if (el) el.checked = true;
            }
        };

        const setCheck = (id, v) => {
            const el = document.getElementById(id);
            if (el) el.checked = !!v;
        };

        // --- Mapeamento dos Dados Pessoais ---
        set("pacienteId", a.paciente?.id);
        set("escolaridade", a.escolaridade?.id);
        
        // No HTML 'profissao' é um input text, mas no backend é objeto. 
        // Tentamos preencher com a descrição se existir
        set("profissao", a.profissao?.descricao || ""); 
        
        set("periodoEstudo", a.periodoEstudo);
        set("periodoTrabalho", a.periodoTrabalho);
        // Nota: 'lancheEstudo' e 'lancheTrabalho' não existem no seu Backend (Anamnese.java), então não são recuperados.

        // --- Mapeamento Socioeconômico ---
        set("rendaFamiliar", a.rendaFamiliar?.id);
        set("numPessoasDomicilio", a.numPessoasDomicilio);

        // --- História Clínica ---
        set("motivo", a.motivo);
        set("apresentaDoenca", a.doenca); // ID HTML: apresentaDoenca -> Backend: doenca
        set("antecedentesFamiliares", a.antecedentes); // ID HTML: antecedentesFamiliares -> Backend: antecedentes
        set("medicamentosContinuos", a.medicamento); // ID HTML: medicamentosContinuos -> Backend: medicamento
        set("suplementosComplementos", a.suplemento); // ID HTML: suplementosComplementos -> Backend: suplemento
        
        set("frequenciaEvacuacao", a.evacuacao?.id); // ID HTML: frequenciaEvacuacao -> Backend: evacuacao object
        set("consistenciaEvacuacao", a.consistenciaEvacuacao);
        
        // 'praticaAtvFisica' e 'atvFisica' não existem no seu Backend (Anamnese.java)

        // --- Comportamento Alimentar ---
        set("quemCozinha", a.quemCozinha);
        set("necessidadeComerEstressadoAnsiosoTriste", a.necessidadeComerEmocional); // ID HTML diferente do Backend
        set("realizaRefeicoesSozinhoAcompanhado", a.companhiaRefeicoes); // ID HTML diferente do Backend
        
        set("frequenciaFomeFisiologica", a.fomeFisiologica); // ID HTML: frequenciaFomeFisiologica
        set("frequenciaNecessidadeEmocionalComer", a.necessidadeEmocionalComer); // ID HTML: frequenciaNecessidadeEmocionalComer
        
        set("naoModificarPlanoAlimentar", a.naoModificarPlano); // ID HTML: naoModificarPlanoAlimentar
        set("aversaoAlimentar", a.aversaoAlimentar);
        set("toleraAlimentosProteinaAnimal", a.toleraProteinaAnimal); // ID HTML: toleraAlimentosProteinaAnimal
        set("alergiaIntoleranciasAlimentares", a.alergias); // ID HTML: alergiaIntoleranciasAlimentares
        
        // Radios de Notas (Saciedade e Humor)
        setRadio("notaSaciedadePosRefeicoes", a.notaSaciedade);
        setRadio("notaHumorPosRefeicoes", a.notaHumor);

        set("metas", a.metas);

        // --- Dados Fisiológicos (Antropometria) ---
        if(a.dadosFisiologicos) {
            set("pesoAtual", a.dadosFisiologicos.peso); // ID HTML: pesoAtual -> Backend: peso
            set("estatura", a.dadosFisiologicos.estatura);
            set("imc", a.dadosFisiologicos.imc);
            set("circunferenciaCintura", a.dadosFisiologicos.circunferenciaCintura);
            set("somatoria4Dobras", a.dadosFisiologicos.somatoria4Dobras);
            set("porcentagemGorduraCorporalSomatoria4Dobras", a.dadosFisiologicos.percentualGorduraCalculado); // ID HTML longo
            set("pesoGordura", a.dadosFisiologicos.pesoGordura);
            set("pesoMassaMagra", a.dadosFisiologicos.pesoMassaMagra);
            set("totalAgua", a.dadosFisiologicos.totalAgua);
            set("porcentagemAguaMassaMagra", a.dadosFisiologicos.porcentagemAguaMassaMagra);
            set("resistencia", a.dadosFisiologicos.resistencia);
            set("reactancia", a.dadosFisiologicos.reactancia);
            set("anguloDeFase", a.dadosFisiologicos.anguloDeFase);
            set("circunferenciaQuadril", a.dadosFisiologicos.circunferenciaQuadril);
            set("circunferenciaPanturrilha", a.dadosFisiologicos.circunferenciaPanturrilha);
            set("emapDireita", a.dadosFisiologicos.emapDireita);
            set("emapEsquerda", a.dadosFisiologicos.emapEsquerda);
            set("forcaPreencaoManualDireita", a.dadosFisiologicos.forcaPreencaoManualDireita);
            set("forcaPreencaoManualEsquerda", a.dadosFisiologicos.forcaPreencaoManualEsquerda);
            
            // Outros campos de dobras (cb, dct, dcb...) também podem ser mapeados se existirem no HTML com esses IDs
            set("cb", a.dadosFisiologicos.cb);
            set("dct", a.dadosFisiologicos.dct);
            set("dcb", a.dadosFisiologicos.dcb);
            set("dcse", a.dadosFisiologicos.dcse);
            set("dcsi", a.dadosFisiologicos.dcsi);
        }

        // --- ATENÇÃO: Alimentos e Refeições ---
        // O HTML 'anamnese.html' usa inputs do tipo TEXTO para refeições (ex: 'cafeDaManha'),
        // mas o Backend só salva checkboxes de ID de refeição. Os textos não são salvos.
        // Se quiser ver os alimentos marcados (checkboxes/radios), o HTML precisaria ter a mesma estrutura do 'adicionar-anamnese.html'.
        
        // Lógica parcial para preencher alimentos se os IDs existissem no HTML (provavelmente não funcionarão no anamnese.html atual):
        if(a.alimentos) {
             a.alimentos.forEach(item => { 
                 // Tenta encontrar radio buttons com nomes baseados em ID (estilo adicionar-anamnese)
                 // Se o seu anamnese.html usa nomes fixos como 'legumesCenoura', essa lógica genérica não funciona sem um mapa manual.
                 const rd = document.querySelector(`input[name="f_${item.alimento.id}"][value="${item.frequencia}"]`); 
                 if(rd) rd.checked=true; 
             });
        }
    });
}

// O restante das funções (atualizarAnamnese, getData, etc.) também precisa ser ajustado para pegar os IDs corretos
// para que o botão "Enviar" funcione nessa tela.

function getData() {
  const getString = (id) => { const el = document.getElementById(id); return (el && el.value.trim() !== "") ? el.value.trim() : null; };
  const getInt = (id) => { const el = document.getElementById(id); if(!el || el.value.trim() === "") return null; const v = parseInt(el.value, 10); return isNaN(v) ? null : v; };
  const getFloat = (id) => { const el = document.getElementById(id); if(!el || el.value.trim() === "") return null; const v = parseFloat(el.value.replace(',', '.')); return isNaN(v) ? null : v; };
  const getObjId = (id) => { const v = getInt(id); return v ? { id: v } : null; };
  
  const uid = localStorage.getItem("usuarioId");
  
  const data = {
    usuario: { id: parseInt(uid) },
    paciente: { id: getInt("pacienteId") },
    escolaridade: getObjId("escolaridade"),
    // Profissao no HTML é texto, mas backend espera objeto ID. Isso pode causar erro ao salvar se não for tratado.
    // Sugestão: usar select como no adicionar. Se for texto, o backend teria que mudar.
    // Vou manter null para evitar erro 500 se for texto
    profissao: null, 
    rendaFamiliar: getObjId("rendaFamiliar"),
    evacuacao: getObjId("frequenciaEvacuacao"), // ID corrigido
    
    motivo: getString("motivo"), 
    doenca: getString("apresentaDoenca"), // ID corrigido
    antecedentes: getString("antecedentesFamiliares"), // ID corrigido
    medicamento: getString("medicamentosContinuos"), // ID corrigido
    suplemento: getString("suplementosComplementos"), // ID corrigido
    
    periodoEstudo: getString("periodoEstudo"), 
    periodoTrabalho: getString("periodoTrabalho"), 
    quemCozinha: getString("quemCozinha"), 
    
    necessidadeComerEmocional: getString("necessidadeComerEstressadoAnsiosoTriste"), // ID corrigido
    companhiaRefeicoes: getString("realizaRefeicoesSozinhoAcompanhado"), // ID corrigido
    fomeFisiologica: getString("frequenciaFomeFisiologica"), // ID corrigido
    necessidadeEmocionalComer: getString("frequenciaNecessidadeEmocionalComer"), // ID corrigido
    naoModificarPlano: getString("naoModificarPlanoAlimentar"), // ID corrigido
    aversaoAlimentar: getString("aversaoAlimentar"), 
    toleraProteinaAnimal: getString("toleraAlimentosProteinaAnimal"), // ID corrigido
    alergias: getString("alergiaIntoleranciasAlimentares"), // ID corrigido
    metas: getString("metas"),
    numPessoasDomicilio: getInt("numPessoasDomicilio"), 
    
    // Radios
    notaSaciedade: parseInt(document.querySelector('input[name="notaSaciedadePosRefeicoes"]:checked')?.value || 0),
    notaHumor: parseInt(document.querySelector('input[name="notaHumorPosRefeicoes"]:checked')?.value || 0),
    
    consistenciaEvacuacao: getInt("consistenciaEvacuacao"),
    
    dadosFisiologicos: { 
        peso: getFloat("pesoAtual"), // ID corrigido
        estatura: getFloat("estatura"), 
        imc: getFloat("imc"), 
        circunferenciaCintura: getFloat("circunferenciaCintura"), 
        somatoria4Dobras: getFloat("somatoria4Dobras"), 
        percentualGorduraCalculado: getFloat("porcentagemGorduraCorporalSomatoria4Dobras"), // Mapeando
        pesoGordura: getFloat("pesoGordura"),
        pesoMassaMagra: getFloat("pesoMassaMagra"),
        totalAgua: getFloat("totalAgua"),
        porcentagemAguaMassaMagra: getFloat("porcentagemAguaMassaMagra"),
        resistencia: getFloat("resistencia"),
        reactancia: getFloat("reactancia"),
        anguloDeFase: getFloat("anguloDeFase"),
        circunferenciaQuadril: getFloat("circunferenciaQuadril"),
        circunferenciaPanturrilha: getFloat("circunferenciaPanturrilha"),
        emapDireita: getFloat("emapDireita"),
        emapEsquerda: getFloat("emapEsquerda"),
        forcaPreencaoManualDireita: getFloat("forcaPreencaoManualDireita"),
        forcaPreencaoManualEsquerda: getFloat("forcaPreencaoManualEsquerda"),
        cb: getFloat("cb"),
        dct: getFloat("dct"),
        dcb: getFloat("dcb"),
        dcse: getFloat("dcse"),
        dcsi: getFloat("dcsi")
    },
    refeicoes: [], 
    alimentos: []
  };
  
  // A lógica de salvar alimentos/refeições aqui dependeria do HTML ter checkboxes com IDs compatíveis
  return data;
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

if(botaoDeletar) botaoDeletar.addEventListener("click", async () => { try { await deletarItem(anamneseId, endpointAnamneses); window.location.href = "formularios.html"; } catch {} });

function listarPacientesSelect(select) {
    fetch(urlApi + endpointPacientes, { headers: { "Authorization": `${token}` } }).then(r => r.json()).then(d => {
        d.forEach(p => { const o = document.createElement('option'); o.value = p.id; o.textContent = p.nome; select.appendChild(o); });
        // Importante: Chama a consulta APÓS carregar os pacientes para garantir que o select esteja pronto
        consultarAnamnese();
    });
}

verificarAutenticacao();
if(document.getElementById("pacienteId")) listarPacientesSelect(document.getElementById("pacienteId"));
showTab(currentTab);
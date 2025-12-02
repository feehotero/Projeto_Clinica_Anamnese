const formAddAnamnese = document.querySelector(".formAddAnamnese");
const nextBtn = document.getElementById("nextBtn");
const prevBtn = document.getElementById("prevBtn");
const tabs = document.getElementsByClassName("tab");
const fPacienteSelect = document.getElementById("pacienteId");
let currentTab = 0;

function cadastrarAnamnese() {
  const data = getData();
  let forbidden = false;
  
  // Feedback visual
  const originalBtnText = nextBtn.textContent;
  nextBtn.disabled = true;
  nextBtn.textContent = "Enviando...";

  return new Promise((resolve, reject) => {
    // Validação obrigatória apenas para o Paciente
    if (!data.paciente.id) {
        badWarning.textContent = "Erro: Selecione um paciente na primeira etapa.";
        nextBtn.disabled = false;
        nextBtn.textContent = originalBtnText;
        reject("Paciente não selecionado");
        return;
    }

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
        // Tenta pegar o corpo do erro para debug
        return response.text().then(text => {
            throw new Error(text || response.statusText);
        });
      }
      return response.json();
    })
    .then(data => {
      goodWarning.textContent = "Anamnese salva com sucesso!";
      badWarning.textContent = "";
      setTimeout(() => {
          window.location.href = "formularios.html";
      }, 1500);
      resolve(data);
    })
    .catch(error => {
      nextBtn.disabled = false;
      nextBtn.textContent = originalBtnText;
      console.error("Erro detalhado:", error);
      
      if (forbidden) {
        badWarning.textContent = "Dados inválidos. Verifique se preencheu campos numéricos com letras ou formatos errados.";
      } else {
        badWarning.textContent = "Erro de conexão com o servidor.";
      }
      reject(error);
    })
  })
}

function getData() {
  // --- FUNÇÕES AUXILIARES PARA LIMPEZA DE DADOS ---
  
  // Para textos: Retorna null se vazio
  const getString = (id) => {
      const el = document.getElementById(id);
      if (!el || !el.value) return null;
      return el.value.trim() === "" ? null : el.value.trim();
  };

  // Para Inteiros: Retorna null se vazio ou inválido
  const getInt = (id) => {
      const el = document.getElementById(id);
      if (!el || !el.value || el.value.trim() === "") return null;
      const val = parseInt(el.value, 10);
      return isNaN(val) ? null : val;
  };

  // Para Floats: Retorna null se vazio ou inválido
  const getFloat = (id) => {
      const el = document.getElementById(id);
      if (!el || !el.value || el.value.trim() === "") return null;
      // Substitui vírgula por ponto caso o usuário digite
      const val = parseFloat(el.value.replace(',', '.')); 
      return isNaN(val) ? null : val;
  };

  // Para Selects de Objetos (FKs): Retorna {id: X} ou null
  const getObjId = (id) => {
      const val = getInt(id);
      return val ? { id: val } : null;
  };

  const usuarioIdLocal = localStorage.getItem("usuarioId");

  // --- MONTAGEM DO JSON ---
  const data = {
    usuario: { id: parseInt(usuarioIdLocal) },
    paciente: { id: getInt("pacienteId") },
    
    // Objetos (Foreign Keys)
    escolaridade: getObjId("escolaridade"),
    profissao: getObjId("profissao"),
    rendaFamiliar: getObjId("rendaFamiliar"),
    evacuacao: getObjId("evacuacao"),

    // Strings e Enums
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

    // Inteiros
    numPessoasDomicilio: getInt("numPessoasDomicilio"),
    notaSaciedade: getInt("notaSaciedade"),
    notaHumor: getInt("notaHumor"),

    // Dados Fisiológicos (Floats)
    dadosFisiologicos: {
        peso: getFloat("peso"),
        estatura: getFloat("estatura"),
        imc: getFloat("imc"),
        circunferenciaCintura: getFloat("circunferenciaCintura")
    },

    // Listas
    refeicoes: [],
    alimentos: []
  };

  // Coletar Refeições
  document.querySelectorAll('input[name="refeicoes"]:checked').forEach((check) => {
      data.refeicoes.push({ id: parseInt(check.value) });
  });

  // Coletar Frequência Alimentar
  document.querySelectorAll('.food-row').forEach(row => {
      const id = row.getAttribute('data-id');
      const radio = row.querySelector(`input[name="f_${id}"]:checked`);
      
      if (radio) {
          data.alimentos.push({
              alimento: { id: parseInt(id) },
              frequencia: radio.value
          });
      }
  });

  console.log("JSON Enviado:", data); // Debug no console
  return data;
}

function listarPacientesSelect(select) {
    fetch(urlApi + endpointPacientes, { headers: { "Authorization": `${token}` } })
      .then(r => r.json())
      .then(data => {
        // Limpa options anteriores
        select.innerHTML = '<option selected disabled value="">Selecione um paciente...</option>';
        data.forEach(p => {
          const opt = document.createElement('option');
          opt.value = p.id;
          opt.textContent = p.nome;
          select.appendChild(opt);
        });
      })
      .catch(console.error);
}

verificarAutenticacao();
if(fPacienteSelect) listarPacientesSelect(fPacienteSelect);
showTab(currentTab);
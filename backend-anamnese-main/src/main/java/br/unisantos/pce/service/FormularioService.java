package br.unisantos.pce.service;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.unisantos.pce.model.Anamnese;
import br.unisantos.pce.model.Refeicao;
import br.unisantos.pce.model.Retorno;

@Service
public class FormularioService {

    private final AnamneseService anamneseService;
    private final RetornoService retornoService;

    @Autowired
    public FormularioService(AnamneseService anamneseService, RetornoService retornoService) {
        this.anamneseService = anamneseService;
        this.retornoService = retornoService;
    }

    public void exportAnamneseToCSV(Writer writer) throws IOException {
        List<Anamnese> anamneses = new ArrayList<>(anamneseService.listarAnamneses());

        try (CSVPrinter printer = new CSVPrinter(writer,
                CSVFormat.DEFAULT.builder().setHeader(
                        "ID",
                        "Paciente",
                        "Usuário",
                        "Data Criação",
                        "Motivo",
                        "Doença",
                        "Antecedentes",
                        "Medicamentos",
                        "Suplementos",
                        "Escolaridade",
                        "Profissão",
                        "Renda Familiar",
                        "Pessoas Domicílio",
                        "Período Estudo",
                        "Período Trabalho",
                        "Quem Cozinha",
                        "Comer Emocional",
                        "Refeições Realizadas",
                        "Frequência Alimentar",
                        "Peso (kg)",
                        "Estatura (m)",
                        "IMC",
                        "Circ. Cintura (cm)",
                        "Metas").build())) {

            for (Anamnese anamnese : anamneses) {
                // 1. Dados de Relacionamentos (Null Safety)
                String escolaridade = anamnese.getEscolaridade() != null ? anamnese.getEscolaridade().getDescricao()
                        : "";

                // ALTERAÇÃO AQUI: Profissão agora é String direta
                String profissao = anamnese.getProfissao() != null ? anamnese.getProfissao() : "";

                String renda = anamnese.getRendaFamiliar() != null ? anamnese.getRendaFamiliar().getDescricao() : "";

                // 2. Dados Fisiológicos
                Float peso = null;
                Float estatura = null;
                Float imc = null;
                Float cintura = null;

                if (anamnese.getDadosFisiologicos() != null) {
                    peso = anamnese.getDadosFisiologicos().getPeso();
                    estatura = anamnese.getDadosFisiologicos().getEstatura();
                    imc = anamnese.getDadosFisiologicos().getImc();
                    cintura = anamnese.getDadosFisiologicos().getCircunferenciaCintura();
                }

                // 3. Lista de Refeições
                String refeicoesStr = "";
                if (anamnese.getRefeicoes() != null && !anamnese.getRefeicoes().isEmpty()) {
                    refeicoesStr = anamnese.getRefeicoes().stream()
                            .map(Refeicao::getDescricao)
                            .collect(Collectors.joining(", "));
                }

                // 4. Lista de Alimentos
                String alimentosStr = "";
                if (anamnese.getAlimentos() != null && !anamnese.getAlimentos().isEmpty()) {
                    alimentosStr = anamnese.getAlimentos().stream()
                            .map(item -> item.getAlimento().getDescricao() + ": " + item.getFrequencia())
                            .collect(Collectors.joining("; "));
                }

                printer.printRecord(
                        anamnese.getId(),
                        anamnese.getPaciente() != null ? anamnese.getPaciente().getNome() : "",
                        anamnese.getUsuario() != null ? anamnese.getUsuario().getNome() : "",
                        anamnese.getCriadoEm(),
                        anamnese.getMotivo(),
                        anamnese.getDoenca(),
                        anamnese.getAntecedentes(),
                        anamnese.getMedicamento(),
                        anamnese.getSuplemento(),
                        escolaridade,
                        profissao,
                        renda,
                        anamnese.getNumPessoasDomicilio(),
                        anamnese.getPeriodoEstudo(),
                        anamnese.getPeriodoTrabalho(),
                        anamnese.getQuemCozinha(),
                        anamnese.getNecessidadeComerEmocional(),
                        refeicoesStr,
                        alimentosStr,
                        peso,
                        estatura,
                        imc,
                        cintura,
                        anamnese.getMetas());
            }
        } catch (IOException e) {
            throw new IOException("Erro ao gerar o CSV de Anamnese", e);
        }
    }

    public void exportRetornoToCSV(Writer writer) throws IOException {
        List<Retorno> retornos = new ArrayList<>(retornoService.listarRetornos());

        try (CSVPrinter printer = new CSVPrinter(writer,
                CSVFormat.DEFAULT.builder().setHeader(
                        "ID",
                        "Paciente",
                        "Usuário",
                        "Anamnese Ref.",
                        "Data Criação",
                        "Metas Última Consulta",
                        "Metas Cumpridas?",
                        "Desempenho (0-10)",
                        "Mudança de Hábitos",
                        "Adaptação",
                        "Dificuldades",
                        "Melhorar Alimentação",
                        "Atividade Física",
                        "Hábito Intestinal",
                        "Metas Próximo Retorno",
                        "Peso Atual",
                        "IMC",
                        "Circ. Abdominal",
                        "Bioimpedância",
                        "Observações").build())) {

            for (Retorno retorno : retornos) {
                printer.printRecord(
                        retorno.getId(),
                        retorno.getPaciente() != null ? retorno.getPaciente().getNome() : "",
                        retorno.getUsuario() != null ? retorno.getUsuario().getNome() : "",
                        retorno.getAnamnese() != null ? retorno.getAnamnese().getId() : "",
                        retorno.getCriadoEm(),
                        retorno.getMetasUltimasConsultas(),
                        retorno.getMetasForamCumpridas(),
                        retorno.getDesempenhoMetas(),
                        retorno.getSentiuMudancaHabitos(),
                        retorno.getAdaptacaoMudanca(),
                        retorno.getDificuldadeAdaptacao(),
                        retorno.getMelhorarAlimentacao(),
                        retorno.getAtividadeFisica(),
                        retorno.getHabitoIntestinal(),
                        retorno.getMetasProximoRetorno(),
                        retorno.getPeso(),
                        retorno.getImc(),
                        retorno.getCircunferenciaAbdominal(),
                        retorno.getValoresBioimpedancia(),
                        retorno.getObservacoesBioimpedancia());
            }
        } catch (IOException e) {
            throw new IOException("Erro ao gerar o CSV de Retorno", e);
        }
    }
}
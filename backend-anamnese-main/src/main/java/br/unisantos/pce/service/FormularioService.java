package br.unisantos.pce.service;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.unisantos.pce.model.Anamnese;
import br.unisantos.pce.model.Retorno;

@Service
public class FormularioService {
    private List<Anamnese> anamneses;
    private List<Retorno> retornos;
    private AnamneseService anamneseService;
    private RetornoService retornoService;

    @Autowired
    public FormularioService(AnamneseService anamneseService, RetornoService retornoService) {
        this.anamneseService = anamneseService;
        this.retornoService = retornoService;
    }

    public void exportAnamneseToCSV(Writer writer) throws IOException {
        this.anamneses = new ArrayList<Anamnese>(anamneseService.listarAnamneses());

        try (CSVPrinter printer = new CSVPrinter(writer,
                CSVFormat.DEFAULT.builder().setHeader(
                        "id",
                        "pacienteNome",
                        "usuarioNome",
                        "idEscolaridade",
                        "idProfissao",
                        "idRendaFamiliar",
                        "nmPeriodoEstudo",
                        "nmPeriodoTrabalho",
                        "nrPessoaDomicilio",
                        "dsMotivo",
                        "dsDoenca",
                        "dsAntecedentes",
                        "dsMedicamento",
                        "dsSuplemento",
                        "idEvacuacao",
                        "dsQuemCozinha",
                        "dsNecessidadeComerEstressadoAnsiosoTriste",
                        "dsRealizaRefeicoesSozinhoAcompanhado",
                        "dsFomeFisiologica",
                        "dsNecessidadeEmocionalComer",
                        "dsNaoModificarPlanoAlimentar",
                        "dsAversaoAlimentar",
                        "dsToleraAlimentosProteinaAnimal",
                        "dsAlergiaIntoleranciasAlimentares",
                        "nrNotaSaciedadePosRefeicoes",
                        "nrNotaHumorPosRefeicoes",
                        "dsMetas",
                        "criadoEm")
                        .build());) {

            for (Anamnese anamnese : anamneses) {
                printer.printRecord(
                        anamnese.getId(),
                        anamnese.getPacienteNome(),
                        anamnese.getUsuarioNome(),
                        anamnese.getIdEscolaridade(),
                        anamnese.getIdProfissao(),
                        anamnese.getIdRendaFamiliar(),
                        anamnese.getNmPeriodoEstudo(),
                        anamnese.getNmPeriodoTrabalho(),
                        anamnese.getNrPessoaDomicilio(),
                        anamnese.getDsMotivo(),
                        anamnese.getDsDoenca(),
                        anamnese.getDsAntecedentes(),
                        anamnese.getDsMedicamento(),
                        anamnese.getDsSuplemento(),
                        anamnese.getIdEvacuacao(),
                        anamnese.getDsQuemCozinha(),
                        anamnese.getDsNecessidadeComerEstressadoAnsiosoTriste(),
                        anamnese.getDsRealizaRefeicoesSozinhoAcompanhado(),
                        anamnese.getDsFomeFisiologica(),
                        anamnese.getDsNecessidadeEmocionalComer(),
                        anamnese.getDsNaoModificarPlanoAlimentar(),
                        anamnese.getDsAversaoAlimentar(),
                        anamnese.getDsToleraAlimentosProteinaAnimal(),
                        anamnese.getDsAlergiaIntoleranciasAlimentares(),
                        anamnese.getNrNotaSaciedadePosRefeicoes(),
                        anamnese.getNrNotaHumorPosRefeicoes(),
                        anamnese.getDsMetas(),
                        anamnese.getCriadoEm());
            }

        } catch (IOException e) {
            throw new IOException("Erro ao gerar o CSV", e);
        }
    }

    public void exportRetornoToCSV(Writer writer) throws IOException {
        this.retornos = new ArrayList<Retorno>(retornoService.listarRetornos());

        try (CSVPrinter printer = new CSVPrinter(writer,
                CSVFormat.DEFAULT.builder().setHeader(
                        "id",
                        "pacienteId",
                        "pacienteNome",
                        "usuarioId",
                        "usuarioNome",
                        "dsComentariosObservacao",
                        "dsMetasForamCumpridas",
                        "nrDesempenhoCumprimentoMetas",
                        "dsMotivoAssinaladoCumprimentoMetas",
                        "dsComoSentiuMudancaHabitos",
                        "dsAdaptacaoMudancaHabitos",
                        "dsMotivosDificuldadeAdaptacao",
                        "dsSentePrecisaMelhorarAlimentacao",
                        "dsHabitoIntestinal",
                        "dsAtividadeFisica",
                        "dsMetasProximoRetorno",
                        "nrPeso",
                        "nrImc",
                        "nrCircunferenciaAbdominal",
                        "dsValoresBioimpedancia",
                        "dsObservacoesBioimpedancia",
                        "criadoEm")
                        .build());) {

            for (Retorno retorno : retornos) {
                printer.printRecord(
                        retorno.getId(),
                        retorno.getPacienteId(),
                        retorno.getPacienteNome(),
                        retorno.getUsuarioId(),
                        retorno.getUsuarioNome(),
                        retorno.getDsComentariosObservacao(),
                        retorno.getDsMetasForamCumpridas(),
                        retorno.getNrDesempenhoCumprimentoMetas(),
                        retorno.getDsMotivoAssinaladoCumprimentoMetas(),
                        retorno.getDsComoSentiuMudancaHabitos(),
                        retorno.getDsAdaptacaoMudancaHabitos(),
                        retorno.getDsMotivosDificuldadeAdaptacao(),
                        retorno.getDsSentePrecisaMelhorarAlimentacao(),
                        retorno.getDsHabitoIntestinal(),
                        retorno.getDsAtividadeFisica(),
                        retorno.getDsMetasProximoRetorno(),
                        retorno.getNrPeso(),
                        retorno.getNrImc(),
                        retorno.getNrCircunferenciaAbdominal(),
                        retorno.getDsValoresBioimpedancia(),
                        retorno.getDsObservacoesBioimpedancia(),
                        retorno.getCriadoEm());
            }

        } catch (IOException e) {
            throw new IOException("Erro ao gerar o CSV", e);
        }
    }
}
package br.unisantos.pce.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.unisantos.pce.model.Anamnese;
import br.unisantos.pce.model.Retorno;
import br.unisantos.pce.service.AnamneseService;
import br.unisantos.pce.service.FormularioService;
import br.unisantos.pce.service.RetornoService;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@CrossOrigin("*")
@RequestMapping(value = "/formularios", produces = MediaType.APPLICATION_JSON_VALUE)
public class FormularioController {

    private final AnamneseService anamneseService;
    private final RetornoService retornoService;
    private final FormularioService formularioService;

    @Autowired
    public FormularioController(AnamneseService anamneseService, RetornoService retornoService,
            FormularioService formularioService) {
        this.anamneseService = anamneseService;
        this.retornoService = retornoService;
        this.formularioService = formularioService;
    }

    // Retornando anamneses e retornos ordenados pela data de criacao
    @GetMapping
    public ResponseEntity<List<Object>> listarFormularios(
        @RequestParam(defaultValue = "true") boolean retornoAgrupado,
        @RequestParam(defaultValue = "") String nome,
        @RequestParam(defaultValue = "todos") String tipo,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate criadoEmInicio,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate criadoEmTermino
    )  {
        List<Object> formularios = new ArrayList<>();
        
        if(retornoAgrupado) {

            if(tipo.equals("retorno")) {
                formularios.addAll(retornoService.listarRetornosByPacienteNome(nome));
            } else {
                List<Anamnese> anamneses = anamneseService.listarAnamnesesByPacienteNome(nome);
                for (var anamnese : anamneses) {
                    anamnese.setRetornos(retornoService.listarRetornosByAnamneseId(anamnese.getId()));
                }

                formularios.addAll(anamneses);
            }
        } else {

            if(tipo.equals("anamnese")) {
                formularios.addAll(anamneseService.listarAnamnesesByPacienteNome(nome));
            } else if(tipo.equals("retorno")) {
                formularios.addAll(retornoService.listarRetornosByPacienteNome(nome));
            } else {
                formularios.addAll(anamneseService.listarAnamnesesByPacienteNome(nome));
                formularios.addAll(retornoService.listarRetornosByPacienteNome(nome));
            }

            formularios.sort(Comparator.comparing(formulario -> {
                if (formulario instanceof Anamnese) {
                    return ((Anamnese) formulario).getCriadoEm();
                } else if (formulario instanceof Retorno) {
                    return ((Retorno) formulario).getCriadoEm();
                } else {
                    return LocalDateTime.MIN;
                }
            }).reversed());
        }

        if (criadoEmInicio != null || criadoEmTermino != null) {
            LocalDateTime inicio = criadoEmInicio != null ? criadoEmInicio.atStartOfDay() : LocalDateTime.MIN;
            LocalDateTime termino = criadoEmTermino != null ? criadoEmTermino.atTime(LocalTime.MAX) : LocalDateTime.MAX;
    
            formularios = formularios.stream()
                .filter(f -> {
                    LocalDateTime criadoEm;
                    if (f instanceof Anamnese) {
                        criadoEm = ((Anamnese) f).getCriadoEm();
                    } else if (f instanceof Retorno) {
                        criadoEm = ((Retorno) f).getCriadoEm();
                    } else {
                        return false;
                    }
                    return (criadoEm != null && !criadoEm.isBefore(inicio) && !criadoEm.isAfter(termino));
                })
                .collect(Collectors.toList());
        }

        return ResponseEntity.ok(formularios);
    }

    @GetMapping("/pacientes/{id}")
    public ResponseEntity<List<Anamnese>> listarFormulariosDoPaciente(@PathVariable Integer id) {
        List<Anamnese> anamneses = anamneseService.listarAnamnesesByPacienteId(id);
        
        for (var anamnese : anamneses) {
            anamnese.setRetornos(retornoService.listarRetornosByAnamneseId(anamnese.getId()));
        }

        return ResponseEntity.ok(anamneses);
    }

    @GetMapping("/usuarios/{id}")
    public ResponseEntity<List<Object>> listarFormulariosDoUsuario(@PathVariable Integer id) {
        List<Object> formularios = new ArrayList<>(anamneseService.listarAnamneses());
        formularios.addAll(retornoService.listarRetornos());

        List<Object> formulariosDoUsuario = formularios.stream()
                .filter(formulario -> {
                    if (formulario instanceof Anamnese) {
                        return ((Anamnese) formulario).getUsuarioId().equals(id);
                    } else if (formulario instanceof Retorno) {
                        return ((Retorno) formulario).getUsuarioId().equals(id);
                    } else {
                        return false;
                    }
                })
                .sorted(Comparator.comparing(formulario -> {
                    if (formulario instanceof Anamnese) {
                        return ((Anamnese) formulario).getCriadoEm();
                    } else if (formulario instanceof Retorno) {
                        return ((Retorno) formulario).getCriadoEm();
                    } else {
                        return LocalDateTime.MIN;
                    }
                }).reversed())
                .collect(Collectors.toList());

        return ResponseEntity.ok(formulariosDoUsuario);
    }

    @GetMapping("/export-anamnese")
    public void exportAnamnese(HttpServletResponse servletResponse) {
        servletResponse.setContentType("text/csv");
        servletResponse.addHeader("Content-Disposition", "attachment; filename=anamneses.csv");

        try {
            formularioService.exportAnamneseToCSV(servletResponse.getWriter());
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    @GetMapping("/export-retorno")
    public void exportRetorno(HttpServletResponse servletResponse) {
        servletResponse.setContentType("text/csv");
        servletResponse.addHeader("Content-Disposition", "attachment; filename=anamneses.csv");

        try {
            formularioService.exportRetornoToCSV(servletResponse.getWriter());
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

}

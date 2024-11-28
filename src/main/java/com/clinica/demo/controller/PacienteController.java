package com.clinica.demo.controller;

import com.clinica.demo.model.Paciente;
import com.clinica.demo.service.PacienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pacientes")
public class PacienteController {

    @Autowired
    private PacienteService pacienteService;

    @GetMapping
    public List<Paciente> listarPacientes() {
        return pacienteService.listarPacientes();
    }

    @PostMapping
    public Paciente adicionarPaciente(@RequestBody Paciente paciente) {
        return pacienteService.adicionarPaciente(paciente);
    }

    @GetMapping("/fila-espera")
    public Map<String, String> obterDadosFilaEspera() {
        String ultimaEmergencia = pacienteService.obterUltimoPacientePorTipo("emergencia");
        String ultimaConsulta = pacienteService.obterUltimoPacientePorTipo("consulta");
        String ultimaColeta = pacienteService.obterUltimoPacientePorTipo("coleta");
        String numeroAtual = pacienteService.obterProximoPaciente();

        Map<String, String> dados = new HashMap<>();
        dados.put("ultimaEmergencia", ultimaEmergencia);
        dados.put("ultimaConsulta", ultimaConsulta);
        dados.put("ultimaColeta", ultimaColeta);
        dados.put("numeroAtual", numeroAtual);

        return dados;
    }
}

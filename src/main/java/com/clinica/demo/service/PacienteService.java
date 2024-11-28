package com.clinica.demo.service;

import com.clinica.demo.model.Medico;
import com.clinica.demo.model.Paciente;
import com.clinica.demo.repository.MedicoRepository;
import com.clinica.demo.repository.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PacienteService {

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private MedicoService medicoService;

    public List<Paciente> listarPacientes() {
        return pacienteRepository.findAll();
    }

    public Paciente adicionarPaciente(Paciente paciente) {
        return pacienteRepository.save(paciente);
    }

    public Paciente buscarPaciente(Long id) {
        return pacienteRepository.findById(id).orElse(null);
    }
    public List<Paciente> listarPacientesEmEspera() {
        return pacienteRepository.findBySituacao("Em Espera");
    }

    public List<Paciente> listarPacientesAtendidos() {
        return pacienteRepository.findBySituacao("Atendido");
    }
    public Paciente atribuirMedicoAoPaciente(Paciente paciente) {
        try {
            Medico medico = medicoService.selecionarMedicoDisponivel();
            paciente.setMedico(medico.getNome()); // Atribui o nome do médico ao paciente
        } catch (RuntimeException e) {
            paciente.setMedico("Nenhum Médico Disponível"); // Define mensagem se nenhum médico estiver disponível
        }
        return paciente;
    }
    public String obterUltimoPacientePorTipo(String tipo) {
        return pacienteRepository.findUltimoPacientePorTipo(tipo);
    }

    public String obterProximoPaciente() {
        return pacienteRepository.findProximoPaciente();
    }

    public Paciente chamarProximoPaciente() {
        // Lógica para encontrar o próximo paciente em espera
        Optional<Paciente> proximoPaciente = pacienteRepository.findFirstBySituacaoOrderByPosicaoNaFila("Em Espera");

        if (proximoPaciente.isPresent()) {
            Paciente paciente = proximoPaciente.get();
            paciente.setSituacao("Em Atendimento");
            return pacienteRepository.save(paciente);
        } else {
            throw new RuntimeException("Não há pacientes em espera");
        }
    }
}

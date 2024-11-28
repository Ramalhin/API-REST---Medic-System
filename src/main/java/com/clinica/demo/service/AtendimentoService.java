package com.clinica.demo.service;

import com.clinica.demo.model.Atendimento;
import com.clinica.demo.model.Medico;
import com.clinica.demo.model.Paciente;
import com.clinica.demo.repository.AtendimentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AtendimentoService {

    @Autowired
    private AtendimentoRepository atendimentoRepository;

    @Autowired
    private MedicoService medicoService;


    public List<Atendimento> buscarPorTipoAtendimento(String tipoAtendimento) {
        return atendimentoRepository.findAllByTipoAtendimento(tipoAtendimento);
    }
    public Atendimento buscarProximoPaciente() {
        return atendimentoRepository.findFirstByOrderByPrioridadeDesc();
    }
    public Atendimento salvarAtendimento(Atendimento atendimento) {
        return atendimentoRepository.save(atendimento);
    }
    public Paciente atribuirMedicoAoPaciente(Paciente paciente) {
        try {
            Medico medico = medicoService.selecionarMedicoDisponivel();
            paciente.setMedico(medico.getNome());
        } catch (RuntimeException e) {
            paciente.setMedico("Nenhum Médico Disponível");
        }
        return paciente;
    }
    public List<Atendimento> listarTodos() {
        return atendimentoRepository.findAll();
    }
}

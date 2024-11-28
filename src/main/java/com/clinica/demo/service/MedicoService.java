package com.clinica.demo.service;

import com.clinica.demo.model.Medico;
import com.clinica.demo.repository.MedicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class MedicoService {

    @Autowired
    private MedicoRepository medicoRepository;

    // Cadastro de médico
    public Medico cadastrarMedico(Medico medico) {
        return medicoRepository.save(medico);  // Salva o médico diretamente no banco de dados
    }

    // Autenticação de médico por email e senha
    public Optional<Medico> autenticarMedico(String email, String senha) {
        Optional<Medico> medicoOpt = medicoRepository.findByEmail(email);
        if (medicoOpt.isPresent()) {
            Medico medico = medicoOpt.get();
            if (medico.getSenha().equals(senha)) {
                return medicoOpt;
            }
        }
        return Optional.empty();
    }
    public Medico selecionarMedicoDisponivel() {
        List<Medico> medicosDisponiveis = medicoRepository.findByDisponivel(true); // Busca médicos disponíveis
        if (medicosDisponiveis.isEmpty()) {
            throw new RuntimeException("Nenhum médico disponível no momento.");
        }
        Random random = new Random();
        return medicosDisponiveis.get(random.nextInt(medicosDisponiveis.size())); // Retorna um médico aleatório
    }
    public List<Medico> listarMedicos() {
        return medicoRepository.findAll();
    }

}

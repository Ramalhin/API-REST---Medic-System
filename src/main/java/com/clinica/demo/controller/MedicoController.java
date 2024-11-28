package com.clinica.demo.controller;

import com.clinica.demo.model.Medico;
import com.clinica.demo.repository.MedicoRepository;
import com.clinica.demo.service.MedicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/medicos")
public class MedicoController {

    @Autowired
    private MedicoService medicoService;
    @Autowired
    private MedicoRepository medicoRepository;

    // Cadastro de médico
    @PostMapping("/cadastrar")
    public ResponseEntity<?> cadastrarMedico(@RequestBody Medico medico) {
        try {
            Medico novoMedico = medicoService.cadastrarMedico(medico);
            return ResponseEntity.ok(novoMedico);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro ao cadastrar o médico: " + e.getMessage());
        }
    }

    // Login de médico
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String email, @RequestParam String senha) {
        Optional<Medico> medico = medicoRepository.findByEmailAndSenha(email, senha);
        if (medico.isPresent()) {
            return ResponseEntity.ok("Login bem-sucedido");
        }
        return ResponseEntity.status(401).body("Email ou senha incorretos!");
    }
    @GetMapping("/listar")
    public List<Medico> listarMedicos() {
        return medicoService.listarMedicos();
    }

}

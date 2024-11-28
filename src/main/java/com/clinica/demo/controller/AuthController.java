package com.clinica.demo.controller;

import com.clinica.demo.model.Medico;
import com.clinica.demo.repository.MedicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private MedicoRepository medicoRepository;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Map<String, String> credentials) {
        String email = credentials.get("email");
        String senha = credentials.get("senha");

        // Verifica se os campos estão presentes
        if (email == null || senha == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email e senha são obrigatórios");
        }

        // Procura o médico pelo email
        Optional<Medico> medicoOpt = medicoRepository.findByEmail(email);

        // Verifica se o médico existe
        if (medicoOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Email não encontrado");
        }

        Medico medico = medicoOpt.get();

        // Verifica se a senha está correta
        if (!medico.getSenha().equals(senha)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Senha incorreta");
        }

        // Login bem-sucedido
        return ResponseEntity.ok("Login bem-sucedido");
    }
}

package com.clinica.demo.controller;

import com.clinica.demo.model.*;
import com.clinica.demo.repository.MedicoRepository;
import com.clinica.demo.service.MedicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class LoginController {

    @Autowired
    private MedicoRepository medicoRepository;
    @Autowired
    private MedicoService medicoService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String email, @RequestParam String senha) {
        Optional<Medico> medicoOpt = medicoService.autenticarMedico(email, senha);
        if (medicoOpt.isPresent()) {
            return ResponseEntity.ok("Login bem-sucedido");
        } else {
            return ResponseEntity.status(401).body("Credenciais inválidas");
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/frontend/login.html";
    }
}
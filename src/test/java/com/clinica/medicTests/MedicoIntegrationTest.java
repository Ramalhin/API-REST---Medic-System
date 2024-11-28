package com.clinica.medicTests;


import com.clinica.demo.ClinicaApplication;
import com.clinica.demo.model.Medico;
import com.clinica.demo.repository.MedicoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = ClinicaApplication.class)
class MedicoIntegrationTest {

    @Autowired
    private MedicoRepository medicoRepository;

    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:cleanup.sql")
    @Test
    void deveSalvarMedicoNoBancoDeDados() {
        // Arrange
        Medico medico = new Medico("Dr. João", "joao@clinica.com", "senha123", "12345");

        // Act
        Medico salvo = medicoRepository.save(medico);
        Optional<Medico> encontrado = medicoRepository.findById(salvo.getId());

        // Assert
        assertTrue(encontrado.isPresent(), "O médico deveria estar presente no banco de dados");
        assertEquals("Dr. João", encontrado.get().getNome(), "O nome do médico deveria ser Dr. João");
        assertEquals("joao@clinica.com", encontrado.get().getEmail(), "O email do médico deveria ser joao@clinica.com");
        assertEquals("12345", encontrado.get().getCrm(), "O CRM do médico deveria ser 12345");
    }

}
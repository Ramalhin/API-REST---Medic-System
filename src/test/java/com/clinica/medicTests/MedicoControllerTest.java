package com.clinica.medicTests;

import com.clinica.demo.controller.MedicoController;
import com.clinica.demo.model.Medico;
import com.clinica.demo.service.MedicoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MedicoControllerTest {

    @Mock
    private MedicoService medicoService;

    @InjectMocks
    private MedicoController medicoController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void cadastrarMedico_deveRetornarOk() {
        // Arrange
        Medico medico = new Medico("Dr. João", "joao@clinica.com", "senha123", "CRM001");
        when(medicoService.cadastrarMedico(medico)).thenReturn(medico);

        // Act
        ResponseEntity<?> response = medicoController.cadastrarMedico(medico);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        verify(medicoService, times(1)).cadastrarMedico(medico);
    }

    @Test
    void cadastrarMedico_deveRetornarErroSeServicoFalhar() {
        // Arrange
        Medico medico = new Medico("Dr. João", "joao@clinica.com", "senha123", "CRM001");
        when(medicoService.cadastrarMedico(medico)).thenThrow(new IllegalArgumentException("Dados inválidos"));

        // Act
        ResponseEntity<?> response = medicoController.cadastrarMedico(medico);

        // Assert
        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Dados inválidos", response.getBody());
        verify(medicoService, times(1)).cadastrarMedico(medico);
    }
}

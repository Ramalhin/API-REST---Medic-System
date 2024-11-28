package com.clinica.medicTests;


import com.clinica.demo.model.Medico;
import com.clinica.demo.repository.MedicoRepository;
import com.clinica.demo.service.MedicoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MedicoServiceTest {

    @Mock
    private MedicoRepository medicoRepository;

    @InjectMocks
    private MedicoService medicoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void cadastrarMedico_deveSalvarMedico() {
        // Arrange
        Medico medico = new Medico("Dr. João", "joao@clinica.com", "senha123", "12345");
        when(medicoRepository.save(medico)).thenReturn(medico);

        // Act
        Medico resultado = medicoService.cadastrarMedico(medico);

        // Assert
        assertNotNull(resultado);
        assertEquals("Dr. João", resultado.getNome());
        verify(medicoRepository, times(1)).save(medico);
    }

    @Test
    void cadastrarMedico_deveLancarExcecaoSeEmailDuplicado() {
        // Arrange
        Medico medico = new Medico("Dr. João", "joao@clinica.com", "senha123", "12345");
        when(medicoRepository.save(medico)).thenThrow(new IllegalArgumentException("Email já está em uso"));

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            medicoService.cadastrarMedico(medico);
        });
        assertEquals("Email já está em uso", exception.getMessage());
    }
}
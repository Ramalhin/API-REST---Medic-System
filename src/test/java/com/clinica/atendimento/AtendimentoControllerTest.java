package com.clinica.atendimento;

import com.clinica.demo.controller.AtendimentoController;
import com.clinica.demo.model.Paciente;
import com.clinica.demo.repository.PacienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AtendimentoControllerTest {

    @InjectMocks
    private AtendimentoController atendimentoController; // Certifique-se de importar corretamente

    @Mock
    private PacienteRepository pacienteRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Inicializa os mocks
    }

    @Test
    void testChamarProximoPacienteComSucesso() {
        // Simula uma fila de pacientes
        List<Paciente> fila = new ArrayList<>();
        Paciente primeiroPaciente = new Paciente();
        primeiroPaciente.setId(1L); // ID como Long
        primeiroPaciente.setNumeroPaciente("P0001");
        primeiroPaciente.setSituacao("Em Espera");
        primeiroPaciente.setPosicaoNaFila(1L); // Corrigido para Long
        fila.add(primeiroPaciente);

        // Configura o mock para retornar a fila simulada
        when(pacienteRepository.findAll(Sort.by(Sort.Direction.ASC, "posicaoNaFila"))).thenReturn(fila);

        // Executa o método do controlador
        Paciente proximoPaciente = atendimentoController.chamarProximoPaciente();

        // Verifica se o paciente foi chamado corretamente
        assertNotNull(proximoPaciente);
        assertEquals("P0001", proximoPaciente.getNumeroPaciente());
        assertEquals("Atendido", proximoPaciente.getSituacao());
        verify(pacienteRepository, times(1)).save(primeiroPaciente);
    }

    @Test
    void testChamarProximoPacienteSemFila() {
        // Configura o mock para retornar uma fila vazia
        when(pacienteRepository.findAll(Sort.by(Sort.Direction.ASC, "posicaoNaFila"))).thenReturn(new ArrayList<>());

        // Executa o método e verifica se uma exceção é lançada
        Exception exception = assertThrows(RuntimeException.class, () -> {
            atendimentoController.chamarProximoPaciente();
        });

        // Verifica se a mensagem da exceção está correta
        assertEquals("Nenhum paciente na fila.", exception.getMessage());
    }
}

package com.clinica.medicTests;

import com.clinica.demo.ClinicaApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = ClinicaApplication.class)
@AutoConfigureMockMvc
public class MedicoEndpointIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:data-test.sql")
    @Test
    void deveCadastrarMedicoPeloEndpoint() throws Exception {
        // Arrange
        String medicoJson = """
            {
                "nome": "Dr. Pedro",
                "crm": "CRM002",
                "email": "pedro@clinica.com",
                "senha": "senha123"
            }
        """;

        // Act & Assert
        mockMvc.perform(post("/api/medicos/cadastrar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(medicoJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Dr. Pedro"))
                .andExpect(jsonPath("$.email").value("pedro@clinica.com"));
    }
}

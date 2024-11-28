package com.clinica.authTests;

import com.clinica.demo.ClinicaApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest(classes = ClinicaApplication.class)
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:data-test.sql")
    @Test
    void testLoginSuccess() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                        .param("email", "joao@clinica.com")
                        .param("senha", "12345")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(content().string("Login bem-sucedido"));
    }

    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:data-test.sql")
    @Test
    void testLoginInvalidPassword() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                        .param("email", "joao@clinica.com")
                        .param("senha", "wrongpassword")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Credenciais inválidas"));
    }

    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:data-test.sql")
    @Test
    void testLoginNonExistentEmail() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                        .param("email", "naoexiste@clinica.com")
                        .param("senha", "12345")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Credenciais inválidas"));
    }
}

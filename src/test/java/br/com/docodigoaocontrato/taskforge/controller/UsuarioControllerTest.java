package br.com.docodigoaocontrato.taskforge.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = "spring.datasource.url=jdbc:h2:mem:testes")
@AutoConfigureMockMvc
public class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void deveCadastrarUsuarioSemRetornarCampoSenha() throws Exception {
        String usuarioJson = """
                {
                    "nome": "Usuario Teste",
                    "email": "usuarioteste@email.com",
                    "senha": "senhaSuperSecreta123"
                }
                """;

        mockMvc.perform(post("/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(usuarioJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.nome").value("Usuario Teste"))
                .andExpect(jsonPath("$.email").value("usuarioteste@email.com"))
                .andExpect(jsonPath("$.senha").doesNotExist());
    }

    @Test
    void deveRealizarLoginSemRetornarCampoSenha() throws Exception {
        String usuarioCadastro = """
                {
                    "nome": "Usuario Login",
                    "email": "loginseguro@email.com",
                    "senha": "senhaSuperSecreta123"
                }
                """;

        String loginJson = """
                {
                    "email": "loginseguro@email.com",
                    "senha": "senhaSuperSecreta123"
                }
                """;

        mockMvc.perform(post("/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(usuarioCadastro))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/usuarios/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.senha").doesNotExist());
    }
}
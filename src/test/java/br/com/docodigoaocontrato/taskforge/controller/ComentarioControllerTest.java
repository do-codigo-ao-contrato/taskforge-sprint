package br.com.docodigoaocontrato.taskforge.controller;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = "spring.datasource.url=jdbc:h2:mem:testes")
@AutoConfigureMockMvc
public class ComentarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private String login() throws Exception {
        String usuarioCadastroDTO = """
                {
                    "nome": "Teste",
                    "email": "teste@teste.com",
                    "senha": "teste123"
                }
                """;

        String loginDTO = """
                {
                    "email": "teste@teste.com",
                    "senha": "teste123"
                }
                """;

        mockMvc.perform(post("/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(usuarioCadastroDTO));

        String resposta = mockMvc.perform(post("/usuarios/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginDTO))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        return "Bearer " + JsonPath.read(resposta, "$.token");
    }

    @Test
    void deveAtualizarDescricaoEAutorDoComentario() throws Exception {
        String token = login();

        String resposta = mockMvc.perform(post("/comentarios")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "descricao": "Comentário inicial",
                                    "autor": "Ana"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.autor").value("Ana"))
                .andReturn().getResponse().getContentAsString();

        Integer id = JsonPath.read(resposta, "$.id");

        mockMvc.perform(put("/comentarios/" + id)
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "descricao": "Comentário atualizado",
                                    "autor": "Bruno"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.descricao").value("Comentário atualizado"))
                .andExpect(jsonPath("$.autor").value("Bruno"));

        mockMvc.perform(get("/comentarios/" + id)
                        .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.descricao").value("Comentário atualizado"))
                .andExpect(jsonPath("$.autor").value("Bruno"));
    }

    @Test
    void deveRetornar404AoAtualizarComentarioInexistente() throws Exception {
        String token = login();

        mockMvc.perform(put("/comentarios/99999")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "descricao": "Comentário atualizado",
                                    "autor": "Bruno"
                                }
                                """))
                .andExpect(status().isNotFound());
    }
}

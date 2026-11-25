package br.com.docodigoaocontrato.taskforge.controller;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasItems;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = "spring.datasource.url=jdbc:h2:mem:testes")
@AutoConfigureMockMvc
public class ComentarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void deveFiltrarComentariosPorAutor() throws Exception {
        String token = login();

        mockMvc.perform(post("/comentarios")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "descricao": "Comentario teste",
                                    "autor": "AutorUnicoTeste"
                                }
                                """))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/comentarios")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "descricao": "Comentario",
                                    "autor": "Outro autor"
                                }
                                """))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/comentarios")
                        .header("Authorization", token)
                        .param("autor", "AutorUnicoTeste"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].autor").value("AutorUnicoTeste"));
    }

    @Test
    void deveListarTodosQuandoNaoInformarAutor() throws Exception {
        String token = login();

        mockMvc.perform(post("/comentarios")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "descricao": "Comentario teste",
                                    "autor": "AutorTodosA"
                                }
                                """))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/comentarios")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "descricao": "Comentario",
                                    "autor": "AutorTodosB"
                                }
                                """))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/comentarios")
                        .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].autor", hasItems("AutorTodosA", "AutorTodosB")));
    }

    @Test
    void deveRetornarListaVaziaQuandoAutorNaoExiste() throws Exception {
        String token = login();

        mockMvc.perform(get("/comentarios")
                        .header("Authorization", token)
                        .param("autor", "AutorQueNaoExiste"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

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
}

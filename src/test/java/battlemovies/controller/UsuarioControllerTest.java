package battlemovies.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UsuarioControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void adicionarUsuarioOk() throws Exception {
        this.mockMvc.perform(post("/usuario/cadastro")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"login\": \"Mayara\", \"senha\": \"Senha123\"}"))
                .andDo(print())
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void adicionarUsuarioFailEmUso() throws Exception {
        this.mockMvc.perform(post("/usuario/cadastro")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"login\": \"Mayara\", \"senha\": \"Senha123\"}"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void adicionarUsuarioFailNaoAtendeRequisitos() throws Exception {
        this.mockMvc.perform(post("/usuario/cadastro")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"login\": \"Mayaraaaaaaaaa\", \"senha\": \"Senha123\"}"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

}

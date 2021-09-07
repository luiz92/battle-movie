package battlemovies.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class BattleMovieControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void listBattleMovieOk() throws Exception {
        this.mockMvc.perform(get("/quizz/battleMovie")
                .content(""))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void battleMovieMsgOk() throws Exception {
        this.mockMvc.perform(get("/quizz/battleMovie"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Pegue o ID")));
    }

    @Test
    void battleMovieFilmeUsuarioIncorreto() throws Exception {
        this.mockMvc.perform(post("/quizz/jogar")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "    \"login\": \"Mayar@\",\"senha\": \"Senha123\",\"id\": \"tt2357129\"\n}"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void battleMovieFilmeIdIncorreto() throws Exception {
        this.mockMvc.perform(post("/quizz/jogar")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "    \"login\": \"Mayara\",\"senha\": \"Senha123\",\"id\": \"tt2357129\"\n}"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

}

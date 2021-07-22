package battlemovies.controller;

import battlemovies.dao.FilmesDaoImpl;
import battlemovies.dao.RankingDaoImpl;
import battlemovies.servicos.JogosServiceImpl;
import battlemovies.servicos.UsuarioServiceImpl;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RequestMapping("/quizz")
@RestController
public class BattleMoviesController {

    @Autowired
    private FilmesDaoImpl filmesDao;

    @Autowired
    private RankingDaoImpl rankingDao;

    @Autowired
    private JogosServiceImpl jogosService;

    @Autowired
    private UsuarioServiceImpl usuarioService;

//    GET >  http://localhost:8080/quizz
    @GetMapping()
    public String exibirFilmes(){
        return jogosService.getTxtBattle();
    }

//    GET > http://localhost:8080/quizz/battle
    @GetMapping("/battle")
    public String battle(){
        return jogosService.getTxtGameInfo();
    }

    //  GET >  http://localhost:8080/quizz/ranking
    @GetMapping("/ranking")
    public List ranking(){
        return rankingDao.linhaEmRanking();
    }

//    POST >  http://localhost:8080/quizz
//    body > raw > JSON > {"nome": "LuizAlves", "senha": "Senha123", "id": "tt1201607"}
//    id é pego em GET > http://localhost:8080/quizz
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String getJogadaUsuario(@RequestBody ObjectNode objectNode) {
        String login = objectNode.get("nome").asText();
        String senha = objectNode.get("senha").asText();
        String id = objectNode.get("id").asText();
        if(usuarioService.verificaExistenciaUsuario(login, senha)) {
            if(jogosService.validaID(id)){
                    return jogosService.validaMelhorFilme(login, id);
            }
        }
        return "Dados incorretos!";
    }

}

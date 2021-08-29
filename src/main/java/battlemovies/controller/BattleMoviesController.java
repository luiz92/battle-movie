package battlemovies.controller;

import battlemovies.exceptions.DadosIncorretosException;
import battlemovies.modelo.Jogada;
import battlemovies.modelo.Ranking;
import battlemovies.servicos.JogosServiceImpl;
import battlemovies.servicos.RankingServiceImpl;
import battlemovies.servicos.UsuarioServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/quizz")
public class BattleMoviesController {
    private final RankingServiceImpl rankingService;
    private final JogosServiceImpl jogosService;
    private final UsuarioServiceImpl usuarioService;

    @GetMapping()
    public String exibirFilmes(){
        return jogosService.getTxtBattle();
    }

    @GetMapping("/battle")
    public String battle(){
        return jogosService.getTxtGameInfo();
    }

    @GetMapping("/ranking")
    public List<Ranking> ranking(){
        return rankingService.exibirRanking();
    }

    @PostMapping("/jogar")
    @ResponseStatus(HttpStatus.CREATED)
    public String jogada(@RequestBody Jogada jogada) {
        if(usuarioService.validaUserPass(jogada.getLogin(), jogada.getSenha())) {
                    return jogosService.validaMelhorFilme(jogada);
        }
        throw new DadosIncorretosException();
    }

}

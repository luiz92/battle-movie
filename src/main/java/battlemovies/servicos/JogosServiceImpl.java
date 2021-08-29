package battlemovies.servicos;

import battlemovies.dao.FilmesDaoImpl;
import battlemovies.dao.JogosDaoImpl;
import battlemovies.exceptions.IdIncorretoException;
import battlemovies.modelo.Filmes;
import battlemovies.modelo.Jogada;
import battlemovies.modelo.Jogos;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class JogosServiceImpl {
    private final FilmesDaoImpl filmesDao;
    private final JogosDaoImpl jogosDao;
    private final RankingServiceImpl rankingService;

    public String validaMelhorFilme(Jogada jogada) {
        var filmesJogada = filmesDao.jogadaAtualList();
        var jogador = jogosDao.jogoPendenteExist(jogada.getLogin());
        double pontoFilmeJogador = validaID(jogada.getIdFilme()).getVotos() / validaID(jogada.getIdFilme()).getRating();

        for (Filmes filmes : filmesJogada) {
            double filme = filmes.getVotos() / filmes.getRating();
            jogador.setLogin(jogada.getLogin());
            if (pontoFilmeJogador > filme) {
                return jogadaCorreta(jogador);
            } else if (pontoFilmeJogador < filme) {
                return jogadaErrada(jogador);
            }
        }
        return null;
    }

    private String jogadaCorreta(Jogos jogador) {
        jogador.setJogadas(jogador.getJogadas() + 1);
        jogosDao.atualizaJogo(jogador);
        filmesDao.fimDaJogada();
        return "Você acertou! Continue jogando";
    }

    private String jogadaErrada(Jogos jogador) {
        jogador.setJogadas(jogador.getJogadas() + 1);
        jogador.setContador(jogador.getContador() + 1);
        jogosDao.atualizaJogo(jogador);
        filmesDao.fimDaJogada();
            if (jogador.getContador() == 3) {
                rankingService.novoRanking(jogador);
                jogosDao.fimDeJogo();
                return "O jogo acabou para você! Consulte /ranking";
            }
        return "Você errou! Mas continua vivo no jogo";
    }

    public Filmes validaID(String id) {
        for (Filmes validaFilmeID : filmesDao.jogadaAtualList()) {
            if (validaFilmeID.getId().equals(id)) {
                return validaFilmeID;
            }
        }
        throw new IdIncorretoException(id);
    }

    public String getTxtBattle(){
        var battleMovie = filmesDao.getBattleMovie();
        return "Pegue o ID e faça sua jogada do melhor filme! (Cálculo= Votos / Rating)\n" +
                "\tID: "+battleMovie.get(0).getId()+"\tFilme: "+battleMovie.get(0).getNome()+"\n"+
                "\tID: "+battleMovie.get(1).getId()+"\tFilme: "+battleMovie.get(1).getNome();
    }

    public String getTxtGameInfo(){
        return "O jogo consiste em 2 filmes batalhando por suas reputações\n" +
                "VOCÊ foi o escolhido para escolher quem é o melhor entre eles\n" +
                "Nem tanto na verdade.. o quesito acerto/erro vai depender se você\n" +
                "acertar qual entre eles tem a melhor média de pontos (votos / rating)\n" +
                "Pense rápido e jogue! A reputação deles está em suas mãos :D";
    }
}

package battlemovies.servicos;

import battlemovies.dao.FilmesDaoImpl;
import battlemovies.dao.JogosDaoImpl;
import battlemovies.modelo.Filmes;
import battlemovies.modelo.Jogos;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;

@Getter
@Setter
@Component
public class JogosServiceImpl {
    List<Filmes> listaFilme;

    @Autowired
    private FilmesDaoImpl filmesDao;
    @Autowired
    private JogosDaoImpl jogosDao;
    @Autowired
    private RankingServiceImpl rankingService;

    //Verifica o melhor filme calculando Rating * Votos
    public String validaMelhorFilme(String login, String id) {
        listaFilme = filmesDao.filmesJogadaAtual();
        Jogos jogador = new Jogos();
        if (jogosDao.continuaJogoPendente(login) != null){
            jogador = jogosDao.continuaJogoPendente(login);
        }
        var pontuacaoFilme1 = listaFilme.get(0).getRating() * listaFilme.get(0).getVotos();
        var pontuacaoFilme2 = listaFilme.get(1).getRating() * listaFilme.get(1).getVotos();
        if (listaFilme.get(0).getId().equals(id) && jogador.getContador()!=3) {
            return validaAcertoErro(login, jogador, pontuacaoFilme1, pontuacaoFilme2);
        } else if (listaFilme.get(1).getId().equals(id) && jogador.getContador()!=3){
            return validaAcertoErro(login, jogador, pontuacaoFilme2, pontuacaoFilme1);
        }
        return null;
    }

    //Verifica se o jogador acertou ou errou
    private String validaAcertoErro(String login, Jogos jogador, double pontuacaoFilme1, double pontuacaoFilme2) {
        if (pontuacaoFilme2 > pontuacaoFilme1) {
            jogador.setLogin(login);
            jogador.setJogadas(jogador.getJogadas() + 1);
            jogador.setContador(jogador.getContador());
            jogosDao.atualizaJogo(jogador);
            return "Você acertou, continue jogando.";
        } else {
            jogador.setLogin(login);
            jogador.setJogadas(jogador.getJogadas());
            jogador.setContador(jogador.getContador() + 1);
            if (jogador.getContador() >= 3) {
                jogosDao.atualizaJogo(jogador);
                rankingService.novoRanking(jogador);
                jogosDao.fimDeJogo(jogador);
                return "O jogo acabou para você! Veja sua pontuação no Ranking";
            }
            jogosDao.atualizaJogo(jogador);
            return "Errou mas continua vivo!";
        }
    }

    //Verifica se o jogador entrou com ID correto
    public boolean validaID(String id) {
        listaFilme = filmesDao.filmesJogadaAtual();
        for (Filmes validaFilmeID : listaFilme) {
            if (validaFilmeID.getId().equals(id)) {
                return true;
            }
        }
        return false;
    }
}

package battlemovies.servicos;

import battlemovies.dao.FilmesDaoImpl;
import battlemovies.dao.JogosDaoImpl;
import battlemovies.modelo.Filmes;
import battlemovies.modelo.Jogos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class JogosServiceImpl {

    List<Filmes> filmesDoJogo;

    @Autowired

    private FilmesDaoImpl filmesDao;

    @Autowired
    private JogosDaoImpl jogosDao;

    @Autowired
    private RankingServiceImpl rankingService;

    //Verifica o melhor filme calculando Rating * Votos
    public String validaMelhorFilme(String login, String id) {
        filmesDoJogo = filmesDao.filmesJogadaAtual();
        Jogos jogador = new Jogos();
        //Caso seja o último jogador que não finalizou suas jogadas
        if (jogosDao.continuaJogoPendente(login) != null){
            jogador = jogosDao.continuaJogoPendente(login);
        }
        double pontuacaoFilme1 = filmesDoJogo.get(0).getRating() * filmesDoJogo.get(0).getVotos();
        double pontuacaoFilme2 = filmesDoJogo.get(1).getRating() * filmesDoJogo.get(1).getVotos();
        if (filmesDoJogo.get(0).getId().equals(id) && jogador.getContador()!=3) {
            return calculaAcertoErro(login, jogador, pontuacaoFilme1, pontuacaoFilme2);
        } else if (filmesDoJogo.get(1).getId().equals(id) && jogador.getContador()!=3){
            return calculaAcertoErro(login, jogador, pontuacaoFilme2, pontuacaoFilme1);
        }
        return null;
    }

    //Verifica se o jogador acertou ou errou na sua escolha e realiza os calculos
    private String calculaAcertoErro(String login, Jogos jogador, double escolhaDoJogador, double filme2) {
        if (escolhaDoJogador > filme2) {
            jogador.setLogin(login);
            jogador.setJogadas(jogador.getJogadas() + 1);
            jogador.setContador(jogador.getContador());
            jogosDao.atualizaJogo(jogador);
            filmesDao.fimDaJogada();
            return msgJogadaResult(escolhaDoJogador, filme2, "Parabéns! Você acertou!%n");
        } else {
            jogador.setLogin(login);
            jogador.setJogadas(jogador.getJogadas());
            jogador.setContador(jogador.getContador() + 1);
                if (jogador.getContador() >= 3) {
                    jogosDao.atualizaJogo(jogador);
                    filmesDao.fimDaJogada();
                    rankingService.novoRanking(jogador);
                    jogosDao.fimDeJogo();
                    return msgJogadaResult(escolhaDoJogador, filme2, "FIM de jogo para você! Acesse Ranking!%n");
                }
            jogosDao.atualizaJogo(jogador);
            filmesDao.fimDaJogada();
            return msgJogadaResult(escolhaDoJogador, filme2, "Você errou.. mas continua vivo!%n");
        }
    }

    private String msgJogadaResult(double escolhaDoJogador, double filme2, String s) {
        return String.format(s +
                "O filme escolhido tem pontuação    : %.0f %n" +
                "O filme concorrente tem pontuação: %.0f", escolhaDoJogador, filme2);
    }

    //Verifica se o jogador entrou com ID correto
    public boolean validaID(String id) {
        filmesDoJogo = filmesDao.filmesJogadaAtual();
        for (Filmes validaFilmeID : filmesDoJogo) {
            if (validaFilmeID.getId().equals(id)) {
                return true;
            }
        }
        return false;
    }

    //Envia texto padrão da batalha, exibindo os filmes
    public String getTxtBattle(){
        var filme = filmesDao.getBattleMovie();
        return "Pegue o ID e faça sua jogada do melhor filme! (Cálculo= Votos*Rating)\n" +
                "\tID: "+filme.get(0).getId()+"\tFilme: "+filme.get(0).getNome()+"\n"+
                "\tID: "+filme.get(1).getId()+"\tFilme: "+filme.get(1).getNome();
    }

    //Informações do jogo
    public String getTxtGameInfo(){
        return "O jogo consiste em 2 filmes batalhando por suas reputações\n" +
                "VOCÊ foi o escolhido para escolher quem é o melhor entre eles\n" +
                "Nem tanto na verdade.. o quesito acerto/erro vai depender se você\n" +
                "acertar qual entre eles tem a melhor média de pontos (votos * rating)\n" +
                "Pense rápido e jogue! A reputação deles está em suas mãos :D";
    }
}

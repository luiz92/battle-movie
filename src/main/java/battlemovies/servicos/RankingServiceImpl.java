package battlemovies.servicos;

import battlemovies.dao.RankingDaoImpl;
import battlemovies.modelo.Jogos;
import battlemovies.modelo.Ranking;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RankingServiceImpl {

    @Autowired
    private RankingDaoImpl rankingDao;

    //No final das vidas do jogador, atribui os dados que serão utilizados no armazenamento
    public void novoRanking(Jogos jogador) {
        Ranking jogadorRankeado = new Ranking();
        jogadorRankeado.setNome(jogador.getLogin());
        jogadorRankeado.setPontuacao(jogador.getContador()*jogador.getJogadas());
        rankingDao.adicionar(jogadorRankeado);
    }
}

package battlemovies.servicos;

import battlemovies.dao.RankingDaoImpl;
import battlemovies.modelo.Jogos;
import battlemovies.modelo.Ranking;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@RequiredArgsConstructor
@Service
public class RankingServiceImpl {
    private final RankingDaoImpl rankingDao;

    public void novoRanking(Jogos jogador) {
        Ranking jogadorRankeado = new Ranking();
        jogadorRankeado.setNome(jogador.getLogin());
        jogadorRankeado.setPontuacao(jogador.getContador()*jogador.getJogadas());
        rankingDao.adicionar(jogadorRankeado);
    }

    public List<Ranking> exibirRanking() {
        return rankingDao.linhaEmRanking();
    }
}

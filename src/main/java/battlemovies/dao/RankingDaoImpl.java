package battlemovies.dao;

import battlemovies.modelo.Ranking;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import javax.annotation.PostConstruct;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Repository
public class RankingDaoImpl {
    @Value("${rankingFile}")
    private String caminho;
    private Path path;
    private List<Ranking> registroLinhas = new ArrayList<>();

    @PostConstruct
    public void init(){
        try {
            path = Paths.get(caminho);
            if (!path.toFile().exists()) {
                Files.createFile(path);
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public void adicionar(Ranking jogo){
        try (BufferedWriter bf = Files.newBufferedWriter(path, StandardOpenOption.APPEND)) {
            bf.write(formatar(jogo));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Ranking> linhaEmRanking() {
        try (Stream<String> streamLinhas = Files.lines(Path.of(caminho))) {
            registroLinhas = streamLinhas
                    .filter(Predicate.not(String::isEmpty))
                    .map(Ranking::new)
                    .sorted((a,b) -> Long.compare(b.getPontuacao(), a.getPontuacao())) //ordena + -> -
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return registroLinhas;
    }

    public String formatar(Ranking ranking) {
        return String.format("%s,%d\r%n",ranking.getNome(),ranking.getPontuacao());
    }
}
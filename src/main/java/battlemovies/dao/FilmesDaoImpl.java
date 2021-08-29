package battlemovies.dao;

import battlemovies.modelo.Filmes;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Repository
public class FilmesDaoImpl {
    @Value("${filmesFile}")
    private String caminho;
    @Value("${filmesTemp}")
    private String caminho2;
    private Path path2;
    private List<Filmes> registroLinhas = new ArrayList<>();

    @PostConstruct
    public void init(){
            path2 = Paths.get(caminho2);
    }

    public void linhaEmFilme() {
        try (Stream<String> streamLinhas = Files.lines(Path.of(caminho))) {
            registroLinhas = streamLinhas
                    .filter(Predicate.not(String::isEmpty))
                    .map(Filmes::new)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Filmes> getAll() {
        linhaEmFilme();
        return registroLinhas;
    }

    public List<Filmes> getBattleMovie(){
        Random random = new Random();
        Filmes filme1;
        Filmes filme2;
        var filmesList = getAll();

        filme1 = filmesList.get(random.nextInt(filmesList.size()));
        do {
            filme2 = filmesList.get(random.nextInt(filmesList.size()));
        } while (filme2 == filme1);

        gravaFilmesTemp(filme1, filme2);
        return jogadaAtualList();
    }

    public void gravaFilmesTemp(Filmes filme1, Filmes filme2){
        var leArquivo = new File(caminho2);
        try{
            var arquivo = new FileWriter(leArquivo, false);
            arquivo.flush();
            arquivo.write(filme1.getId() + "," + filme1.getNome() + "," + filme1.getVotos() + "," + filme1.getRating() + "\n");
            arquivo.write(filme2.getId() + "," + filme2.getNome() + "," + filme2.getVotos() + "," + filme2.getRating() + "\n");
            arquivo.close();
            } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Filmes> jogadaAtualList() {
        try (Stream<String> streamLinhas = Files.lines(Path.of(caminho2))) {
            registroLinhas = streamLinhas
                    .filter(Predicate.not(String::isEmpty))
                    .map(Filmes::new)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return registroLinhas;
    }

    public void fimDaJogada(){
        try {
            Files.newBufferedWriter(path2 , StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
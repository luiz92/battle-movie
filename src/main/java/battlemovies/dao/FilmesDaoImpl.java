package battlemovies.dao;

import battlemovies.modelo.Filmes;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class FilmesDaoImpl {
    private String caminho = "src\\main\\java\\battlemovies\\files\\filmes.csv";
    private String caminho2 = "src\\main\\java\\battlemovies\\files\\filmesTemp.csv";
    private Path path;
    private List<Filmes> registroLinhas = new ArrayList<>();

    @PostConstruct
    public void init(){
        path = Paths.get(caminho);
    }

    //Gera dois filmes random envia para o GET e envia os dados para o metodo filmesJogadaAtual();
    public List getBattleMovie(){
        var filmes = getAll();
        var battleMovie = new ArrayList();
        Random random = new Random();
        Filmes filme1 = filmes.get(random.nextInt(filmes.size()));
        Filmes filme2 = filmes.get(random.nextInt(filmes.size()));
        battleMovie.add(filme1);
        battleMovie.add(filme2);
        gravaArquivo(filme1, filme2);
        return battleMovie;
    }

    //Grava os filmes da jogada atual em um csv para ser possível manipular os dados sem perder eles em novas solicitações
    public List filmesJogadaAtual() {
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

    public List linhaEmFilme() {
        try (Stream<String> streamLinhas = Files.lines(Path.of(caminho))) {
            registroLinhas = streamLinhas
                    .filter(Predicate.not(String::isEmpty))
                    .map(Filmes::new)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return registroLinhas;
    }

    public List<Filmes> getAll() {
        linhaEmFilme();
        return registroLinhas;
    }

    public void gravaArquivo(Filmes filme1, Filmes filme2){
        var leArquivo = new File(caminho2);
        try{
            var arquivo = new FileWriter(leArquivo, false);
            arquivo.flush();
            arquivo.write(String.valueOf(filme1.getId()+","+filme1.getNome()+","+filme1.getVotos()+","+filme1.getRating() +"\n"));
            arquivo.write(String.valueOf(filme2.getId()+","+filme2.getNome()+","+filme2.getVotos()+","+filme2.getRating()+"\n"));
            arquivo.close();
            } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
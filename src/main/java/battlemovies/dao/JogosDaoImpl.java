package battlemovies.dao;

import battlemovies.modelo.Jogos;
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
public class JogosDaoImpl {
    @Value("${jogosFile}")
    private String caminho;
    private Path path;
    private List<Jogos> registroLinhas = new ArrayList<>();

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

    public void atualizaJogo(Jogos jogador){
        linhaEmJogo();
        if(checkJogadorExist(jogador.getLogin())) {
            gravaJogos(jogador);
        } else {
            registroLinhas.add(jogador);
            gravaJogos(registroLinhas.get(0));
        }

    }



    public void linhaEmJogo() {
        try (Stream<String> streamLinhas = Files.lines(Path.of(caminho))) {
            registroLinhas = streamLinhas
                    .filter(Predicate.not(String::isEmpty))
                    .map(Jogos::new)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Jogos jogoPendenteExist(String login){
        linhaEmJogo();
        for (Jogos jogador : registroLinhas) {
            if (jogador.getLogin().equals(login)) {
                return jogador;
            }
        }
        return new Jogos();
    }

    public void fimDeJogo(){
        try {
            Files.newBufferedWriter(path , StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean checkJogadorExist(String login){
        linhaEmJogo();
        for (Jogos registroLinha : registroLinhas) {
            if (registroLinha.getLogin().equals(login)) {
                return true;
            }
        }
        return false;
    }

    public void gravaJogos(Jogos jogador){
        try (BufferedWriter bf = Files.newBufferedWriter(path, StandardOpenOption.CREATE)) {
            bf.write(formatar(jogador));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String formatar(Jogos jogador) {
        return String.format("%s,%d,%d\r%n",jogador.getLogin(),jogador.getContador(),jogador.getJogadas());
    }
}

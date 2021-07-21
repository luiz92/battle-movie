package battlemovies.dao;

import battlemovies.modelo.Jogos;
import org.springframework.stereotype.Component;
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

@Component
public class JogosDaoImpl {
    private String caminho = "src\\main\\java\\battlemovies\\files\\jogos.csv";
    private Path path;
    private List<Jogos> registroLinhas = new ArrayList<>();

    @PostConstruct
    public void init(){
        path = Paths.get(caminho);
    }

    //Atualiza a cada jogada do usuario
    public Jogos atualizaJogo(Jogos jogador){
        atualizaListPontos();
        if(validaUsuario(jogador.getLogin())) {
            gravaJogos(jogador);
            return jogador;
        } else {
            registroLinhas.add(jogador);
            gravaJogos(registroLinhas.get(0));
            return jogador;
        }
    }

    //Continua um jogo pendente do usuario, até ele atingir 3 de vida
    public Jogos continuaJogoPendente(String login){
        atualizaListPontos();
        for (Jogos registroLinha : registroLinhas) {
            if (registroLinha.getLogin().equals(login)) {
                return registroLinha;
            }
        }
        return null;
    }

    //Zera o arquivo para um novo jogador
    public Jogos fimDeJogo(Jogos jogador){
        try {
            Files.newBufferedWriter(path , StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jogador;
    }

    public List atualizaListPontos() {
        try (Stream<String> streamLinhas = Files.lines(Path.of(caminho))) {
            registroLinhas = streamLinhas
                    .filter(Predicate.not(String::isEmpty))
                    .map(Jogos::new)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return registroLinhas;
    }

    public boolean validaUsuario(String login){
        atualizaListPontos();
        for (Jogos registroLinha : registroLinhas) {
            if (registroLinha.getLogin().equals(login)) {
                return true;
            }
        }
        return false;
    }

    public Jogos gravaJogos(Jogos jogador){
        try (BufferedWriter bf = Files.newBufferedWriter(path, StandardOpenOption.CREATE)) {
            bf.write(formatar(jogador));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jogador;
    }

    public String formatar(Jogos jogador) {
        return String.format("%s,%d,%d\r\n",jogador.getLogin(),jogador.getContador(),jogador.getJogadas());
    }
}

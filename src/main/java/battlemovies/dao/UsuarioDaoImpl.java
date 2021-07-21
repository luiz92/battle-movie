package battlemovies.dao;

import battlemovies.modelo.Usuario;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import java.io.BufferedWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class UsuarioDaoImpl {
    private String caminho = "src\\main\\java\\battlemovies\\files\\jogadores.csv";
    private Path path;
    private List<Usuario> registroLinhas = new ArrayList<>();

    @PostConstruct
    public void init(){
        path = Paths.get(caminho);
    }

    public Usuario adicionar(Usuario usuario){
        try (BufferedWriter bf = Files.newBufferedWriter(path, StandardOpenOption.APPEND)) {
            bf.write(formatar(usuario));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return usuario;
    }

    public List linhaEmUsuario() {
        try (Stream<String> streamLinhas = Files.lines(Path.of(caminho))) {
            registroLinhas = streamLinhas
                    .filter(Predicate.not(String::isEmpty))
                    .map(Usuario::new)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return registroLinhas;
    }

    public String formatar(Usuario usuario) {
        return String.format("%s,%s\r\n",usuario.getNome(),cript(usuario.getSenha()));
    }

    //criptografa senha
    public String cript(String senha){
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("sha-1");
            messageDigest.reset();
            final var plainPassword = senha;
            messageDigest.update(plainPassword.getBytes(StandardCharsets.UTF_8));
            final var crypto = new BigInteger(1, messageDigest.digest()).toString(16);
            return crypto;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Usuario> getAll() {
        linhaEmUsuario();
        return registroLinhas;
    }
}

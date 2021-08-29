package battlemovies.dao;

import battlemovies.exceptions.UsuarioIncorretoException;
import battlemovies.modelo.Usuario;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
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

@Repository
public class UsuarioDaoImpl {
    @Value("${jogadoresFile}")
    private String caminho;
    private Path path;
    private List<Usuario> registroLinhas = new ArrayList<>();

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

    public void linhaEmUsuario() {
        try (Stream<String> streamLinhas = Files.lines(Path.of(caminho))) {
            registroLinhas = streamLinhas
                    .filter(Predicate.not(String::isEmpty))
                    .map(Usuario::new)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void adicionar(Usuario usuario){
        try (BufferedWriter bf = Files.newBufferedWriter(path, StandardOpenOption.APPEND)) {
            bf.write(formatar(usuario));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String formatar(Usuario usuario) {
        return String.format("%s;%s\r%n",usuario.getLogin(),cript(usuario.getSenha()));
    }

    public String cript(String senha){
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("sha-1");
            messageDigest.reset();
            messageDigest.update(senha.getBytes(StandardCharsets.UTF_8));
            return new BigInteger(1, messageDigest.digest()).toString(16);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean loginExist(String login){
        linhaEmUsuario();
        for (Usuario registroLinha: registroLinhas){
            if(registroLinha.getLogin().equals(login)){
                return true;
            }
        }
        return false;
    }

    public boolean validaUserPass(String login, String senha) {
        linhaEmUsuario();
        for (Usuario registroLinha : registroLinhas) {
            if (registroLinha.getLogin().equals(login) && registroLinha.getSenha().equals(cript(senha))) {
                    return true;
                }
            }
        throw new UsuarioIncorretoException();
    }

}

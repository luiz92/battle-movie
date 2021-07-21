package battlemovies.servicos;

import battlemovies.dao.UsuarioDaoImpl;
import battlemovies.modelo.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class UsuarioServiceImpl{
    @Autowired
    private UsuarioDaoImpl usuarioDaoImpl;

    //Verifica se o usuário existe
    public boolean validaUsuario(String login, String senha){
        var listaUsuarios = usuarioDaoImpl.getAll();
        for (battlemovies.modelo.Usuario listaUsuario : listaUsuarios) {
            if (listaUsuario.getNome().equals(login)) {
                if ( listaUsuario.getSenha().equals(usuarioDaoImpl.cript(senha))){
                    return true;
                }
            }
        }
        return false;
    }

    //Verifica se os dados correspondem as regras
    public boolean validarCriacao(Usuario usuario){
        if (verificaCaracterEspecial(usuario.getNome())){
            if (verificaCaracterEspecial(usuario.getSenha())){
                if (usuario.getNome().length() >= 5 && usuario.getNome().length() <= 10 && usuario.getSenha().length() >= 4 && usuario.getSenha().length() <= 8){
                    return true;
                }
            }
        }
        return false;
    }

    //Verificação de caractere especial e espaço
    public boolean verificaCaracterEspecial(String stringDado) {
        if (stringDado == null || stringDado.trim().isEmpty()) {
            return false;
        }
        Pattern p = Pattern.compile("[^A-Za-z0-9]");
        Matcher m = p.matcher(stringDado);
        boolean b = m.find();
        if (b == true)
            return false;
        else
            return true;
    }

}

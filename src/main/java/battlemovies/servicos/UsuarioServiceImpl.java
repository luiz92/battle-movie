package battlemovies.servicos;

import battlemovies.dao.UsuarioDaoImpl;
import battlemovies.exceptions.LoginEmUsoException;
import battlemovies.exceptions.NaoAtendeOsRequisitosException;
import battlemovies.modelo.Usuario;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.regex.Pattern;

@RequiredArgsConstructor
@Service
public class UsuarioServiceImpl {
    private final UsuarioDaoImpl usuarioDao;

    public String cadastro(Usuario usuario) {
        if (!usuarioDao.loginExist(usuario.getLogin())) {
            if (validaRequisitos(usuario)) {
                usuarioDao.adicionar(usuario);
                return "Usuário criado com sucesso!";
            }
            throw new NaoAtendeOsRequisitosException();
        }
        throw new LoginEmUsoException();
    }

    public boolean validaUserPass(String login, String senha) {
        return usuarioDao.validaUserPass(login, senha);
    }

    public boolean validaRequisitos(Usuario usuario) {
        return validaLogin(usuario.getLogin()) && validaSenha(usuario.getSenha());
    }

    public boolean validaLogin(String stringDado) {
        if (stringDado == null) return false;
        Pattern check = Pattern.compile("^[a-zA-Z0-9]{5,10}$");
        return check.matcher(stringDado).find();
    }

    public boolean validaSenha(String stringDado) {
        if (stringDado == null) return false;
        Pattern check = Pattern.compile("^[a-zA-Z0-9]{4,8}$");
        return check.matcher(stringDado).find();
    }

}

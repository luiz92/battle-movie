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
    private UsuarioDaoImpl usuarioDao;

    //Realiza a criação do usuário, caso atenda as condições
    public String criarUsuario(Usuario usuario){
        if(!verificaExistenciaUsuario(usuario.getNome(), usuario.getSenha())) {
            if (validaRequisitos(usuario)) {
                usuarioDao.adicionar(usuario);
                return "Usuário criado com sucesso!";
            }
            return "Não atende os requisitos de criação:\n- Nome = 5 a 10 caracteres\n- Senha = 4 a 8 caracteres\n- Proibido espaço e caractere especial";
        }
        return "Usuário já existe!";
    }

    //Verifica se o usuário existe
    public boolean verificaExistenciaUsuario(String login, String senha){
        var listaUsuarios = usuarioDao.getAll();
        for (battlemovies.modelo.Usuario listaUsuario : listaUsuarios) {
            if (listaUsuario.getNome().equals(login)) {
                if ( listaUsuario.getSenha().equals(usuarioDao.cript(senha))){
                    return true;
                }
            }
        }
        return false;
    }

    //Verifica se os dados correspondem as regras
    public boolean validaRequisitos(Usuario usuario){
        if (verificaRequisitosEspeciais(usuario.getNome())){
            if (verificaRequisitosEspeciais(usuario.getSenha())){
                if (usuario.getNome().length() >= 5 &&
                        usuario.getNome().length() <= 10 &&
                        usuario.getSenha().length() >= 4 &&
                        usuario.getSenha().length() <= 8){
                    return true;
                }
            }
        }
        return false;
    }

    //Verificação de caractere especial e espaço
    public boolean verificaRequisitosEspeciais(String stringDado) {
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

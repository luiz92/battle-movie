package battlemovies.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UsuarioIncorretoException extends RuntimeException {
    public UsuarioIncorretoException(){
        super ("Dados de usuário incorretos!");
    }
}

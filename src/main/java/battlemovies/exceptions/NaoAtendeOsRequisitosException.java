package battlemovies.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NaoAtendeOsRequisitosException extends RuntimeException {
    public NaoAtendeOsRequisitosException(){
        super( "Não atende os requisitos de criação:%n" +
                "- Nome = 5 a 10 caracteres%n" +
                "- Senha = 4 a 8 caracteres%n" +
                "- Proibido espaço e caractere especial");
    }
}

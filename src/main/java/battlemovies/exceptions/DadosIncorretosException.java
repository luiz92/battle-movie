package battlemovies.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class DadosIncorretosException extends RuntimeException {
    public DadosIncorretosException(){
        super ("Dados incorretos, verifique e tente novamente!");
    }
}

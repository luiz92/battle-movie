package battlemovies.modelo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Usuario {
    private String login;
    private String senha;

    public Usuario(String txtLinhas) {
        String[] split = txtLinhas.split(";");
        this.login = split[0];
        this.senha = split[1];
    }
}

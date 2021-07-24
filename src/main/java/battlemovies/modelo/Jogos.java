package battlemovies.modelo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Jogos {
    private String login;
    private int contador = 0;
    private int jogadas = 0;

    public Jogos(String txtLinhas) {
        String[] split = txtLinhas.split(",");
        this.login = split[0];
        this.contador= Integer.parseInt(split[1]);
        this.jogadas = Integer.parseInt(split[2]);

    }
}

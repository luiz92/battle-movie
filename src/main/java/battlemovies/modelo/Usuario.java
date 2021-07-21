package battlemovies.modelo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Usuario {
    private String nome;
    private String senha;

    public Usuario(String txtLinhas) {
        String[] split = txtLinhas.split(",");
        this.nome = split[0].trim().replace("\"", "");
        this.senha = split[1].trim().replace("\"", "");
    }
}

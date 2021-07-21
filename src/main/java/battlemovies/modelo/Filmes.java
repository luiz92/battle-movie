package battlemovies.modelo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Filmes {
    private String id;
    private String nome;
    private Long votos;
    private Double rating;

    public Filmes(String txtLinhas) {
        String[] split = txtLinhas.split(",");
        this.id = split[0].trim().replace("\"", "");
        this.nome = split[1].trim().replace("\"", "");
        this.votos= Long.parseLong(split[2].trim().replace("\"", ""));
        this.rating= Double.parseDouble(split[3].trim().replace("\"", ""));
    }
}
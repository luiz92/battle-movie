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

    public Filmes(String id, String nome){
        this.id = id;
        this.nome = nome;
    }

    public Filmes(String txtLinhas) {
        String[] split = txtLinhas.split(",");
        this.id = split[0];
        this.nome = split[1];
        this.votos= Long.parseLong(split[2]);
        this.rating= Double.parseDouble(split[3].replace("\"", ""));
    }
}
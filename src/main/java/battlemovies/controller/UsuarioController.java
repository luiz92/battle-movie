package battlemovies.controller;

import battlemovies.dao.UsuarioDaoImpl;
import battlemovies.modelo.*;
import battlemovies.servicos.UsuarioServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/usuario")
@RestController
public class UsuarioController {
    @Autowired
    private UsuarioDaoImpl usuarioDao;

    @Autowired
    private UsuarioServiceImpl usuarioService;

//  GET > http://localhost:8080/usuario/
    @GetMapping
    public String mensagem(){
        return "Cadastre seu usuario!";
    }

//  POST >  http://localhost:8080/usuario/
//  body > raw > JSON > {"nome": "LuizAlves", "senha": "Senha123"}
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String getNovoUsuario(@RequestBody Usuario usuario) {
        return usuarioService.criarUsuario(usuario);
    }

}

package br.upe.aula.controlepeso.controle;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.upe.aula.controlepeso.entidade.Historico;
import br.upe.aula.controlepeso.entidade.Usuario;
import br.upe.aula.controlepeso.servico.UsuarioServico;

@RestController
@RequestMapping("/api/v1")
public class ControlePesoAPI {

    @Autowired
    private UsuarioServico usuarioServico;

    @GetMapping("/usuarios")
    @ResponseStatus(HttpStatus.OK)
    public List<Usuario> listarUsuario() {
        return this.usuarioServico.listar();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Usuario incluirUsuario(@RequestBody Usuario usuario) {
        return this.usuarioServico.incluir(usuario);
    }

    @DeleteMapping("usuario/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void apagarUsuario(@PathVariable Long id) {
        this.usuarioServico.apagarUsuario(id);
    }

    @PutMapping("usuario/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Usuario alterarUsuario(@PathVariable Long id, @RequestBody Usuario usuario) {
        usuario.setId(id);
        return this.usuarioServico.alterar(usuario);
    }

    @PutMapping("usuario/novopeso/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Usuario alterarPesoUsuario(@PathVariable Long id, @RequestBody Usuario usuario) {
        usuario.setId(id);
        return this.usuarioServico.atualizarPeso(usuario);
    }

    @PostMapping("/logar")
    public Usuario logar(@RequestParam(value = "email") String email) {
        return this.usuarioServico.logar(email);
    }

    @GetMapping("/historico/{id}")
    @ResponseStatus(HttpStatus.OK)
    public List<Historico> exibirHistorico(@PathVariable Long id) {
        return this.usuarioServico.exibirHistorico(id);
    }

    @GetMapping("/imc/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Double calcularIMC(@PathVariable Long id) {
        return this.usuarioServico.calcularIMC(id);

    }

    // @GetMapping("/comparativo/{id}")
    // @ResponseStatus(HttpStatus.OK)
    // public Double comparativo(@PathVariable Long id) {
    // return this.usuarioServico.comparativo(id);
    // }

}

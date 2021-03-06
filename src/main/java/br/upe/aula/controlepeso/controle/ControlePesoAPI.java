package br.upe.aula.controlepeso.controle;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
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

import br.upe.aula.controlepeso.modelo.VO.MonitoramentoVO;
import br.upe.aula.controlepeso.modelo.entidade.Usuario;
import br.upe.aula.controlepeso.servico.PesoService;
import br.upe.aula.controlepeso.servico.UsuarioServico;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin("*")
public class ControlePesoAPI {

    @Autowired
    private UsuarioServico usuarioServico;

    @Autowired
    private PesoService pesoServico;

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
    public Usuario alterarPesoUsuario(@PathVariable Long id, @RequestParam(value = "peso") Double peso) {
        return this.usuarioServico.atualizarPeso(id, peso);
    }

    @GetMapping("/usuario/email/{email}")
    public Usuario buscarUsuario(@PathVariable String email) {
        return this.usuarioServico.buscarUsuario(email);
    }

    // @PostMapping("/logar")
    // public Usuario logar(@RequestParam(value = "email") String email) {
    // return this.usuarioServico.logar(email);
    // }

    @GetMapping("/imc/{email}")
    public MonitoramentoVO calcularIMC(@PathVariable String email) {
        return this.pesoServico.calcularIMC(email);
    }

    @GetMapping("/comparativo/{email}")
    public MonitoramentoVO comparativo(@PathVariable String email) {
        return this.pesoServico.comparativo(email);
    }

}

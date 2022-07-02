package br.upe.aula.controlepeso.servico;

import java.util.List;

import java.util.Optional;

import javax.mail.internet.InternetAddress;

import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.upe.aula.controlepeso.modelo.entidade.Peso;
import br.upe.aula.controlepeso.modelo.entidade.Usuario;
import br.upe.aula.controlepeso.repositorio.PesoRepositorio;
import br.upe.aula.controlepeso.repositorio.UsuarioRepositorio;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UsuarioServico {

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Autowired
    private PesoRepositorio pesoRepositorio;

    public Usuario incluir(Usuario usuario) {
        usuario.setId(null);
        usuario.setPesoAtual(usuario.getPesoInicial());
        usuario.setDataInicial(LocalDate.now());

        this.validarPreenchimentoCamposObrigatorios(usuario);

        Usuario novoUsuario = usuarioRepositorio.save(usuario);
        this.adicionarAoHistorico(novoUsuario);
        return novoUsuario;
    }

    public List<Usuario> listar() {
        return usuarioRepositorio.findAll();
    }

    public void apagarUsuario(Long id) {

        if (id == null || id == 01) {
            throw new RuntimeException("Informe um identificador de usuário.");
        }

        if (!this.usuarioRepositorio.existsById(id)) {
            throw new RuntimeException("Não existe usuario cadastrado com o identificador: " + id);
        }
        this.usuarioRepositorio.deleteById(id);
    }

    public Usuario alterar(Usuario usuario) {

        if (usuario == null) {
            throw new RuntimeException("Informe os dados do usuário");
        }

        if (usuario.getId() == null || usuario.getId() == 0) {
            throw new RuntimeException("Informe um identificador de usuário.");
        }

        Optional<Usuario> usuarioBase = this.usuarioRepositorio.findById(usuario.getId());

        if (usuarioBase.isEmpty()) {
            throw new RuntimeException("Não existe usuario cadastrado com o identificador" + usuario.getId());
        }

        if (!this.usuarioRepositorio.existsById(usuario.getId())) {
            throw new RuntimeException("Não existe usuario cadastrado com o identificador: " + usuario.getId());
        }

        return usuarioRepositorio.save(usuario);
    }

    public Usuario atualizarPeso(Long id, Double peso) {

        if (id == null) {
            throw new RuntimeException("Informe um identificador de usuário.");
        }

        if (!this.usuarioRepositorio.existsById(id)) {
            throw new RuntimeException("Não existe usuario cadastrado com o identificador: " + id);
        }

        if (peso == null || peso < 30) {
            throw new RuntimeException("Peso não pode ser vazio ou menor que 30kg. Informe um peso válido!");
        }

        Optional<Usuario> usuarioBase = this.usuarioRepositorio.findById(id);

        usuarioBase.get().setPesoInicial(peso);

        this.usuarioRepositorio.save(usuarioBase.get());
        this.adicionarAoHistorico(usuarioBase.get());
        return usuarioBase.get();
    }

    public void adicionarAoHistorico(Usuario usuario) {
        Peso peso = new Peso();
        peso.setId(null);
        peso.setPeso(usuario.getPesoInicial());
        peso.setData(LocalDate.now());
        peso.setUsuario(usuario);

        this.pesoRepositorio.save(peso);
    }

    public List<Peso> exibirHistorico(Long id) {

        if (id == null || id == 01) {
            throw new RuntimeException("Informe um identificador de usuário.");
        }

        if (!this.usuarioRepositorio.existsById(id)) {
            throw new RuntimeException("Não existe usuario cadastrado com o identificador: " + id);
        }

        return this.pesoRepositorio.findAll();
    }

    public Usuario buscarUsuario(String email) {

        if (email.isEmpty() || email == null) {
            throw new RuntimeException("Informe um email valido");
        }
        Optional<Usuario> usuario = this.usuarioRepositorio.findFirstByEmailIgnoreCase(email.toUpperCase());

        if (usuario == null) {
            throw new RuntimeException("Este e-mail não está cadastrado");
        }

        return usuario.get();
    }

    private void validarPreenchimentoCamposObrigatorios(Usuario usuario) {

        if (usuario.getNome().trim().isEmpty() ||
                usuario.getEmail().trim().isEmpty() || usuario.getAltura() == 0
                || usuario.getPesoInicial() == 0 || usuario.getPesoDesejado() == 0
                || usuario.getDataObjetivo() == null) {
            log.error("Os campos do usuário não podem ser vazios");
            throw new RuntimeException(
                    "Os campos: Nome, E-mail, Altura, Peso Inicial, Peso Desejado e Peso Objetivo não podem ser vazios ou não ter valor. Verifique os campos e tente novamente!");
        }

        this.validarEmail(usuario.getEmail());

        if (usuario.getGenero() == null) {
            log.error("O campo Genero do usuário não pode ser vazio");
            throw new RuntimeException("O Genero não pode ser vazio. Digite um Genero valido!");
        }

        if (usuario.getAltura() <= 100) {
            log.error("O campo Altura do usuário não pode ser menor que 100 centimentros");
            throw new RuntimeException("A Altura não pode ser menor que 100 centimentros. Digite uma Altura valida!");
        }

        if (usuario.getPesoInicial() <= 30) {
            log.error("O campo Peso Inicial do usuário não pode ser menor que 30Kg");
            throw new RuntimeException(
                    "O campo Peso Inicial do usuario não pode ser menor que 30Kg. Digite um peso valido!");
        }

        if (usuario.getDataInicial().isAfter(usuario.getDataObjetivo())) {
            log.error("O campo Data Objetivo do usuario não pode ser menor que data atual!");
            throw new RuntimeException(
                    "Data objetivo invalida. O campo Data Objetivo do usuario não pode ser menor que data atual!");
        }

        if (Days.daysBetween(usuario.getDataInicial(), usuario.getDataObjetivo()).getDays() < 7) {
            log.error("Data Objetivo invalida. Informe uma data valida!");
            throw new RuntimeException(
                    "Campo data objetivo invalido. O campo data objetivo deve ser de no minimo uma semana!");
        }
    }

    private void validarEmail(String email) {
        try {
            InternetAddress emailValidar = new InternetAddress(email);
            emailValidar.validate();
        } catch (Exception e) {
            throw new RuntimeException("E-mail invalido infome um email bslido!", e);
        }
    }

}
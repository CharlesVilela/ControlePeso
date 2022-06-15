package br.upe.aula.controlepeso.servico;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import javax.mail.internet.InternetAddress;

import org.apache.tomcat.util.buf.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties.Undertow.Options;
import org.springframework.stereotype.Service;

import br.upe.aula.controlepeso.modelo.entidade.Peso;
import br.upe.aula.controlepeso.modelo.entidade.Usuario;
import br.upe.aula.controlepeso.repositorio.PesoRepositorio;
import br.upe.aula.controlepeso.repositorio.UsuarioRepositorio;

@Service
public class UsuarioServico {

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Autowired
    private PesoRepositorio pesoRepositorio;

    public Usuario incluir(Usuario usuario) {
        usuario.setId(null);
        usuario.setPesoAtual(usuario.getPesoInicial());
        usuario.setDataInicial(LocalDate.now());

        String mensagem = this.validarCamposUsuario(usuario);

        System.out.println("Aqui a mensagem recebida: " + mensagem);

        if (mensagem != "Ok") {
            throw new RuntimeException(mensagem);
        } else {
            System.out.println(mensagem);
            Usuario novoUsuario = usuarioRepositorio.save(usuario);

            this.adicionarAoHistorico(novoUsuario);
            return novoUsuario;
        }
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

    public Usuario atualizarPeso(Usuario usuario) {
        if (usuario == null) {
            throw new RuntimeException("Informe os dados do usuário");
        }

        if (usuario.getId() == null || usuario.getId() == 0) {
            throw new RuntimeException("Informe um identificador de usuário.");
        }

        if (!this.usuarioRepositorio.existsById(usuario.getId())) {
            throw new RuntimeException("Não existe usuario cadastrado com o identificador: " + usuario.getId());
        }

        usuario.setDataInicial(LocalDate.now());

        String mensagem = "Ok";// this.validarCamposUsuario(usuario);

        this.usuarioRepositorio.save(usuario);
        this.adicionarAoHistorico(usuario);
        return usuario;
    }

    public Usuario logar(String email) {
        return usuarioRepositorio.logar(email);
    }

    public void adicionarAoHistorico(Usuario usuario) {
        Peso peso = new Peso();
        peso.setId(null);
        peso.setPeso(usuario.getPesoInicial());
        peso.setData(usuario.getDataInicial());
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

    public Double calcularIMC(Long id) {
        // Menor que 18,5 = abaixo do peso.
        // Entre 18,5 e 24,9 = peso normal.
        // Entre 25 e 29,9 = sobrepeso.
        // Entre 30 e 34,99 = obesidade grau I.
        // Entre 35 e 39,99 = obesidade grau II (severa).
        // Acima de 40 = obesidade grau III (mórbida).
        // IMC = Massa (kg) ÷ Altura (m)².

        Usuario usuario = this.usuarioRepositorio.buscarUsuarioPorId(id);

        int altura = usuario.getAltura();
        Double alturaConvertidaMetros = (Double.valueOf(altura) / 100);

        Double IMC = usuario.getPesoInicial() / (alturaConvertidaMetros * alturaConvertidaMetros);

        return IMC;
    }

    // public Double comparativo(Long id) {

    // List<Historico> historico = this.historicoRepositorio.exibirHistorico(id);

    // LocalDate now = LocalDate.now();

    // LocalDate ultimaPeso = null;

    // for (int i = 1; i <= historico.size(); i++) {

    // if (historico.get(i -
    // 1).getDataInsercao().isBefore(historico.get(i).getDataInsercao())) {
    // ultimaPeso = historico.get(i).getDataInsercao();
    // }

    // }

    // System.out.println(ultimaPeso);

    // return null;

    // }

    public String validarCamposUsuario(Usuario usuario) {

        String mensagem = "Ok";

        // // 1. Preenchimento dos campos obrigatorios
        if (usuario.getNome().trim().isEmpty() ||
                usuario.getEmail().trim().isEmpty())
            mensagem = "Os campos Nome ou E-mail não podem ser vazios. Verifique e tente novamente!";

        if (usuario.getGenero() == null)
            mensagem = "O campo Genêro não pode ser vazio. Verifique e tente novamente!";

        if (usuario.getAltura() == null)
            mensagem = "O campo Altura não pode ser vazio. Verifique e tente novamente!";

        if (usuario.getPesoInicial() == null || usuario.getPesoDesejado() == null)
            mensagem = "Os campos Peso Inicial ou Peso Desejado não podem ser vazio. Verifique e tente novamente!";

        if (usuario.getDataObjetivo() == null)
            mensagem = "O campo Data Objetivo não pode ser vazio. Verifique e tente novamente!";

        // 2. Verificar validade do email
        if (this.validarEmail(usuario.getEmail()) == false)
            mensagem = "E-mail invalido. Informe um endereço de e-mail válido!";

        // 3. Validar se existe email já cadastrado
        Usuario verificarEmail = this.usuarioRepositorio.logar(usuario.getEmail());
        if (verificarEmail != null)
            mensagem = "O E-mail que você tentou se cadastrar já existe!";

        // 4. A altura deve ser maior do que 100 camada
        if (usuario.getAltura() <= 100)
            mensagem = "Altura inválida. A altura deve ser maior que 100 centimentros!";

        // 5. Peso inicial deve ser maior que 30Kg
        if (usuario.getPesoInicial() <= 30)
            mensagem = "Peso inicial inválido. O peso inicial deve ser maior que 30Kg!";

        // 6. Verificar se a data do peso objetivo deve ser no minimo de uma semana
        LocalDate dataAtual = LocalDate.now();

        Period periodo = Period.between(usuario.getDataObjetivo(), dataAtual);

        if (periodo.getDays() < 7)
            mensagem = "A data objetivo deve ser de pelo menos uma semana!";

        return mensagem;
    }

    public boolean validarEmail(String email) {
        boolean result = true;
        try {
            InternetAddress emailValidar = new InternetAddress(email);
            emailValidar.validate();
        } catch (Exception e) {
            result = false;
        }
        return result;
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

}

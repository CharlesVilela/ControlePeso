package br.upe.aula.controlepeso.servico;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.upe.aula.controlepeso.modelo.VO.IMCEnum;
import br.upe.aula.controlepeso.modelo.VO.IMCVO;
import br.upe.aula.controlepeso.modelo.VO.MonitoramentoVO;
import br.upe.aula.controlepeso.modelo.entidade.Peso;
import br.upe.aula.controlepeso.modelo.entidade.Usuario;
import br.upe.aula.controlepeso.repositorio.PesoRepositorio;
import br.upe.aula.controlepeso.repositorio.UsuarioRepositorio;

@Service
public class PesoService {

    @Autowired
    private PesoRepositorio pesoRepositorio;

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    public MonitoramentoVO calcularIMC(String email) {

        MonitoramentoVO retorno = new MonitoramentoVO();

        Optional<Usuario> optUSUARIO = usuarioRepositorio.findFirstByEmailIgnoreCase(email);

        if (optUSUARIO.isEmpty()) {
            throw new RuntimeException("Usuario não cadastrado!");
        }

        double altura = (Double.valueOf(optUSUARIO.get().getAltura()) / 100);

        Peso optPeso = pesoRepositorio.findFirstByUsuarioEmail(email);

        if (optPeso == null) {
            throw new RuntimeException("Usuario não tem peso cadastrado!");
        }

        double peso = optPeso.getPeso();

        double IMC = peso / (altura * altura);

        System.out.println(IMC);

        IMCEnum grau = IMCEnum.ABAIXO_DO_PESO;

        if (IMC < 18.5) {
            grau = IMCEnum.ABAIXO_DO_PESO;
        } else if (IMC >= 18.5 && IMC < 25) {
            grau = IMCEnum.PESO_IDEAL;
        } else if (IMC >= 25) {
            grau = IMCEnum.ACIMA_DO_PESO;
        }

        retorno.setIMC(IMCVO.builder().IMC(IMC).classificacao(grau).build());

        return retorno;
    }

    public MonitoramentoVO comparativo(String email) {

        MonitoramentoVO retorno = new MonitoramentoVO();

        Optional<Usuario> optUsuario = this.usuarioRepositorio.findFirstByEmailIgnoreCase(email);

        if (optUsuario.isEmpty()) {
            throw new RuntimeException("Usuario não cadastrado!");
        }

        List<Peso> optPesos = this.pesoRepositorio.findAllPesos(optUsuario.get().getId());

        // Collections.sort(slist, Collections.reverseOrder());

        for (int i = 0; i < optPesos.size(); i++) {
            System.out.println(optPesos.get(i).getPeso() + ", " +
                    optPesos.get(i).getData());
        }

        return null;
    }

}

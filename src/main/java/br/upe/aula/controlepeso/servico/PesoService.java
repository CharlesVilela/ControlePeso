package br.upe.aula.controlepeso.servico;

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

    private MonitoramentoVO montarMonitoramento(String email) {

        MonitoramentoVO retorno = new MonitoramentoVO();

        Optional<Usuario> optUSUARIO = usuarioRepositorio.findFirstByEmailIgnoreCase(email);

        if (optUSUARIO.isEmpty()) {
            throw new RuntimeException("Usuario não cadastrado!");
        }

        double altura = optUSUARIO.get().getAltura() / 100;

        Optional<Peso> optPeso = pesoRepositorio.findFirstByUsuarioEmailOrderDataDesc(email);

        if (optPeso.isEmpty()) {
            throw new RuntimeException("Usuario não tem peso cadastrado!");
        }

        double peso = optPeso.get().getPeso();

        double IMC = peso / (altura * altura);

        IMCEnum grau = IMCEnum.ABAIXO_DO_PESO;

        // Finalizar o IF
        // if(IMC < 19){

        // }

        // retorno.setIMC(IMCVO.builder().IMC(IMC).classificacao(grau).build());

        return retorno;
    }

}

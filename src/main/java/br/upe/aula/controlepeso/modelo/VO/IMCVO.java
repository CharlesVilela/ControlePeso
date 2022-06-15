package br.upe.aula.controlepeso.modelo.VO;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class IMCVO {

    private Double IMC;

    private IMCEnum classificacao;

}

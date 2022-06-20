package br.upe.aula.controlepeso.modelo.VO;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ComparativoVO {

    private Double valor;
    private ComparativoEnum tempo;
    private Double pesoInicial;

}

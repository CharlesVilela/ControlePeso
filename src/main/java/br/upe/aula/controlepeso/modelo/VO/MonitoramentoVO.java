package br.upe.aula.controlepeso.modelo.VO;

import java.util.List;

import lombok.Data;

@Data
public class MonitoramentoVO {

    private IMCVO IMC;
    private List<ComparativoVO> comparativo;

}

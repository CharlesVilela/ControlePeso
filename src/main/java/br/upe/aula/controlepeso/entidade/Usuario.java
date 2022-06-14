package br.upe.aula.controlepeso.entidade;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import lombok.Data;

@Entity
@Data
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String nome;
    private String email;
    private Integer altura;

    @Enumerated(EnumType.STRING)
    private Genero genero;

    private Double pesoInicial;
    private Double pesoDesejado;
    private LocalDate dataInicial;
    private LocalDate dataObjetivo;

}

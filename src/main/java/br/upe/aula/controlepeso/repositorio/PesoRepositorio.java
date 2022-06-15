package br.upe.aula.controlepeso.repositorio;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.upe.aula.controlepeso.modelo.entidade.Peso;

public interface PesoRepositorio extends JpaRepository<Peso, Long> {

    public Optional<Peso> findFirstByUsuarioEmailOrderDataDesc(String email);

}

package br.upe.aula.controlepeso.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.upe.aula.controlepeso.modelo.entidade.Peso;

@Repository
public interface PesoRepositorio extends JpaRepository<Peso, Long> {

    public Peso findFirstByUsuarioEmail(String email);

}

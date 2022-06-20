package br.upe.aula.controlepeso.repositorio;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.upe.aula.controlepeso.modelo.entidade.Peso;

@Repository
public interface PesoRepositorio extends JpaRepository<Peso, Long> {

    public Peso findFirstByUsuarioEmail(String email);

    @Query(value = "SELECT * FROM peso WHERE usuario_id = ?", nativeQuery = true)
    public List<Peso> findAllPesos(Long usuario_id);

}

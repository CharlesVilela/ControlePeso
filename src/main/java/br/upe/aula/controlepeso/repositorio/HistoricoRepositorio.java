package br.upe.aula.controlepeso.repositorio;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.upe.aula.controlepeso.entidade.Historico;

public interface HistoricoRepositorio extends JpaRepository<Historico, Long> {

    @Query(value = "SELECT * FROM historico WHERE id_usuario = ?", nativeQuery = true)
    public List<Historico> exibirHistorico(Long id);

    @Query(value = "DELETE FROM historico WHERE id_usuario = ?", nativeQuery = true)
    public boolean deletarUsuarioHistorico(Long id);
}

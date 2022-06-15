package br.upe.aula.controlepeso.repositorio;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.upe.aula.controlepeso.modelo.entidade.Usuario;

@Repository
public interface UsuarioRepositorio extends JpaRepository<Usuario, Long> {

    // public Boolean existeEmailCadastrado(String email);

    @Query(value = "SELECT * FROM usuario WHERE email = ?", nativeQuery = true)
    public Usuario logar(String email);

    @Query(value = "SELECT * FROM usuario WHERE id = ?", nativeQuery = true)
    public Usuario buscarUsuarioPorId(Long id);

    Optional<Usuario> findFirstByEmailIgnoreCase(String email);

}

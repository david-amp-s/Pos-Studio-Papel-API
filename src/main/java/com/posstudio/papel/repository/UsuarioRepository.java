package com.posstudio.papel.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.posstudio.papel.model.Usuario;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByUsuario(String usuario);
}

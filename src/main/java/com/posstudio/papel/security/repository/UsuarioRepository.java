package com.posstudio.papel.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.posstudio.papel.security.model.Usuario;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByUsuario(String usuario);
}

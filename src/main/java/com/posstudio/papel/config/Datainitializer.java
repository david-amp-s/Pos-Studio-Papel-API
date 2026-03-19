package com.posstudio.papel.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.posstudio.papel.enums.Roles;
import com.posstudio.papel.model.Usuario;
import com.posstudio.papel.repository.UsuarioRepository;

@Configuration
public class Datainitializer {

    @Bean
    public CommandLineRunner createUser(UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder) {
        return args -> {

            if (usuarioRepository.findByUsuario("admin").isEmpty()) {

                Usuario user = Usuario.builder()
                        .nombre("Administrador")
                        .usuario("admin")
                        .contrasena(passwordEncoder.encode("123456")) // 🔥 BCrypt
                        .rol(Roles.ADMIN)
                        .activo(true)
                        .build();

                usuarioRepository.save(user);

                System.out.println("✅ Usuario admin creado");
            }
        };
    }
}

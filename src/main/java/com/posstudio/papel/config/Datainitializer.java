package com.posstudio.papel.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.posstudio.papel.common.enums.Roles;
import com.posstudio.papel.inventario.model.Categoria;
import com.posstudio.papel.inventario.model.Ubicacion;
import com.posstudio.papel.inventario.repository.CategoriaRepository;
import com.posstudio.papel.inventario.repository.UbicacionRepository;
import com.posstudio.papel.security.model.Usuario;
import com.posstudio.papel.security.repository.UsuarioRepository;

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

    @Bean
    public CommandLineRunner createUbicacionServicio(UbicacionRepository ubicacionRepository) {
        return args -> {
            if (ubicacionRepository.findByCodigo("SERVICIO").isEmpty()) {
                Ubicacion ubicacion = Ubicacion.builder()
                        .codigo("SERVICIO")
                        .build();
                ubicacionRepository.save(ubicacion);
                System.out.println("Ubicacion servicio creada");
            }
        };
    }

    @Bean
    public CommandLineRunner createCategoriaServicio(CategoriaRepository categoriaRepository) {
        return args -> {
            if (categoriaRepository.findByNombre("SERVICIO").isEmpty()) {
                Categoria categoria = Categoria.builder()
                        .nombre("SERVICIO")
                        .build();
                categoriaRepository.save(categoria);
                System.out.println("Categoria servicio Creada");
            }
        };
    }
}

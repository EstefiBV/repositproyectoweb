
package com.example.proyectoweb;

import com.example.proyectoweb.service.UsuarioDetailsService;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class ProbarAutenticacion {

    @Bean
    CommandLineRunner probarUsuario(
            UsuarioDetailsService usuarioDetailsService,
            PasswordEncoder passwordEncoder) {

        return args -> {

            System.out.println("=== PRUEBA DE SEGURIDAD ===");

            try {
                UserDetails usuario =
                        usuarioDetailsService
                                .loadUserByUsername("admin");

                System.out.println(
                        "Usuario: " + usuario.getUsername());

                System.out.println(
                        "Habilitado: " + usuario.isEnabled());

                System.out.println(
                        "Rol: " + usuario.getAuthorities());

                System.out.println(
                        "Contraseña válida: " +
                                passwordEncoder.matches(
                                        "Admin2026!Prueba",
                                        usuario.getPassword()
                                ));

            } catch (Exception e) {
                System.out.println(
                        "Error de autenticación: "
                                + e.getClass().getSimpleName());

                System.out.println(
                        "Detalle: " + e.getMessage());
            }

            System.out.println("=== FIN DE PRUEBA ===");
        };
    }
}

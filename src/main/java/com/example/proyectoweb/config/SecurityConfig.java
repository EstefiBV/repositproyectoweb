package com.example.proyectoweb.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.core.userdetails.UserDetailsService;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // CIFRADO DE CONTRASEÑAS
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // AUTENTICACIÓN DESDE SQL SERVER
    @Bean
    public AuthenticationProvider authenticationProvider(
            UserDetailsService usuarioDetailsService,
            PasswordEncoder passwordEncoder) {

        DaoAuthenticationProvider provider =
                new DaoAuthenticationProvider(usuarioDetailsService);

        provider.setPasswordEncoder(passwordEncoder);

        return provider;
    }

    // CONFIGURACIÓN DE SPRING SECURITY
    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            AuthenticationProvider authenticationProvider
    ) throws Exception {

        http

                // UTILIZAR EL PROVEEDOR DE SQL SERVER
                .authenticationProvider(authenticationProvider)

                // AUTORIZACIÓN
                .authorizeHttpRequests(auth -> auth

                        // RUTAS PÚBLICAS
                        .requestMatchers(
                                "/",
                                "/login",
                                "/error",
                                "/acceso-denegado",
                                "/favicon.ico",
                                "/css/**",
                                "/js/**",
                                "/images/**"
                        ).permitAll()

                        // USUARIOS: ADMINISTRADOR
                        .requestMatchers("/usuarios/**")
                        .hasRole("ADMIN")

                        // GUARDAR CATEGORÍAS
                        .requestMatchers(
                                HttpMethod.POST,
                                "/categorias/guardar"
                        ).hasRole("ADMIN")

                        // CONSULTAR CATEGORÍAS
                        .requestMatchers(
                                HttpMethod.GET,
                                "/categorias",
                                "/categorias/"
                        ).hasAnyRole("ADMIN", "EDITOR")

                        // OTRAS OPERACIONES DE CATEGORÍAS
                        .requestMatchers("/categorias/**")
                        .hasRole("ADMIN")

                        // ELIMINAR PRODUCTOS
                        .requestMatchers("/productos/eliminar/**")
                        .hasRole("ADMIN")

                        // REGISTRAR Y EDITAR PRODUCTOS
                        .requestMatchers(
                                "/productos/guardar",
                                "/productos/actualizar",
                                "/productos/editar/**"
                        ).hasAnyRole("ADMIN", "EDITOR")

                        // CONSULTAR PRODUCTOS
                        .requestMatchers(
                                "/productos",
                                "/productos/"
                        ).hasAnyRole(
                                "ADMIN",
                                "EDITOR",
                                "CONSULTA"
                        )

                        // RESTO DE RUTAS
                        .anyRequest().authenticated()
                )

                // INICIO DE SESIÓN
                .formLogin(form -> form

                        // PÁGINA DE LOGIN
                        .loginPage("/login")

                        // SPRING SECURITY PROCESA POST /login
                        .loginProcessingUrl("/login")

                        // CAMPOS DEL FORMULARIO
                        .usernameParameter("username")
                        .passwordParameter("password")

                        // REDIRECCIÓN EXITOSA
                        .defaultSuccessUrl("/productos", true)

                        // ERROR DE AUTENTICACIÓN
                        .failureUrl("/login?error")

                        .permitAll()
                )

                // CIERRE DE SESIÓN
                .logout(logout -> logout

                        .logoutUrl("/logout")

                        .logoutSuccessUrl("/login?logout")

                        .invalidateHttpSession(true)

                        .deleteCookies("JSESSIONID")

                        .permitAll()
                )

                // ACCESO DENEGADO
                .exceptionHandling(ex -> ex

                        .accessDeniedPage("/acceso-denegado")
                );

        return http.build();
    }
}

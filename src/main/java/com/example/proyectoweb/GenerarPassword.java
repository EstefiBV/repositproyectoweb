package com.example.proyectoweb;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class GenerarPassword {

    public static void main(String[] args) {

        BCryptPasswordEncoder encoder =
                new BCryptPasswordEncoder();

        String password = "Admin2026!Prueba";

        String hash = encoder.encode(password);

        System.out.println("Contraseña: " + password);
        System.out.println("Hash BCrypt: " + hash);
        System.out.println("Verificación: " +
                encoder.matches(password, hash));
    }
}
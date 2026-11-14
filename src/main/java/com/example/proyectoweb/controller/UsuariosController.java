
package com.example.proyectoweb.controller;

import com.example.proyectoweb.model.Usuario;
import com.example.proyectoweb.model.Rol;
import com.example.proyectoweb.repository.UsuarioRepository;
import com.example.proyectoweb.repository.RolRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/usuarios")
public class UsuariosController {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuariosController(
            UsuarioRepository usuarioRepository,
            RolRepository rolRepository,
            PasswordEncoder passwordEncoder) {

        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // MOSTRAR USUARIOS
    @GetMapping
    public String listar(Model model) {

        model.addAttribute(
                "usuarios",
                usuarioRepository.findAll()
        );

        model.addAttribute(
                "roles",
                rolRepository.findAll()
        );

        return "usuarios";
    }

    // REGISTRAR USUARIO
    @PostMapping("/guardar")
    public String guardar(
            @RequestParam String nombre,
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam Long rolId) {

        nombre = nombre.trim();
        username = username.trim();

        if (nombre.isEmpty()
                || username.isEmpty()
                || password.length() < 8) {
            return "redirect:/usuarios?error=datos";
        }

        if (usuarioRepository.findByUsername(username).isPresent()) {
            return "redirect:/usuarios?error=duplicado";
        }

        Rol rol = rolRepository.findById(rolId)
                .orElse(null);

        if (rol == null) {
            return "redirect:/usuarios?error=rol";
        }

        Usuario usuario = new Usuario();

        usuario.setNombre(nombre);
        usuario.setUsername(username);

        // CONTRASEÑA CIFRADA CON BCRYPT
        usuario.setPasswordHash(
                passwordEncoder.encode(password)
        );

        usuario.setRol(rol);
        usuario.setActivo(true);

        usuarioRepository.save(usuario);

        return "redirect:/usuarios?exito=guardado";
    }

    // ACTIVAR O DESACTIVAR USUARIO
    @PostMapping("/estado/{id}")
    public String cambiarEstado(
            @PathVariable Long id,
            Authentication authentication) {

        Usuario usuario = usuarioRepository
                .findById(id)
                .orElse(null);

        if (usuario == null) {
            return "redirect:/usuarios?error=noexiste";
        }

        // EVITAR DESACTIVAR LA PROPIA CUENTA
        if (usuario.getUsername()
                .equals(authentication.getName())) {

            return "redirect:/usuarios?error=propio";
        }

        usuario.setActivo(
                !Boolean.TRUE.equals(usuario.getActivo())
        );

        usuarioRepository.save(usuario);

        return "redirect:/usuarios?exito=estado";
    }
}

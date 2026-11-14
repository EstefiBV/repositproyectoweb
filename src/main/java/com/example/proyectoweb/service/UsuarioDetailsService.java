package com.example.proyectoweb.service;

import com.example.proyectoweb.model.Usuario;
import com.example.proyectoweb.repository.UsuarioRepository;

import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class UsuarioDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;
    public UsuarioDetailsService(UsuarioRepository usuarioRepository){
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException("Usuario no encontrado"));
        return User.builder().username(usuario.getUsername())
                .password(usuario.getPasswordHash())
                .roles(usuario.getRol().getNombre())
                .disabled(!Boolean.TRUE.equals(usuario.getActivo()))
                .build();

    }
}


package com.example.proyectoweb.model;

import jakarta.persistence.*;

@Entity
@Table(name = "usuarios")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String nombre;
    @Column(nullable = false, unique = true)
    private String username;
    @Column(name = "password_hash", nullable = false)
    private String passwordHash;
    @Column(nullable = false)
    private Boolean activo = true;

    @ManyToOne
    @JoinColumn(name="rol_id", nullable = false)
    private Rol rol;

    public Usuario() {}

    public Long getId() {return id;}
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {this.nombre = nombre;}

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {this.username = username;}

    public String getPasswordHash() {return passwordHash;}
    public void setPasswordHash(String passwordHash) {this.passwordHash = passwordHash;}

    public Boolean getActivo() {
        return activo;
    }
    public void setActivo(Boolean activo) {this.activo = activo;}

    public Rol getRol() {
        return rol;
    }
    public void setRol(Rol rol) {this.rol = rol;}
}

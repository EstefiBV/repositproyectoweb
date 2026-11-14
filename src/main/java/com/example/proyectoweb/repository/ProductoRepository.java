package com.example.proyectoweb.repository;

import com.example.proyectoweb.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductoRepository
        extends JpaRepository<Producto, Long> {
}
package com.example.proyectoweb.controller;

import com.example.proyectoweb.model.Categoria;
import com.example.proyectoweb.repository.CategoriaRepository;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/categorias")
public class CategoriaController {

    private final CategoriaRepository repositorio;

    public CategoriaController(CategoriaRepository repositorio) {
        this.repositorio = repositorio;
    }

    @GetMapping
    public String listar(Model model) {

        model.addAttribute("categorias", repositorio.findAll());
        model.addAttribute("categoria", new Categoria());

        return "categorias/lista";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Categoria categoria) {

        if (categoria.getNombre() != null &&
                !categoria.getNombre().isBlank()) {

            categoria.setNombre(categoria.getNombre().trim());

            repositorio.save(categoria);
        }

        return "redirect:/categorias";
    }
}




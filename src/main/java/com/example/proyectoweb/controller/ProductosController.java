package com.example.proyectoweb.controller;

import com.example.proyectoweb.model.Producto;
import com.example.proyectoweb.repository.CategoriaRepository;
import com.example.proyectoweb.service.ProductoService;
import java.math.BigDecimal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/productos")
public class ProductosController {

    private final ProductoService productoService;
    private final CategoriaRepository categoriaRepository;

    // 1. INYECCION DE DEPENDENCIAS
    public ProductosController(
            ProductoService productoService,
            CategoriaRepository categoriaRepository) {

        this.productoService = productoService;
        this.categoriaRepository = categoriaRepository;
    }

    @GetMapping
    public String listar(Model model) {

        model.addAttribute(
                "productos",
                productoService.listar()
        );

        model.addAttribute(
                "categorias",
                categoriaRepository.findAll()
        );

        return "productos";
    }

    // 2. GUARDAR PRODUCTO
    @PostMapping("/guardar")
    public String guardar(
            @RequestParam String nombre,
            @RequestParam BigDecimal precio,
            @RequestParam Integer cantidad,
            @RequestParam Long categoriaId) {

         boolean guardado = productoService.guardar(nombre, precio, cantidad, categoriaId);
         if(!guardado){
             return "redirect:/productos?error=datos";
         }
         return "redirect:/productos?mensaje=guardado";
    }


    // 4. ACTUALIZAR PRODUCTO
    @PostMapping("/actualizar")
    public String actualizar(
            @RequestParam Long id,
            @RequestParam String nombre,
            @RequestParam BigDecimal precio,
            @RequestParam Integer cantidad,
            @RequestParam Long categoriaId) {

        if(productoService.buscar(id) == null){
            return "redirect:/productos?error=noexiste";
        }
        boolean actualizar = productoService.actualizar(id, nombre, precio, cantidad,categoriaId);

        if(!actualizar){
            return "redirect:/productos?error=datos";
        }
        return "redirect:/productos?mensaje=actualizado";
    }


    // 3. EDITAR PRODUCTO
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {

        Producto producto =  productoService.buscar(id);

        if (producto == null) {
            return "redirect:/productos?error=noexiste";
        }

        model.addAttribute(
                "productos", productoService.listar());
        model.addAttribute(
                "categorias", categoriaRepository.findAll());
        model.addAttribute("productoEditar", producto);

        return "productos";
    }


    // 5. ELIMINAR PRODUCTO
    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        boolean eliminado = productoService.eliminar(id);
        if(!eliminado){
            return "redirect:/productos?error=noexiste";
        }

        return "redirect:/productos?mensaje=eliminado";
    }

}

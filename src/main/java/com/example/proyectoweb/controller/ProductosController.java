package com.example.proyectoweb.controller;

import com.example.proyectoweb.model.Producto;
import com.example.proyectoweb.model.Categoria;
import com.example.proyectoweb.repository.ProductoRepository;
import com.example.proyectoweb.repository.CategoriaRepository;

import java.math.BigDecimal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/productos")
public class ProductosController {

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;

    public ProductosController(
            ProductoRepository productoRepository,
            CategoriaRepository categoriaRepository) {

        this.productoRepository = productoRepository;
        this.categoriaRepository = categoriaRepository;
    }

    @GetMapping
    public String listar(Model model) {

        model.addAttribute(
                "productos",
                productoRepository.findAll()
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

        if (!datosValidados(nombre, precio, cantidad)) {
            return "redirect:/productos?error=datos";
        }

        Categoria categoria = categoriaRepository
                .findById(categoriaId)
                .orElse(null);

        if (categoria == null) {
            return "redirect:/productos?error=categoria";
        }

        Producto nuevo = new Producto();

        nuevo.setNombre(nombre.trim());
        nuevo.setPrecio(precio);
        nuevo.setCantidad(cantidad);
        nuevo.setCategoria(categoria);

        productoRepository.save(nuevo);

        return "redirect:/productos?mensaje=guardado";
    }

    // 3. BUSCAR PRODUCTO PARA EDITAR
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {

        Producto producto = productoRepository
                .findById(id)
                .orElse(null);

        if (producto == null) {
            return "redirect:/productos?error=noexiste";
        }

        model.addAttribute(
                "productos", productoRepository.findAll());

        model.addAttribute(
                "categorias", categoriaRepository.findAll());

        model.addAttribute("productoEditar", producto);

        return "productos";
    }

    // 4. ACTUALIZAR PRODUCTO
    @PostMapping("/actualizar")
    public String actualizar(
            @RequestParam Long id,
            @RequestParam String nombre,
            @RequestParam BigDecimal precio,
            @RequestParam Integer cantidad,
            @RequestParam Long categoriaId) {

        Producto producto = productoRepository
                .findById(id)
                .orElse(null);

        if (producto == null) {
            return "redirect:/productos?error=noexiste";
        }

        if (!datosValidados(nombre, precio, cantidad)) {
            return "redirect:/productos?error=datos";
        }

        Categoria categoria = categoriaRepository
                .findById(categoriaId)
                .orElse(null);

        if (categoria == null) {
            return "redirect:/productos?error=categoria";
        }

        producto.setNombre(nombre.trim());
        producto.setPrecio(precio);
        producto.setCantidad(cantidad);
        producto.setCategoria(categoria);

        productoRepository.save(producto);

        return "redirect:/productos?mensaje=actualizado";
    }

    // 5. ELIMINAR PRODUCTO
    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {

        if (!productoRepository.existsById(id)) {
            return "redirect:/productos?error=noexiste";
        }

        productoRepository.deleteById(id);

        return "redirect:/productos?mensaje=eliminado";
    }

    // 6. VALIDAR DATOS
    private boolean datosValidados(
            String nombre,
            BigDecimal precio,
            Integer cantidad) {

        return nombre != null
                && !nombre.isBlank()
                && precio != null
                && precio.compareTo(BigDecimal.ZERO) >= 0
                && cantidad != null
                && cantidad >= 0;
    }
}

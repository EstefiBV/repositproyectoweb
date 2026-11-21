package com.example.proyectoweb.service;

import com.example.proyectoweb.model.Producto;
import com.example.proyectoweb.model.Categoria;
import com.example.proyectoweb.repository.ProductoRepository;
import com.example.proyectoweb.repository.CategoriaRepository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;

    // 1. INTECCIÓN DE DEPENDECIAS
    public ProductoService(ProductoRepository productoRepository, CategoriaRepository categoriaRepository){
       this.productoRepository = productoRepository;
       this.categoriaRepository = categoriaRepository;

    }

    // 2. LISTAR TODOS LOS PRODUCTOS
    public List<Producto> listar() {
        return productoRepository.findAll();
    }

    // 3. BUSCAR PRODUCTO PARA EDITAR
    public Producto buscar(Long id) {
        if(id == null){
            return  null;
        }
        return productoRepository.findById(id).orElse(null);
    }

    // 4. GUARDAR PRODUCTO NUEVO
    public boolean guardar(String nombre, BigDecimal precio, Integer cantidad, Long categoriaId) {

        //Validar los datos recibidos
        if (!datosValidados(nombre, precio, cantidad) || categoriaId == null) {
            return false;
        }
        //Buscar la categoria seleccionada
        Categoria categoria = categoriaRepository
                .findById(categoriaId)
                .orElse(null);

        if (categoria == null) {
            return false;
        }

        Producto nuevo = new Producto();

        nuevo.setNombre(nombre.trim());
        nuevo.setPrecio(precio);
        nuevo.setCantidad(cantidad);
        nuevo.setCategoria(categoria);

        productoRepository.save(nuevo);

        return true;
    }


    // 5. ACTUALIZAR UN PRODUCTO
    public boolean actualizar(Long id, String nombre, BigDecimal precio, Integer cantidad,  Long categoriaId) {
        //Buscar el producto  existente
        Producto producto = productoRepository
                .findById(id)
                .orElse(null);

        if (producto == null || !datosValidados(nombre, precio, cantidad) || categoriaId == null) {
            return false;
        }

        //Buscar la categoria
        Categoria categoria = categoriaRepository
                .findById(categoriaId)
                .orElse(null);

        if (categoria == null) {
            return false;
        }

        producto.setNombre(nombre.trim());
        producto.setPrecio(precio);
        producto.setCantidad(cantidad);
        producto.setCategoria(categoria);

        productoRepository.save(producto);

        return true;
    }

    // 6. ELIMINAR UN PRODUCTO
    public boolean eliminar(Long id) {

        if (id == null || !productoRepository.existsById(id)) {
            return false;
        }
        productoRepository.deleteById(id);
        return true;
    }

    // 7. VALIDAR LOS DATOS DE PRODUCTO
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

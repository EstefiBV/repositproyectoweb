package com.example.proyectoweb.controller;

import com.example.proyectoweb.model.Producto;
import com.example.proyectoweb.service.ProductoService;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/productos")
public class ProductosRestController {

    private final ProductoService productoService;

    // 1. INYECCION DE DEPENDENCIAS
    public ProductosRestController(ProductoService productoService){
        this.productoService = productoService;
    }

    // OBJETO PARA REIBIR DATOS JSON
    public record ProductoPeticion(String nombre, BigDecimal precio, Integer cantidad, Long categoriaId){}

    //OBJETO PARAR DEVOLVER DATOS JSON
    public record ProductoRepuesta(Long id, String nombre, BigDecimal precio, Integer cantidad, Long categoriaId){}

    // CONVERTIR PRODUCTO A RESPUESTA
    private ProductoRepuesta convertir(Producto p){
        Long categoriaId = p.getCategoria() == null ? null : p.getCategoria().getId();
        return new ProductoRepuesta(p.getId(), p.getNombre(), p.getPrecio(), p.getCantidad(), categoriaId);
    }

    //1. LISTAR PRODUCTO
    @GetMapping
    public List<ProductoRepuesta> listar() {
        return productoService.listar().stream().map(this::convertir).toList();
    }

    // 2. BUSCAR POR ID
    @GetMapping("/{id}")
    public ResponseEntity<ProductoRepuesta> buscar(@PathVariable Long id) {
        Producto producto = productoService.buscar(id);
        if(producto == null){
            ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(convertir(producto));
    }

    // 3. GUARDAR PRODUCTO
    @PostMapping
    public ResponseEntity<String> guardar(@RequestBody ProductoPeticion datos) {

        boolean guardado = productoService.guardar(datos.nombre(),datos.precio(), datos.cantidad(), datos.categoriaId());
        if(!guardado){
            return ResponseEntity.badRequest().body("Datos o categoria invalidos");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("Producto registrado correctamente");
    }

    // 4. ACTUALIZAR PRODUCTO
    @PostMapping("/actualizar")
    public ResponseEntity<String> actualizar(@PathVariable Long id, @RequestBody ProductoPeticion datos) {
        if(productoService.buscar(id) == null){
            ResponseEntity.notFound().build();
        }
        boolean actualizar = productoService.actualizar(id, datos.nombre(),datos.precio(),
                datos.cantidad(), datos.categoriaId());
        if(!actualizar){
            return ResponseEntity.badRequest().body("Datos o categoria invalidos");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("Producto actualizado correctamente");
    }

    // 5. ELIMINAR PRODUCTO
    @PostMapping("/eliminar/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        boolean eliminado = productoService.eliminar(id);
        if(!eliminado){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }

    // 6. CONSULTAR ENCABEZADOS
    @RequestMapping(method = RequestMethod.HEAD)
    public ResponseEntity<Void> verificar(){
        return ResponseEntity.ok().build();
    }

}

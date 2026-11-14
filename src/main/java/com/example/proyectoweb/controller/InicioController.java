
package com.example.proyectoweb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.example.proyectoweb.model.Usuario;
import com.example.proyectoweb.repository.UsuarioRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpServletRequest;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.web.bind.annotation.*;

import java.nio.channels.IllegalChannelGroupException;

@Controller
public class InicioController {
    private final UsuarioRepository usuarioRepository;

    public InicioController(UsuarioRepository usuarioRepository){
        this.usuarioRepository = usuarioRepository;
    }

    //MOSTRAR LOGIN
    @GetMapping({"/", "/login"})
    public String mostrarInicio(Model model) {
        model.addAttribute(
                "titulo", "Sistema de Gestión de Productos");

        model.addAttribute(
                "curso", "Programación Avanzada");
        return "login";
    }

    // ===========================
    // INICIAR SESIÓN
    // ===========================
    @PostMapping("/login")
    public String iniciarSesion(@RequestParam String username, @RequestParam String password,
                                HttpServletRequest request){
        Usuario usuario = usuarioRepository.findByUsername(username).orElse(null);
        // VALIDAR USUARIO ACTIVO
        if(usuario == null || Boolean.TRUE.equals(usuario.getActivo()) || usuario.getPasswordHash() == null){
            return "redirect:/login?error";
        }

        //VALIDAR CONTRASEÑA
        boolean passwordCorrecta;
        try{
            passwordCorrecta = BCrypt.checkpw(password, usuario.getPasswordHash());
        }catch(IllegalArgumentException ex){
            return "redirect:/login?error";
        }
        if(!passwordCorrecta){
            return "redirect:/login?error";
        }
        //RENOVAR SESION
        HttpSession anterior = request.getSession(false);
        if(anterior != null){
            anterior.invalidate();
        }
        HttpSession session = request.getSession(true);

        //GUARDAR USUARIO Y ROL
        session.setAttribute("usuarioId", usuario.getId());
        session.setAttribute("usuario", usuario.getNombre());
        session.setAttribute("rol", usuario.getRol());

        //REDIRECCION SEGÚN ROL
        if("ADMIN".equals(usuario.getRol().getNombre())){
            return "redirect:/productos";
        }
        return "acceso-denegado";
    }

    // ACCESO DENEGADO
    @GetMapping("/acceso-denegado")
    public String accesoDenegado() {
        return "acceso-denegado";
    }

    // CERRAR SESION
    @PostMapping("/logout")
    public String cerrarSesion(HttpServletRequest request){

        HttpSession session = request.getSession(false);
        if(session != null){
            session.invalidate();
        }
        return "redirect:/login?logout";
    }
}

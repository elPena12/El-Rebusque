package com.el_rebusque.demo.config;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.el_rebusque.demo.model.Usuario;
import com.el_rebusque.demo.repository.UsuarioRepository;

import jakarta.servlet.http.HttpSession;

/** Pone en todas las vistas al usuario con sesión iniciada (o null). */
@ControllerAdvice
public class UsuarioActualAdvice {

    private final UsuarioRepository usuarioRepository;

    public UsuarioActualAdvice(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @ModelAttribute("usuarioActual")
    public Usuario usuarioActual(HttpSession session) {
        Integer id = Sesion.usuarioId(session);
        return id == null ? null : usuarioRepository.findById(id).orElse(null);
    }
}

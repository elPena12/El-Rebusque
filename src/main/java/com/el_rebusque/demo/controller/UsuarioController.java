package com.el_rebusque.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.el_rebusque.demo.config.Sesion;
import com.el_rebusque.demo.dto.RegistroForm;
import com.el_rebusque.demo.model.Usuario;
import com.el_rebusque.demo.service.UsuarioService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

/** Controlador: guardar contraseñas y usuarios (registro, inicio y cierre de sesión). */
@Controller
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/registro")
    public String formularioRegistro(Model model) {
        model.addAttribute("form", new RegistroForm());
        return "registro";
    }

    @PostMapping("/registro")
    public String registrar(@Valid @ModelAttribute("form") RegistroForm form,
                            BindingResult errores,
                            HttpServletRequest request,
                            RedirectAttributes redirect) {

        if (form.getContrasena() != null && !form.getContrasena().equals(form.getConfirmarContrasena())) {
            errores.rejectValue("confirmarContrasena", "distinta", "Las contraseñas no coinciden");
        }
        if (form.getCorreo() != null && !form.getCorreo().isBlank() && usuarioService.correoRegistrado(form.getCorreo())) {
            errores.rejectValue("correo", "duplicado", "Ya existe una cuenta con este correo");
        }
        if (errores.hasErrors()) {
            return "registro";
        }

        Usuario nuevo = usuarioService.registrar(form);
        iniciarSesion(request, nuevo);
        redirect.addFlashAttribute("mensaje", "Cuenta creada. Ya puede publicar sus cómics.");
        return "redirect:/";
    }

    @GetMapping("/login")
    public String formularioLogin() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String correo,
                        @RequestParam String contrasena,
                        HttpServletRequest request,
                        Model model,
                        RedirectAttributes redirect) {

        return usuarioService.autenticar(correo, contrasena)
                .map(u -> {
                    iniciarSesion(request, u);
                    redirect.addFlashAttribute("mensaje", "Hola, " + u.getPrimerNombre());
                    return "redirect:/";
                })
                .orElseGet(() -> {
                    model.addAttribute("error", "Correo o contraseña incorrectos");
                    model.addAttribute("correo", correo);
                    return "login";
                });
    }

    @PostMapping("/logout")
    public String logout(HttpSession session, RedirectAttributes redirect) {
        session.invalidate();
        redirect.addFlashAttribute("mensaje", "Sesión cerrada");
        return "redirect:/";
    }

    /** Cambia el id de sesión al entrar (evita fijación de sesión) y guarda el usuario. */
    private void iniciarSesion(HttpServletRequest request, Usuario u) {
        request.getSession(true);
        request.changeSessionId();
        request.getSession().setAttribute(Sesion.USUARIO_ID, u.getId());
    }
}

package com.el_rebusque.demo.controller;

import java.math.BigDecimal;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.el_rebusque.demo.config.Sesion;
import com.el_rebusque.demo.model.Comic;
import com.el_rebusque.demo.repository.ComicRepository;
import com.el_rebusque.demo.service.ComicService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/comics")
public class PrecioController {

    private final ComicRepository comicRepository;
    private final ComicService comicService;

    public PrecioController(ComicRepository comicRepository, ComicService comicService) {
        this.comicRepository = comicRepository;
        this.comicService = comicService;
    }

    @PostMapping("/{id}/precio")
    public String modificarPrecio(@PathVariable Integer id,
                                  @RequestParam BigDecimal precio,
                                  HttpSession session,
                                  RedirectAttributes redirect) {

        // Antes: findById(...).orElseThrow(404). Ahora además verifica que el cómic sea del usuario en sesión.
        Comic comic = comicService.buscarPropio(id, Sesion.usuarioId(session));

        if (precio.compareTo(BigDecimal.ZERO) <= 0) {
            redirect.addFlashAttribute("error", "El precio debe ser mayor que cero");
            return "redirect:/mis-comics";
        }
        // NUMERIC(10,2) admite como máximo 99.999.999,99
        if (precio.compareTo(new BigDecimal("99999999.99")) > 0) {
            redirect.addFlashAttribute("error", "El precio es demasiado alto");
            return "redirect:/mis-comics";
        }

        comic.setPrecio(precio);
        comicRepository.save(comic);

        redirect.addFlashAttribute("mensaje", "Precio actualizado");
        return "redirect:/mis-comics";
    }
}

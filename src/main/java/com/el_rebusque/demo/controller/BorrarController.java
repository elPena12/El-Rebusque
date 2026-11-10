package com.el_rebusque.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.el_rebusque.demo.config.Sesion;
import com.el_rebusque.demo.model.Comic;
import com.el_rebusque.demo.repository.ComicRepository;
import com.el_rebusque.demo.service.ComicService;
import com.el_rebusque.demo.service.ImagenService;

import jakarta.servlet.http.HttpSession;

/** Controlador: borrar cómics publicados (solo el vendedor dueño). */
@Controller
@RequestMapping("/comics")
public class BorrarController {

    private final ComicRepository comicRepository;
    private final ComicService comicService;
    private final ImagenService imagenService;

    public BorrarController(ComicRepository comicRepository, ComicService comicService, ImagenService imagenService) {
        this.comicRepository = comicRepository;
        this.comicService = comicService;
        this.imagenService = imagenService;
    }

    @PostMapping("/{id}/eliminar")
    public String eliminar(@PathVariable Integer id, HttpSession session, RedirectAttributes redirect) {
        Comic comic = comicService.buscarPropio(id, Sesion.usuarioId(session));
        String imagen = comic.getImagen();
        comicRepository.delete(comic);
        imagenService.borrarSiEsLocal(imagen);

        redirect.addFlashAttribute("mensaje", "Cómic eliminado: " + comic.getTitulo());
        return "redirect:/mis-comics";
    }
}

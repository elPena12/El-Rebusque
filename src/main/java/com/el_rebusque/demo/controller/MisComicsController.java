package com.el_rebusque.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.el_rebusque.demo.config.Sesion;
import com.el_rebusque.demo.repository.ComicRepository;

import jakarta.servlet.http.HttpSession;

/** Panel del vendedor: desde aquí se modifican stock y precio y se borran publicaciones. */
@Controller
public class MisComicsController {

    private final ComicRepository comicRepository;

    public MisComicsController(ComicRepository comicRepository) {
        this.comicRepository = comicRepository;
    }

    @GetMapping("/mis-comics")
    public String misComics(HttpSession session, Model model) {
        model.addAttribute("comics",
                comicRepository.findByUsuarioIdOrderByFechaDescIdDesc(Sesion.usuarioId(session)));
        return "mis-comics";
    }
}

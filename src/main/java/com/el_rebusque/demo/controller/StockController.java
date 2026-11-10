package com.el_rebusque.demo.controller;

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
public class StockController {

    private final ComicRepository comicRepository;
    private final ComicService comicService;

    public StockController(ComicRepository comicRepository, ComicService comicService) {
        this.comicRepository = comicRepository;
        this.comicService = comicService;
    }

    @PostMapping("/{id}/stock")
    public String modificarStock(@PathVariable Integer id,
                                 @RequestParam Integer stock,
                                 HttpSession session,
                                 RedirectAttributes redirect) {

        // Antes: findById(...).orElseThrow(404). Ahora además verifica que el cómic sea del usuario en sesión.
        Comic comic = comicService.buscarPropio(id, Sesion.usuarioId(session));

        if (stock < 0) {
            redirect.addFlashAttribute("error", "El stock no puede ser negativo");
            return "redirect:/mis-comics";
        }

        comic.setStock(stock);
        comicRepository.save(comic);

        redirect.addFlashAttribute("mensaje", "Stock actualizado");
        return "redirect:/mis-comics";
    }
}

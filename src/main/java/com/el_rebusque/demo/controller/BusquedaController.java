package com.el_rebusque.demo.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import com.el_rebusque.demo.model.Categoria;
import com.el_rebusque.demo.model.Comic;
import com.el_rebusque.demo.repository.CategoriaRepository;
import com.el_rebusque.demo.repository.ComicRepository;

/** Controlador: muro y búsqueda de cómics por categoría y nombre. */
@Controller
public class BusquedaController {

    private final ComicRepository comicRepository;
    private final CategoriaRepository categoriaRepository;

    public BusquedaController(ComicRepository comicRepository, CategoriaRepository categoriaRepository) {
        this.comicRepository = comicRepository;
        this.categoriaRepository = categoriaRepository;
    }

    @GetMapping("/")
    public String muro(@RequestParam(value = "q", required = false) String q,
                       @RequestParam(value = "categoria", required = false) Integer idCategoria,
                       Model model) {

        String texto = q == null ? "" : q.trim();
        List<Comic> comics = comicRepository.buscar(texto, idCategoria);
        List<Categoria> categorias = categoriaRepository.findAllByOrderByNombreAsc();

        model.addAttribute("comics", comics);
        model.addAttribute("categorias", categorias);
        model.addAttribute("q", texto);
        model.addAttribute("categoriaSel", idCategoria);
        model.addAttribute("filtrando", !texto.isEmpty() || idCategoria != null);
        model.addAttribute("nombreCategoriaSel", categorias.stream()
                .filter(c -> c.getId().equals(idCategoria))
                .map(Categoria::getNombre).findFirst().orElse(null));
        return "index";
    }

    @GetMapping("/comics/{id}")
    public String detalle(@PathVariable Integer id, Model model) {
        Comic comic = comicRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "El cómic no existe"));
        model.addAttribute("comic", comic);
        return "detalle";
    }
}

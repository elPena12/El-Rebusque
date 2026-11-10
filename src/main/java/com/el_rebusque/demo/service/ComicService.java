package com.el_rebusque.demo.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.el_rebusque.demo.model.Comic;
import com.el_rebusque.demo.repository.ComicRepository;

@Service
public class ComicService {

    private final ComicRepository comicRepository;

    public ComicService(ComicRepository comicRepository) {
        this.comicRepository = comicRepository;
    }

    /** Trae el cómic solo si existe y pertenece al usuario; si no, 404 o 403. */
    public Comic buscarPropio(Integer idComic, Integer idUsuario) {
        Comic comic = comicRepository.findById(idComic)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "El cómic no existe"));
        if (idUsuario == null || !comic.getUsuario().getId().equals(idUsuario)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Solo el vendedor puede modificar este cómic");
        }
        return comic;
    }
}

package com.el_rebusque.demo.controller;

import java.io.IOException;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.el_rebusque.demo.config.Sesion;
import com.el_rebusque.demo.dto.PublicarForm;
import com.el_rebusque.demo.model.Comic;
import com.el_rebusque.demo.repository.CategoriaRepository;
import com.el_rebusque.demo.repository.ComicRepository;
import com.el_rebusque.demo.repository.UsuarioRepository;
import com.el_rebusque.demo.service.ImagenService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

/** Controlador: publicar cómics. */
@Controller
public class PublicarController {

    private final ComicRepository comicRepository;
    private final CategoriaRepository categoriaRepository;
    private final UsuarioRepository usuarioRepository;
    private final ImagenService imagenService;

    public PublicarController(ComicRepository comicRepository,
                              CategoriaRepository categoriaRepository,
                              UsuarioRepository usuarioRepository,
                              ImagenService imagenService) {
        this.comicRepository = comicRepository;
        this.categoriaRepository = categoriaRepository;
        this.usuarioRepository = usuarioRepository;
        this.imagenService = imagenService;
    }

    @GetMapping("/publicar")
    public String formulario(Model model) {
        model.addAttribute("form", new PublicarForm());
        model.addAttribute("categorias", categoriaRepository.findAllByOrderByNombreAsc());
        return "publicar";
    }

    @PostMapping("/publicar")
    public String publicar(@Valid @ModelAttribute("form") PublicarForm form,
                           BindingResult errores,
                           @RequestParam(value = "imagenArchivo", required = false) MultipartFile archivo,
                           HttpSession session,
                           Model model,
                           RedirectAttributes redirect) throws IOException {

        boolean hayArchivo = archivo != null && !archivo.isEmpty();
        if (hayArchivo && !imagenService.esImagenValida(archivo)) {
            errores.reject("imagen", "La portada debe ser JPG, PNG, WEBP o GIF");
        }
        String url = form.getImagenUrl() == null ? "" : form.getImagenUrl().trim();
        if (!url.isEmpty() && !(url.startsWith("http://") || url.startsWith("https://"))) {
            errores.rejectValue("imagenUrl", "url", "El enlace debe empezar por http:// o https://");
        }
        if (form.getIdCategoria() != null && !categoriaRepository.existsById(form.getIdCategoria())) {
            errores.rejectValue("idCategoria", "noExiste", "La categoría no existe");
        }
        if (errores.hasErrors()) {
            model.addAttribute("categorias", categoriaRepository.findAllByOrderByNombreAsc());
            return "publicar";
        }

        Comic comic = new Comic();
        comic.setTitulo(form.getTitulo().trim());
        comic.setDescripcion(form.getDescripcion() == null || form.getDescripcion().isBlank()
                ? null : form.getDescripcion().trim());
        comic.setPrecio(form.getPrecio());
        comic.setStock(form.getStock());
        comic.setCategoria(categoriaRepository.getReferenceById(form.getIdCategoria()));
        comic.setUsuario(usuarioRepository.getReferenceById(Sesion.usuarioId(session)));
        if (hayArchivo) {
            comic.setImagen(imagenService.guardar(archivo));
        } else if (!url.isEmpty()) {
            comic.setImagen(url);
        }
        comic = comicRepository.save(comic);

        redirect.addFlashAttribute("mensaje", "Cómic publicado");
        return "redirect:/comics/" + comic.getId();
    }
}

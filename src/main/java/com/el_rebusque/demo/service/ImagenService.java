package com.el_rebusque.demo.service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/** Guarda las portadas subidas en la carpeta uploads/ y devuelve la ruta pública. */
@Service
public class ImagenService {

    private static final Map<String, String> EXTENSIONES = Map.of(
            "image/jpeg", ".jpg",
            "image/png", ".png",
            "image/webp", ".webp",
            "image/gif", ".gif");

    private final Path carpeta;

    public ImagenService(@Value("${app.uploads.dir}") String uploadsDir) throws IOException {
        this.carpeta = Path.of(uploadsDir).toAbsolutePath();
        Files.createDirectories(carpeta);
    }

    public boolean esImagenValida(MultipartFile archivo) {
        return archivo != null && !archivo.isEmpty() && EXTENSIONES.containsKey(archivo.getContentType());
    }

    public String guardar(MultipartFile archivo) throws IOException {
        String nombre = UUID.randomUUID() + EXTENSIONES.get(archivo.getContentType());
        try (InputStream in = archivo.getInputStream()) {
            Files.copy(in, carpeta.resolve(nombre), StandardCopyOption.REPLACE_EXISTING);
        }
        return "/uploads/" + nombre;
    }

    /** Borra el archivo si la portada fue subida a este servidor (no toca enlaces externos). */
    public void borrarSiEsLocal(String rutaPublica) {
        if (rutaPublica == null || !rutaPublica.startsWith("/uploads/")) return;
        Path archivo = carpeta.resolve(rutaPublica.substring("/uploads/".length())).normalize();
        if (!archivo.startsWith(carpeta)) return;
        try {
            Files.deleteIfExists(archivo);
        } catch (IOException ignorada) {
            // Si no se puede borrar el archivo, el cómic igual queda eliminado de la base.
        }
    }
}

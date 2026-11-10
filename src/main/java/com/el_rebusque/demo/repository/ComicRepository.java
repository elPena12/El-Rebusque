package com.el_rebusque.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.el_rebusque.demo.model.Comic;

public interface ComicRepository extends JpaRepository<Comic, Integer> {

    /**
     * Búsqueda por nombre (título) y categoría.
     * texto vacío = todos los títulos; idCategoria null = todas las categorías.
     */
    @Query("""
           SELECT c FROM Comic c
           WHERE LOWER(c.titulo) LIKE LOWER(CONCAT('%', :texto, '%'))
             AND (:idCategoria IS NULL OR c.categoria.id = :idCategoria)
           ORDER BY c.fecha DESC, c.id DESC
           """)
    List<Comic> buscar(@Param("texto") String texto,
                       @Param("idCategoria") Integer idCategoria);

    List<Comic> findByUsuarioIdOrderByFechaDescIdDesc(Integer idUsuario);
}

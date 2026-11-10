package com.el_rebusque.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.el_rebusque.demo.model.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {

    List<Categoria> findAllByOrderByNombreAsc();
}

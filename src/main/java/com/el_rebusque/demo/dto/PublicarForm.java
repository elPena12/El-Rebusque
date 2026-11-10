package com.el_rebusque.demo.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class PublicarForm {

    @NotBlank(message = "Escriba el título del cómic")
    @Size(max = 200, message = "Máximo 200 caracteres")
    private String titulo;

    @Size(max = 5000, message = "Máximo 5000 caracteres")
    private String descripcion;

    @NotNull(message = "Escriba el precio")
    @DecimalMin(value = "0.01", message = "El precio debe ser mayor que cero")
    @DecimalMax(value = "99999999.99", message = "El precio es demasiado alto")
    private BigDecimal precio;

    @NotNull(message = "Escriba cuántas unidades tiene")
    @Min(value = 0, message = "El stock no puede ser negativo")
    @Max(value = 100000, message = "El stock es demasiado alto")
    private Integer stock = 1;

    @NotNull(message = "Elija una categoría")
    private Integer idCategoria;

    @Size(max = 500, message = "El enlace es demasiado largo")
    private String imagenUrl;

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public BigDecimal getPrecio() { return precio; }
    public void setPrecio(BigDecimal precio) { this.precio = precio; }
    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }
    public Integer getIdCategoria() { return idCategoria; }
    public void setIdCategoria(Integer idCategoria) { this.idCategoria = idCategoria; }
    public String getImagenUrl() { return imagenUrl; }
    public void setImagenUrl(String imagenUrl) { this.imagenUrl = imagenUrl; }
}

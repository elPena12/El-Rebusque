package com.el_rebusque.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/** Tabla usuario: id_usuario, nombre, telefono, contrasena, correo, ciudad_departamento. */
@Entity
@Table(name = "usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Integer id;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "telefono", nullable = false, length = 20)
    private String telefono;

    /** Hash BCrypt, nunca la clave en texto plano. */
    @Column(name = "contrasena", nullable = false, length = 100)
    private String contrasena;

    @Column(name = "correo", nullable = false, length = 100, unique = true)
    private String correo;

    @Column(name = "ciudad_departamento", nullable = false, length = 150)
    private String ciudadDepartamento;

    /** Primera palabra del nombre, para el saludo de la barra superior. */
    public String getPrimerNombre() {
        if (nombre == null || nombre.isBlank()) return "";
        return nombre.trim().split("\\s+")[0];
    }

    /** Número para enlace de WhatsApp: solo dígitos, con indicativo 57 si es celular colombiano de 10 dígitos. */
    public String getTelefonoWhatsapp() {
        if (telefono == null) return "";
        String digitos = telefono.replaceAll("\\D", "");
        if (digitos.length() == 10 && digitos.startsWith("3")) return "57" + digitos;
        return digitos;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public String getCiudadDepartamento() { return ciudadDepartamento; }
    public void setCiudadDepartamento(String ciudadDepartamento) { this.ciudadDepartamento = ciudadDepartamento; }
}

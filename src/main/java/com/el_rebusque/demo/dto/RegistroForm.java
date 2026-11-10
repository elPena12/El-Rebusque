package com.el_rebusque.demo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class RegistroForm {

    @NotBlank(message = "Escriba su nombre")
    @Size(max = 100, message = "Máximo 100 caracteres")
    private String nombre;

    @NotBlank(message = "Escriba un teléfono de contacto")
    @Pattern(regexp = "^[0-9+ ]{7,20}$", message = "Use solo números (7 a 20 dígitos)")
    private String telefono;

    @NotBlank(message = "Escriba su correo")
    @Email(message = "El correo no tiene un formato válido")
    @Size(max = 100, message = "Máximo 100 caracteres")
    private String correo;

    @NotBlank(message = "Escriba una contraseña")
    @Size(min = 8, max = 72, message = "La contraseña debe tener entre 8 y 72 caracteres")
    private String contrasena;

    @NotBlank(message = "Repita la contraseña")
    private String confirmarContrasena;

    @NotBlank(message = "Escriba su ciudad y departamento")
    @Size(max = 150, message = "Máximo 150 caracteres")
    private String ciudadDepartamento;

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }
    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }
    public String getConfirmarContrasena() { return confirmarContrasena; }
    public void setConfirmarContrasena(String confirmarContrasena) { this.confirmarContrasena = confirmarContrasena; }
    public String getCiudadDepartamento() { return ciudadDepartamento; }
    public void setCiudadDepartamento(String ciudadDepartamento) { this.ciudadDepartamento = ciudadDepartamento; }
}

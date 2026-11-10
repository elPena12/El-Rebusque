package com.el_rebusque.demo.service;

import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.el_rebusque.demo.dto.RegistroForm;
import com.el_rebusque.demo.model.Usuario;
import com.el_rebusque.demo.repository.UsuarioRepository;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder encoder;

    public UsuarioService(UsuarioRepository usuarioRepository, BCryptPasswordEncoder encoder) {
        this.usuarioRepository = usuarioRepository;
        this.encoder = encoder;
    }

    public boolean correoRegistrado(String correo) {
        return usuarioRepository.existsByCorreoIgnoreCase(correo.trim());
    }

    /** Guarda el usuario con la contraseña cifrada (BCrypt). */
    @Transactional
    public Usuario registrar(RegistroForm f) {
        Usuario u = new Usuario();
        u.setNombre(f.getNombre().trim());
        u.setTelefono(f.getTelefono().trim());
        u.setCorreo(f.getCorreo().trim().toLowerCase());
        u.setContrasena(encoder.encode(f.getContrasena()));
        u.setCiudadDepartamento(f.getCiudadDepartamento().trim());
        return usuarioRepository.save(u);
    }

    /** Devuelve el usuario si el correo existe y la contraseña coincide con el hash guardado. */
    public Optional<Usuario> autenticar(String correo, String contrasena) {
        if (correo == null || contrasena == null) return Optional.empty();
        return usuarioRepository.findByCorreoIgnoreCase(correo.trim())
                .filter(u -> encoder.matches(contrasena, u.getContrasena()));
    }
}

package com.el_rebusque.demo.config;

import jakarta.servlet.http.HttpSession;

/** Llave con la que se guarda en sesión el id del usuario que inició sesión. */
public final class Sesion {

    public static final String USUARIO_ID = "usuarioId";

    private Sesion() {
    }

    public static Integer usuarioId(HttpSession session) {
        Object valor = session.getAttribute(USUARIO_ID);
        return valor instanceof Integer id ? id : null;
    }
}

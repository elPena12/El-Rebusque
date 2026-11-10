package com.el_rebusque.demo.config;

import java.nio.file.Path;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${app.uploads.dir}")
    private String uploadsDir;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /** Rutas que exigen haber iniciado sesión. */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        HandlerInterceptor requiereSesion = new HandlerInterceptor() {
            @Override
            public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
                    throws Exception {
                if (Sesion.usuarioId(request.getSession()) != null) {
                    return true;
                }
                response.sendRedirect(request.getContextPath() + "/login?pendiente");
                return false;
            }
        };

        registry.addInterceptor(requiereSesion)
                .addPathPatterns("/publicar", "/mis-comics",
                        "/comics/*/stock", "/comics/*/precio", "/comics/*/eliminar");
    }

    /** Sirve las portadas subidas desde la carpeta uploads/ del proyecto. */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String ruta = Path.of(uploadsDir).toAbsolutePath().toUri().toString();
        registry.addResourceHandler("/uploads/**").addResourceLocations(ruta);
    }
}

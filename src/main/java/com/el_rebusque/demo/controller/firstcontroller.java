package com.el_rebusque.demo.controller;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
public class firstcontroller {
    @GetMapping("/")
    public String first(Model model){
        model.addAttribute("mensaje", "BIENVENIDO A EL REBUSQUE");
        model.addAttribute("descripcion", "Aqui encontraras tus mayores fantasias");
        return "first";
    }
}

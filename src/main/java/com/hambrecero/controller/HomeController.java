package com.hambrecero.controller;

import com.hambrecero.service.DonacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    
    @Autowired
    private DonacionService donacionService;
    
    @GetMapping("/")
    public String home(Model model) {
        // Obtener estadísticas para mostrar en la página principal
        var estadisticas = donacionService.obtenerEstadisticas();
        model.addAttribute("estadisticas", estadisticas);
        return "index";
    }
    
    @GetMapping("/about")
    public String about() {
        return "about";
    }
} 
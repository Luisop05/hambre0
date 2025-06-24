package com.hambrecero.controller;

import com.hambrecero.entity.ZonaEntrega;
import com.hambrecero.service.ZonaEntregaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("/zonas")
public class ZonaEntregaController {
    
    @Autowired
    private ZonaEntregaService zonaEntregaService;
    
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("zonas", zonaEntregaService.findAll());
        return "zonas/lista";
    }
    
    @GetMapping("/nueva")
    public String mostrarFormularioCrear(Model model) {
        model.addAttribute("zona", new ZonaEntrega());
        return "zonas/formulario";
    }
    
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        return zonaEntregaService.findById(id)
                .map(zona -> {
                    model.addAttribute("zona", zona);
                    return "zonas/formulario";
                })
                .orElseGet(() -> {
                    redirectAttributes.addFlashAttribute("error", "Zona no encontrada");
                    return "redirect:/zonas";
                });
    }
    
    @PostMapping("/guardar")
    public String guardar(@Valid ZonaEntrega zona, BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "zonas/formulario";
        }
        
        try {
            if (zona.getIdZona() == null) {
                zonaEntregaService.save(zona);
                redirectAttributes.addFlashAttribute("success", "Zona creada con éxito");
            } else {
                zonaEntregaService.update(zona.getIdZona(), zona);
                redirectAttributes.addFlashAttribute("success", "Zona actualizada con éxito");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al guardar la zona: " + e.getMessage());
        }
        
        return "redirect:/zonas";
    }
    
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            zonaEntregaService.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "Zona eliminada con éxito");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar la zona: " + e.getMessage());
        }
        return "redirect:/zonas";
    }
    
    @GetMapping("/ver/{id}")
    public String ver(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        return zonaEntregaService.findById(id)
                .map(zona -> {
                    model.addAttribute("zona", zona);
                    return "zonas/detalle";
                })
                .orElseGet(() -> {
                    redirectAttributes.addFlashAttribute("error", "Zona no encontrada");
                    return "redirect:/zonas";
                });
    }
    
    @GetMapping("/buscar")
    public String buscar(@RequestParam String nombre, Model model) {
        model.addAttribute("zonas", zonaEntregaService.findByNombre(nombre));
        model.addAttribute("busqueda", nombre);
        return "zonas/lista";
    }
} 
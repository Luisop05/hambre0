package com.hambrecero.controller;

import com.hambrecero.entity.Donante;
import com.hambrecero.service.DonanteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("/donantes")
public class DonanteController {
    
    @Autowired
    private DonanteService donanteService;
    
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("donantes", donanteService.findAll());
        return "donantes/lista";
    }
    
    @GetMapping("/nuevo")
    public String mostrarFormularioCrear(Model model) {
        model.addAttribute("donante", new Donante());
        return "donantes/formulario";
    }
    
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        return donanteService.findById(id)
                .map(donante -> {
                    model.addAttribute("donante", donante);
                    return "donantes/formulario";
                })
                .orElseGet(() -> {
                    redirectAttributes.addFlashAttribute("error", "Donante no encontrado");
                    return "redirect:/donantes";
                });
    }
    
    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute Donante donante, 
                         BindingResult result, 
                         RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "donantes/formulario";
        }
        
        try {
            if (donante.getIdDonante() != null) {
                donanteService.update(donante.getIdDonante(), donante);
                redirectAttributes.addFlashAttribute("success", "Donante actualizado correctamente");
            } else {
                donanteService.save(donante);
                redirectAttributes.addFlashAttribute("success", "Donante creado correctamente");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/donantes/nuevo";
        }
        
        return "redirect:/donantes";
    }
    
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            donanteService.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "Donante eliminado correctamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "No se pudo eliminar el donante: " + e.getMessage());
        }
        return "redirect:/donantes";
    }
    
    @GetMapping("/ver/{id}")
    public String ver(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        return donanteService.findById(id)
                .map(donante -> {
                    model.addAttribute("donante", donante);
                    return "donantes/detalle";
                })
                .orElseGet(() -> {
                    redirectAttributes.addFlashAttribute("error", "Donante no encontrado");
                    return "redirect:/donantes";
                });
    }
    
    @GetMapping("/buscar")
    public String buscar(@RequestParam String nombre, Model model) {
        model.addAttribute("donantes", donanteService.findByNombre(nombre));
        model.addAttribute("busqueda", nombre);
        return "donantes/lista";
    }
} 
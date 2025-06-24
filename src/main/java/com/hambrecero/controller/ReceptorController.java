package com.hambrecero.controller;

import com.hambrecero.entity.Receptor;
import com.hambrecero.entity.Donacion;
import com.hambrecero.service.ReceptorService;
import com.hambrecero.service.DonacionService;
import com.hambrecero.service.ZonaEntregaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/receptores")
public class ReceptorController {
    
    @Autowired
    private ReceptorService receptorService;
    
    @Autowired
    private ZonaEntregaService zonaEntregaService;
    
    @Autowired
    private DonacionService donacionService;

    @GetMapping("")
    public String listar(Model model) {
        List<Receptor> receptores = receptorService.findAll();
        model.addAttribute("receptores", receptores);
        return "receptores/lista";
    }

    @GetMapping("/nuevo")
    public String mostrarFormulario(Model model) {
        model.addAttribute("receptor", new Receptor());
        model.addAttribute("zonas", zonaEntregaService.findAll());
        return "receptores/formulario";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid Receptor receptor, BindingResult result, RedirectAttributes flash, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("zonas", zonaEntregaService.findAll());
            return "receptores/formulario";
        }
        
        try {
            if (receptor.getIdReceptor() == null) {
                receptorService.save(receptor);
                flash.addFlashAttribute("success", "Receptor guardado con éxito");
            } else {
                receptorService.update(receptor.getIdReceptor(), receptor);
                flash.addFlashAttribute("success", "Receptor actualizado con éxito");
            }
        } catch (Exception e) {
            flash.addFlashAttribute("error", "Error al guardar el receptor: " + e.getMessage());
            model.addAttribute("zonas", zonaEntregaService.findAll());
            return "receptores/formulario";
        }
        
        return "redirect:/receptores";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Integer id, Model model, RedirectAttributes flash) {
        Receptor receptor = receptorService.findById(id)
                .orElse(null);
        
        if (receptor == null) {
            flash.addFlashAttribute("error", "El receptor no existe");
            return "redirect:/receptores";
        }
        
        model.addAttribute("receptor", receptor);
        model.addAttribute("zonas", zonaEntregaService.findAll());
        return "receptores/formulario";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id, RedirectAttributes flash) {
        try {
            receptorService.deleteById(id);
            flash.addFlashAttribute("success", "Receptor eliminado con éxito");
        } catch (Exception e) {
            flash.addFlashAttribute("error", "Error al eliminar el receptor: " + e.getMessage());
        }
        return "redirect:/receptores";
    }

    @GetMapping("/ver/{id}")
    public String ver(@PathVariable Integer id, Model model, RedirectAttributes flash) {
        Receptor receptor = receptorService.findById(id)
                .orElse(null);
        
        if (receptor == null) {
            flash.addFlashAttribute("error", "El receptor no existe");
            return "redirect:/receptores";
        }
        
        List<Donacion> donaciones = donacionService.findByReceptor(id);
        
        model.addAttribute("receptor", receptor);
        model.addAttribute("donaciones", donaciones);
        return "receptores/detalle";
    }
} 
package com.hambrecero.controller;

import com.hambrecero.entity.Donacion;
import com.hambrecero.entity.DetalleDonacion;
import com.hambrecero.service.DonacionService;
import com.hambrecero.service.DonanteService;
import com.hambrecero.service.ReceptorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/donaciones")
public class DonacionController {
    
    @Autowired
    private DonacionService donacionService;
    
    @Autowired
    private DonanteService donanteService;
    
    @Autowired
    private ReceptorService receptorService;
    
    @GetMapping
    public String listar(@RequestParam(required = false) String estado, Model model) {
        if (estado != null && !estado.isEmpty()) {
            model.addAttribute("donaciones", donacionService.findByEstado(estado));
            model.addAttribute("estadoFiltro", estado);
        } else {
            model.addAttribute("donaciones", donacionService.findAll());
        }
        return "donaciones/lista";
    }
    
    @GetMapping("/nueva")
    public String mostrarFormularioCrear(Model model) {
        model.addAttribute("donantes", donanteService.findAll());
        model.addAttribute("receptores", receptorService.findAll());
        return "donaciones/formulario";
    }
    
    @PostMapping("/guardar")
    public String guardar(@RequestParam Integer idDonante,
                         @RequestParam Integer idReceptor,
                         @RequestParam String observaciones,
                         @RequestParam(required = false) String[] nombreAlimento,
                         @RequestParam(required = false) Double[] cantidad,
                         @RequestParam(required = false) String[] unidadMedida,
                         @RequestParam(required = false) Double[] caloriasTotales,
                         RedirectAttributes redirectAttributes) {
        try {
            // Crear lista de detalles
            List<DetalleDonacion> detalles = new ArrayList<>();
            if (nombreAlimento != null) {
                for (int i = 0; i < nombreAlimento.length; i++) {
                    if (nombreAlimento[i] != null && !nombreAlimento[i].trim().isEmpty()) {
                        DetalleDonacion detalle = new DetalleDonacion(
                            nombreAlimento[i],
                            cantidad[i],
                            unidadMedida[i],
                            caloriasTotales[i]
                        );
                        detalles.add(detalle);
                    }
                }
            }
            
            // Crear donación completa (usando transacción)
            Donacion donacion = donacionService.crearDonacionCompleta(
                idDonante, idReceptor, observaciones, detalles
            );
            
            redirectAttributes.addFlashAttribute("success", 
                "Donación creada correctamente con ID: " + donacion.getIdDonacion());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al crear la donación: " + e.getMessage());
            return "redirect:/donaciones/nueva";
        }
        
        return "redirect:/donaciones";
    }
    
    @GetMapping("/ver/{id}")
    public String ver(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        return donacionService.findById(id)
                .map(donacion -> {
                    model.addAttribute("donacion", donacion);
                    return "donaciones/detalle";
                })
                .orElseGet(() -> {
                    redirectAttributes.addFlashAttribute("error", "Donación no encontrada");
                    return "redirect:/donaciones";
                });
    }
    
    @GetMapping("/actualizar-estado/{id}")
    public String mostrarFormularioActualizarEstado(@PathVariable Integer id, Model model, 
                                                   RedirectAttributes redirectAttributes) {
        return donacionService.findById(id)
                .map(donacion -> {
                    model.addAttribute("donacion", donacion);
                    return "donaciones/actualizar-estado";
                })
                .orElseGet(() -> {
                    redirectAttributes.addFlashAttribute("error", "Donación no encontrada");
                    return "redirect:/donaciones";
                });
    }
    
    @PostMapping("/actualizar-estado")
    public String actualizarEstado(@RequestParam Integer idDonacion,
                                  @RequestParam String nuevoEstado,
                                  @RequestParam String observaciones,
                                  RedirectAttributes redirectAttributes) {
        try {
            boolean actualizado = donacionService.actualizarEstadoDonacion(idDonacion, nuevoEstado, observaciones);
            if (actualizado) {
                redirectAttributes.addFlashAttribute("success", "Estado actualizado correctamente");
            } else {
                redirectAttributes.addFlashAttribute("error", "No se pudo actualizar el estado");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error: " + e.getMessage());
        }
        
        return "redirect:/donaciones";
    }
    
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            donacionService.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "Donación eliminada correctamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "No se pudo eliminar la donación: " + e.getMessage());
        }
        return "redirect:/donaciones";
    }
    
    @GetMapping("/estadisticas")
    public String estadisticas(Model model) {
        model.addAttribute("estadisticas", donacionService.obtenerEstadisticas());
        return "donaciones/estadisticas";
    }
} 
package com.hambrecero.controller;

import com.hambrecero.service.ProcedimientoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/reportes")
public class ReporteController {
    
    @Autowired
    private ProcedimientoService procedimientoService;
    
    @GetMapping
    public String mostrarReportes(Model model) {
        // Obtener estadísticas avanzadas
        Map<String, Object> estadisticas = procedimientoService.obtenerEstadisticasAvanzadas();
        model.addAttribute("estadisticas", estadisticas);
        
        // Fechas por defecto para el reporte por zona
        model.addAttribute("fechaInicio", LocalDate.now().minusMonths(1));
        model.addAttribute("fechaFin", LocalDate.now());
        
        return "reportes/index";
    }
    
    @PostMapping("/zona")
    public String generarReporteZona(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin,
            Model model) {
        
        // Generar reporte usando el procedimiento almacenado
        var reporteZonas = procedimientoService.generarReporteDonacionesPorZona(fechaInicio, fechaFin);
        
        model.addAttribute("reporteZonas", reporteZonas);
        model.addAttribute("fechaInicio", fechaInicio);
        model.addAttribute("fechaFin", fechaFin);
        
        // También incluir las estadísticas generales
        model.addAttribute("estadisticas", procedimientoService.obtenerEstadisticasAvanzadas());
        
        return "reportes/index";
    }
    
    @GetMapping("/procedimiento-demo")
    public String demoProcedimiento(Model model) {
        // Demostración del procedimiento almacenado sp_registrar_donacion_completa
        Map<String, Object> demoInfo = new HashMap<>();
        demoInfo.put("nombreProcedimiento", "sp_registrar_donacion_completa");
        demoInfo.put("descripcion", "Este procedimiento registra una donación completa con transacción, " +
                                   "incluyendo la actualización automática de estadísticas del donante.");
        demoInfo.put("parametrosEntrada", new String[]{
            "p_id_donante INT - ID del donante",
            "p_id_receptor INT - ID del receptor",
            "p_observaciones VARCHAR(255) - Observaciones",
            "p_nombre_alimento VARCHAR(100) - Nombre del alimento",
            "p_cantidad DECIMAL(10,2) - Cantidad",
            "p_unidad_medida VARCHAR(20) - Unidad de medida",
            "p_calorias DECIMAL(10,2) - Calorías totales"
        });
        demoInfo.put("parametrosSalida", new String[]{
            "p_id_donacion INT - ID de la donación creada",
            "p_mensaje VARCHAR(255) - Mensaje de resultado"
        });
        
        model.addAttribute("demoInfo", demoInfo);
        return "reportes/procedimiento-demo";
    }
    
    @PostMapping("/ejecutar-procedimiento")
    public String ejecutarProcedimiento(
            @RequestParam Integer idDonante,
            @RequestParam Integer idReceptor,
            @RequestParam String observaciones,
            @RequestParam String nombreAlimento,
            @RequestParam Double cantidad,
            @RequestParam String unidadMedida,
            @RequestParam Double calorias,
            RedirectAttributes redirectAttributes) {
        
        try {
            Map<String, Object> resultado = procedimientoService.registrarDonacionConProcedimiento(
                idDonante, idReceptor, observaciones, nombreAlimento, cantidad, unidadMedida, calorias
            );
            
            if ((Boolean) resultado.get("exito")) {
                redirectAttributes.addFlashAttribute("success", resultado.get("mensaje"));
            } else {
                redirectAttributes.addFlashAttribute("error", resultado.get("mensaje"));
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al ejecutar el procedimiento: " + e.getMessage());
        }
        
        return "redirect:/reportes/procedimiento-demo";
    }
} 
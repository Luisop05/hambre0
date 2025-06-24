package com.hambrecero.service;

import com.hambrecero.entity.Donacion;
import com.hambrecero.entity.DetalleDonacion;
import com.hambrecero.repository.DonacionRepository;
import com.hambrecero.repository.DonanteRepository;
import com.hambrecero.repository.ReceptorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class DonacionService {
    
    @Autowired
    private DonacionRepository donacionRepository;
    
    @Autowired
    private DonanteRepository donanteRepository;
    
    @Autowired
    private ReceptorRepository receptorRepository;
    
    public List<Donacion> findAll() {
        return donacionRepository.findAllWithDetails();
    }
    
    public Optional<Donacion> findById(Integer id) {
        return donacionRepository.findById(id);
    }
    
    public List<Donacion> findByEstado(String estado) {
        return donacionRepository.findByEstado(estado);
    }
    
    public List<Donacion> findByDonante(Integer idDonante) {
        return donacionRepository.findByDonanteIdDonante(idDonante);
    }
    
    public List<Donacion> findByReceptor(Integer idReceptor) {
        return donacionRepository.findByReceptorIdReceptor(idReceptor);
    }
    
    public List<Donacion> findByFechaRange(LocalDate fechaInicio, LocalDate fechaFin) {
        return donacionRepository.findByFechaBetween(fechaInicio, fechaFin);
    }
    
    @Transactional
    public Donacion crearDonacionCompleta(Integer idDonante, Integer idReceptor, 
                                          String observaciones, List<DetalleDonacion> detalles) {
        // Validar donante y receptor
        var donante = donanteRepository.findById(idDonante)
                .orElseThrow(() -> new RuntimeException("Donante no encontrado"));
        var receptor = receptorRepository.findById(idReceptor)
                .orElseThrow(() -> new RuntimeException("Receptor no encontrado"));
        
        // Crear la donación
        Donacion donacion = new Donacion(donante, receptor, observaciones);
        
        // Agregar detalles
        for (DetalleDonacion detalle : detalles) {
            donacion.addDetalle(detalle);
        }
        
        // Guardar la donación
        donacion = donacionRepository.save(donacion);
        
        // Actualizar estadísticas del donante
        donante.setTotalDonaciones(donante.getTotalDonaciones() + 1);
        donante.setTotalCaloriasDonadas(donante.getTotalCaloriasDonadas() + donacion.getTotalCalorias());
        donanteRepository.save(donante);
        
        return donacion;
    }
    
    @Transactional
    public boolean actualizarEstadoDonacion(Integer idDonacion, String nuevoEstado, String observaciones) {
        Optional<Donacion> donacionOpt = donacionRepository.findById(idDonacion);
        if (donacionOpt.isPresent()) {
            Donacion donacion = donacionOpt.get();
            donacion.setEstado(nuevoEstado);
            donacion.setObservaciones(observaciones);
            donacionRepository.save(donacion);
            return true;
        }
        return false;
    }
    
    public void deleteById(Integer id) {
        if (!donacionRepository.existsById(id)) {
            throw new RuntimeException("Donación no encontrada con ID: " + id);
        }
        donacionRepository.deleteById(id);
    }
    
    public Map<String, Object> obtenerEstadisticas() {
        Map<String, Object> stats = new HashMap<>();
        
        Long totalDonaciones = donacionRepository.count();
        Long donacionesEntregadas = donacionRepository.countByEstado("Entregado");
        Long donacionesPendientes = donacionRepository.countByEstado("Pendiente");
        Double totalCalorias = donacionRepository.sumCaloriasEntregadas();
        
        stats.put("totalDonaciones", totalDonaciones);
        stats.put("donacionesEntregadas", donacionesEntregadas);
        stats.put("donacionesPendientes", donacionesPendientes);
        stats.put("totalCalorias", totalCalorias != null ? totalCalorias : 0.0);
        
        double porcentajeEntregadas = totalDonaciones > 0 ? 
            (donacionesEntregadas * 100.0 / totalDonaciones) : 0.0;
        stats.put("porcentajeEntregadas", porcentajeEntregadas);
        
        return stats;
    }
    
    public List<Donacion> obtenerUltimasDonaciones(int cantidad) {
        return donacionRepository.findTop10ByOrderByFechaDesc();
    }
}
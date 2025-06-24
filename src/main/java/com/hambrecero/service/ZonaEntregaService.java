package com.hambrecero.service;

import com.hambrecero.entity.ZonaEntrega;
import com.hambrecero.repository.ZonaEntregaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ZonaEntregaService {
    
    @Autowired
    private ZonaEntregaRepository zonaEntregaRepository;
    
    public List<ZonaEntrega> findAll() {
        return zonaEntregaRepository.findAll();
    }
    
    public List<ZonaEntrega> findAllWithReceptores() {
        return zonaEntregaRepository.findAllWithReceptores();
    }
    
    public Optional<ZonaEntrega> findById(Integer id) {
        return zonaEntregaRepository.findById(id);
    }
    
    public ZonaEntrega save(ZonaEntrega zonaEntrega) {
        return zonaEntregaRepository.save(zonaEntrega);
    }
    
    public ZonaEntrega update(Integer id, ZonaEntrega zonaEntrega) {
        if (!zonaEntregaRepository.existsById(id)) {
            throw new RuntimeException("Zona de entrega no encontrada con ID: " + id);
        }
        zonaEntrega.setIdZona(id);
        return zonaEntregaRepository.save(zonaEntrega);
    }
    
    public void deleteById(Integer id) {
        if (!zonaEntregaRepository.existsById(id)) {
            throw new RuntimeException("Zona de entrega no encontrada con ID: " + id);
        }
        zonaEntregaRepository.deleteById(id);
    }
    
    public List<ZonaEntrega> findByCiudad(String ciudad) {
        return zonaEntregaRepository.findByCiudad(ciudad);
    }
    
    public List<ZonaEntrega> findByNombre(String nombre) {
        return zonaEntregaRepository.findByNombreZonaContainingIgnoreCase(nombre);
    }
    
    public List<String> findDistinctCiudades() {
        return zonaEntregaRepository.findDistinctCiudades();
    }
} 
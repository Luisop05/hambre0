package com.hambrecero.service;

import com.hambrecero.entity.Receptor;
import com.hambrecero.repository.ReceptorRepository;
import com.hambrecero.repository.ZonaEntregaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ReceptorService {
    
    @Autowired
    private ReceptorRepository receptorRepository;
    
    @Autowired
    private ZonaEntregaRepository zonaEntregaRepository;
    
    public List<Receptor> findAll() {
        return receptorRepository.findAll();
    }
    
    public Optional<Receptor> findById(Integer id) {
        return receptorRepository.findById(id);
    }
    
    public Optional<Receptor> findByNumeroDocumento(String numeroDocumento) {
        return receptorRepository.findByNumeroDocumento(numeroDocumento);
    }
    
    public Receptor save(Receptor receptor) {
        // Validar si ya existe el documento
        if (receptor.getIdReceptor() == null && receptorRepository.existsByNumeroDocumento(receptor.getNumeroDocumento())) {
            throw new RuntimeException("Ya existe un receptor con el número de documento: " + receptor.getNumeroDocumento());
        }
        
        // Validar que la zona existe
        if (receptor.getZonaEntrega() != null && receptor.getZonaEntrega().getIdZona() != null) {
            zonaEntregaRepository.findById(receptor.getZonaEntrega().getIdZona())
                    .orElseThrow(() -> new RuntimeException("Zona de entrega no encontrada"));
        }
        
        return receptorRepository.save(receptor);
    }
    
    public Receptor update(Integer id, Receptor receptor) {
        if (!receptorRepository.existsById(id)) {
            throw new RuntimeException("Receptor no encontrado con ID: " + id);
        }
        receptor.setIdReceptor(id);
        return receptorRepository.save(receptor);
    }
    
    public void deleteById(Integer id) {
        if (!receptorRepository.existsById(id)) {
            throw new RuntimeException("Receptor no encontrado con ID: " + id);
        }
        receptorRepository.deleteById(id);
    }
    
    public List<Receptor> findByNombre(String nombre) {
        return receptorRepository.findByNombreContainingIgnoreCase(nombre);
    }
    
    public List<Receptor> findByZona(Integer idZona) {
        return receptorRepository.findByZonaEntregaIdZona(idZona);
    }
    
    public List<Receptor> findByCiudad(String ciudad) {
        return receptorRepository.findByCiudad(ciudad);
    }
} 
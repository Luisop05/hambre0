package com.hambrecero.service;

import com.hambrecero.entity.Donante;
import com.hambrecero.repository.DonanteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class DonanteService {
    
    @Autowired
    private DonanteRepository donanteRepository;
    
    @PersistenceContext
    private EntityManager entityManager;
    
    public List<Donante> findAll() {
        return donanteRepository.findAll();
    }
    
    @Transactional(readOnly = true)
    public Optional<Donante> findById(Integer id) {
        return donanteRepository.findByIdWithDonaciones(id);
    }
    
    public Optional<Donante> findByNumeroDocumento(String numeroDocumento) {
        return donanteRepository.findByNumeroDocumento(numeroDocumento);
    }
    
    public Donante save(Donante donante) {
        // Validar si ya existe el documento
        if (donante.getIdDonante() == null && donanteRepository.existsByNumeroDocumento(donante.getNumeroDocumento())) {
            throw new RuntimeException("Ya existe un donante con el número de documento: " + donante.getNumeroDocumento());
        }
        return donanteRepository.save(donante);
    }
    
    public Donante update(Integer id, Donante donante) {
        if (!donanteRepository.existsById(id)) {
            throw new RuntimeException("Donante no encontrado con ID: " + id);
        }
        donante.setIdDonante(id);
        return donanteRepository.save(donante);
    }
    
    public void deleteById(Integer id) {
        if (!donanteRepository.existsById(id)) {
            throw new RuntimeException("Donante no encontrado con ID: " + id);
        }
        donanteRepository.deleteById(id);
    }
    
    public List<Donante> findByNombre(String nombre) {
        return donanteRepository.findByNombreContainingIgnoreCase(nombre);
    }
    
    public List<Donante> findTopDonantes(int minDonaciones) {
        return donanteRepository.findTopDonantes(minDonaciones);
    }
    
    public List<Donante> findDonantesByCaloriasDesc() {
        return donanteRepository.findDonantesByCaloriasDesc();
    }
} 
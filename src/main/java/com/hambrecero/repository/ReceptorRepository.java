package com.hambrecero.repository;

import com.hambrecero.entity.Receptor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReceptorRepository extends JpaRepository<Receptor, Integer> {
    
    Optional<Receptor> findByNumeroDocumento(String numeroDocumento);
    
    List<Receptor> findByNombreContainingIgnoreCase(String nombre);
    
    List<Receptor> findByZonaEntregaIdZona(Integer idZona);
    
    @Query("SELECT r FROM Receptor r JOIN r.zonaEntrega z WHERE z.ciudad = :ciudad")
    List<Receptor> findByCiudad(@Param("ciudad") String ciudad);
    
    boolean existsByNumeroDocumento(String numeroDocumento);
} 
package com.hambrecero.repository;

import com.hambrecero.entity.ZonaEntrega;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ZonaEntregaRepository extends JpaRepository<ZonaEntrega, Integer> {
    
    List<ZonaEntrega> findByCiudad(String ciudad);
    
    List<ZonaEntrega> findByNombreZonaContainingIgnoreCase(String nombreZona);
    
    @Query("SELECT DISTINCT z.ciudad FROM ZonaEntrega z ORDER BY z.ciudad")
    List<String> findDistinctCiudades();
    
    @Query("SELECT z FROM ZonaEntrega z LEFT JOIN FETCH z.receptores")
    List<ZonaEntrega> findAllWithReceptores();
} 
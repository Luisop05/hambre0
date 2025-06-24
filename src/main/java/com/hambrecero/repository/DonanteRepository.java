package com.hambrecero.repository;

import com.hambrecero.entity.Donante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DonanteRepository extends JpaRepository<Donante, Integer> {
    
    @Query("SELECT DISTINCT d FROM Donante d " +
           "LEFT JOIN FETCH d.donaciones don " +
           "LEFT JOIN FETCH don.receptor " +
           "WHERE d.idDonante = :id")
    Optional<Donante> findByIdWithDonaciones(@Param("id") Integer id);
    
    Optional<Donante> findByNumeroDocumento(String numeroDocumento);
    
    List<Donante> findByNombreContainingIgnoreCase(String nombre);
    
    @Query("SELECT d FROM Donante d WHERE d.totalDonaciones > :minDonaciones ORDER BY d.totalDonaciones DESC")
    List<Donante> findTopDonantes(@Param("minDonaciones") int minDonaciones);
    
    @Query("SELECT d FROM Donante d ORDER BY d.totalCaloriasDonadas DESC")
    List<Donante> findDonantesByCaloriasDesc();
    
    boolean existsByNumeroDocumento(String numeroDocumento);
} 
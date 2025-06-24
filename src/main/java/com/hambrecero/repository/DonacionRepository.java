package com.hambrecero.repository;

import com.hambrecero.entity.Donacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DonacionRepository extends JpaRepository<Donacion, Integer> {
    
    List<Donacion> findByEstado(String estado);
    
    List<Donacion> findByDonanteIdDonante(Integer idDonante);
    
    List<Donacion> findByReceptorIdReceptor(Integer idReceptor);
    
    List<Donacion> findByFechaBetween(LocalDate fechaInicio, LocalDate fechaFin);
    
    @Query("SELECT d FROM Donacion d JOIN FETCH d.donante JOIN FETCH d.receptor ORDER BY d.fecha DESC")
    List<Donacion> findAllWithDetails();
    
    @Query("SELECT COUNT(d) FROM Donacion d WHERE d.estado = :estado")
    Long countByEstado(@Param("estado") String estado);
    
    @Query("SELECT SUM(dd.caloriasTotales) FROM Donacion d JOIN d.detalles dd WHERE d.estado = 'Entregado'")
    Double sumCaloriasEntregadas();
    
    @Modifying
    @Transactional
    @Query("UPDATE Donacion d SET d.estado = :estado, d.observaciones = :observaciones WHERE d.idDonacion = :id")
    int updateEstado(@Param("id") Integer id, @Param("estado") String estado, @Param("observaciones") String observaciones);

    @Query("SELECT COALESCE(SUM(dd.caloriasTotales), 0) FROM Donacion d JOIN d.detalles dd")
    double sumTotalCalorias();
    
    List<Donacion> findTop10ByOrderByFechaDesc();
} 
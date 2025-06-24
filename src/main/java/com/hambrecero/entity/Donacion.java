package com.hambrecero.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "donacion")
public class Donacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_donacion")
    private Integer idDonacion;
    
    @NotNull(message = "La fecha es requerida")
    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_donante", nullable = false)
    private Donante donante;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_receptor", nullable = false)
    private Receptor receptor;
    
    @NotNull(message = "El estado es requerido")
    @Size(max = 20, message = "El estado no puede exceder 20 caracteres")
    @Column(name = "estado", nullable = false, length = 20)
    private String estado = "Pendiente";
    
    @Size(max = 255, message = "Las observaciones no pueden exceder 255 caracteres")
    @Column(name = "observaciones")
    private String observaciones;
    
    @OneToMany(mappedBy = "donacion", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private Set<DetalleDonacion> detalles = new HashSet<>();

    // Constructores
    public Donacion() {
        this.fecha = LocalDate.now();
        this.estado = "Pendiente";
    }

    public Donacion(Donante donante, Receptor receptor, String observaciones) {
        this();
        this.donante = donante;
        this.receptor = receptor;
        this.observaciones = observaciones;
    }

    // Método auxiliar para calcular total de calorías
    public Double getTotalCalorias() {
        if (detalles == null || detalles.isEmpty()) {
            return 0.0;
        }
        return detalles.stream()
                .mapToDouble(DetalleDonacion::getCaloriasTotales)
                .sum();
    }

    // Getters y Setters
    public Integer getIdDonacion() {
        return idDonacion;
    }

    public void setIdDonacion(Integer idDonacion) {
        this.idDonacion = idDonacion;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public Donante getDonante() {
        return donante;
    }

    public void setDonante(Donante donante) {
        this.donante = donante;
    }

    public Receptor getReceptor() {
        return receptor;
    }

    public void setReceptor(Receptor receptor) {
        this.receptor = receptor;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Set<DetalleDonacion> getDetalles() {
        return detalles;
    }

    public void setDetalles(Set<DetalleDonacion> detalles) {
        this.detalles = detalles;
    }
    
    // Método para agregar detalle
    public void addDetalle(DetalleDonacion detalle) {
        detalles.add(detalle);
        detalle.setDonacion(this);
    }
    
    // Método para remover detalle
    public void removeDetalle(DetalleDonacion detalle) {
        detalles.remove(detalle);
        detalle.setDonacion(null);
    }
} 
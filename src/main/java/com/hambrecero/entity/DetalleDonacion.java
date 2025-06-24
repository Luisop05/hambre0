package com.hambrecero.entity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Entity
@Table(name = "detalle_donacion")
public class DetalleDonacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_detalle")
    private Integer idDetalle;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_donacion", nullable = false)
    private Donacion donacion;
    
    @NotBlank(message = "El nombre del alimento es requerido")
    @Size(max = 100, message = "El nombre del alimento no puede exceder 100 caracteres")
    @Column(name = "nombre_alimento", nullable = false, length = 100)
    private String nombreAlimento;
    
    @NotNull(message = "La cantidad es requerida")
    @Positive(message = "La cantidad debe ser positiva")
    @Column(name = "cantidad", nullable = false)
    private Double cantidad;
    
    @NotBlank(message = "La unidad de medida es requerida")
    @Size(max = 20, message = "La unidad de medida no puede exceder 20 caracteres")
    @Column(name = "unidad_medida", nullable = false, length = 20)
    private String unidadMedida;
    
    @NotNull(message = "Las calorías totales son requeridas")
    @Positive(message = "Las calorías deben ser positivas")
    @Column(name = "calorias_totales", nullable = false)
    private Double caloriasTotales;

    // Constructores
    public DetalleDonacion() {}

    public DetalleDonacion(String nombreAlimento, Double cantidad, String unidadMedida, Double caloriasTotales) {
        this.nombreAlimento = nombreAlimento;
        this.cantidad = cantidad;
        this.unidadMedida = unidadMedida;
        this.caloriasTotales = caloriasTotales;
    }

    // Getters y Setters
    public Integer getIdDetalle() {
        return idDetalle;
    }

    public void setIdDetalle(Integer idDetalle) {
        this.idDetalle = idDetalle;
    }

    public Donacion getDonacion() {
        return donacion;
    }

    public void setDonacion(Donacion donacion) {
        this.donacion = donacion;
    }

    public String getNombreAlimento() {
        return nombreAlimento;
    }

    public void setNombreAlimento(String nombreAlimento) {
        this.nombreAlimento = nombreAlimento;
    }

    public Double getCantidad() {
        return cantidad;
    }

    public void setCantidad(Double cantidad) {
        this.cantidad = cantidad;
    }

    public String getUnidadMedida() {
        return unidadMedida;
    }

    public void setUnidadMedida(String unidadMedida) {
        this.unidadMedida = unidadMedida;
    }

    public Double getCaloriasTotales() {
        return caloriasTotales;
    }

    public void setCaloriasTotales(Double caloriasTotales) {
        this.caloriasTotales = caloriasTotales;
    }
} 
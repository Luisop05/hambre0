package com.hambrecero.entity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "zona_entrega")
public class ZonaEntrega {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_zona")
    private Integer idZona;
    
    @NotBlank(message = "El nombre de la zona es requerido")
    @Size(max = 100, message = "El nombre de la zona no puede exceder 100 caracteres")
    @Column(name = "nombre_zona", nullable = false, length = 100)
    private String nombreZona;
    
    @NotBlank(message = "La ciudad es requerida")
    @Size(max = 50, message = "La ciudad no puede exceder 50 caracteres")
    @Column(name = "ciudad", nullable = false, length = 50)
    private String ciudad;
    
    @OneToMany(mappedBy = "zonaEntrega", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Receptor> receptores = new ArrayList<>();

    // Constructores
    public ZonaEntrega() {}

    public ZonaEntrega(String nombreZona, String ciudad) {
        this.nombreZona = nombreZona;
        this.ciudad = ciudad;
    }

    // Getters y Setters
    public Integer getIdZona() {
        return idZona;
    }

    public void setIdZona(Integer idZona) {
        this.idZona = idZona;
    }

    public String getNombreZona() {
        return nombreZona;
    }

    public void setNombreZona(String nombreZona) {
        this.nombreZona = nombreZona;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public List<Receptor> getReceptores() {
        return receptores;
    }

    public void setReceptores(List<Receptor> receptores) {
        this.receptores = receptores;
    }
} 
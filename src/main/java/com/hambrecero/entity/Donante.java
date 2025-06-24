package com.hambrecero.entity;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "donante")
public class Donante {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_donante")
    private Integer idDonante;
    
    @NotBlank(message = "El nombre es requerido")
    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;
    
    @NotBlank(message = "El tipo de documento es requerido")
    @Size(max = 20, message = "El tipo de documento no puede exceder 20 caracteres")
    @Column(name = "tipo_documento", nullable = false, length = 20)
    private String tipoDocumento;
    
    @NotBlank(message = "El número de documento es requerido")
    @Size(max = 20, message = "El número de documento no puede exceder 20 caracteres")
    @Column(name = "numero_documento", nullable = false, unique = true, length = 20)
    private String numeroDocumento;
    
    @Email(message = "El email debe ser válido")
    @Column(name = "email", length = 100)
    private String email;
    
    @Size(max = 20, message = "El teléfono no puede exceder 20 caracteres")
    @Column(name = "telefono", length = 20)
    private String telefono;
    
    @Column(name = "total_donaciones")
    private Integer totalDonaciones = 0;
    
    @Column(name = "total_calorias_donadas")
    private Double totalCaloriasDonadas = 0.0;
    
    @OneToMany(mappedBy = "donante", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Donacion> donaciones = new HashSet<>();

    // Constructores
    public Donante() {}

    public Donante(String nombre, String tipoDocumento, String numeroDocumento) {
        this.nombre = nombre;
        this.tipoDocumento = tipoDocumento;
        this.numeroDocumento = numeroDocumento;
    }

    // Getters y Setters
    public Integer getIdDonante() {
        return idDonante;
    }

    public void setIdDonante(Integer idDonante) {
        this.idDonante = idDonante;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public String getNumeroDocumento() {
        return numeroDocumento;
    }

    public void setNumeroDocumento(String numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public Integer getTotalDonaciones() {
        return totalDonaciones;
    }

    public void setTotalDonaciones(Integer totalDonaciones) {
        this.totalDonaciones = totalDonaciones;
    }

    public Double getTotalCaloriasDonadas() {
        return totalCaloriasDonadas;
    }

    public void setTotalCaloriasDonadas(Double totalCaloriasDonadas) {
        this.totalCaloriasDonadas = totalCaloriasDonadas;
    }

    public Set<Donacion> getDonaciones() {
        return donaciones;
    }

    public void setDonaciones(Set<Donacion> donaciones) {
        this.donaciones = donaciones;
    }

    // Método para agregar donación
    public void addDonacion(Donacion donacion) {
        donaciones.add(donacion);
        donacion.setDonante(this);
        this.totalDonaciones++;
        this.totalCaloriasDonadas += donacion.getTotalCalorias();
    }

    // Método para remover donación
    public void removeDonacion(Donacion donacion) {
        donaciones.remove(donacion);
        donacion.setDonante(null);
        this.totalDonaciones--;
        this.totalCaloriasDonadas -= donacion.getTotalCalorias();
    }
} 
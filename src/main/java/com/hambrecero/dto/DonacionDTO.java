package com.hambrecero.dto;

import java.time.LocalDate;

public class DonacionDTO {
    private int idDonacion;
    private LocalDate fecha;
    private int idDonante;
    private int idReceptor;
    private String estado;
    private String observaciones;
    private String nombreDonante;
    private String nombreReceptor;
    private String zonaEntrega;
    private double totalCalorias;

    // Constructores
    public DonacionDTO() {}

    public DonacionDTO(int idDonacion, LocalDate fecha, int idDonante,
                       int idReceptor, String estado, String observaciones) {
        this.idDonacion = idDonacion;
        this.fecha = fecha;
        this.idDonante = idDonante;
        this.idReceptor = idReceptor;
        this.estado = estado;
        this.observaciones = observaciones;
    }

    // Getters y Setters
    public int getIdDonacion() { return idDonacion; }
    public void setIdDonacion(int idDonacion) { this.idDonacion = idDonacion; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public int getIdDonante() { return idDonante; }
    public void setIdDonante(int idDonante) { this.idDonante = idDonante; }

    public int getIdReceptor() { return idReceptor; }
    public void setIdReceptor(int idReceptor) { this.idReceptor = idReceptor; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }

    public String getNombreDonante() { return nombreDonante; }
    public void setNombreDonante(String nombreDonante) { this.nombreDonante = nombreDonante; }

    public String getNombreReceptor() { return nombreReceptor; }
    public void setNombreReceptor(String nombreReceptor) { this.nombreReceptor = nombreReceptor; }

    public String getZonaEntrega() { return zonaEntrega; }
    public void setZonaEntrega(String zonaEntrega) { this.zonaEntrega = zonaEntrega; }

    public double getTotalCalorias() { return totalCalorias; }
    public void setTotalCalorias(double totalCalorias) { this.totalCalorias = totalCalorias; }

    @Override
    public String toString() {
        return "DonacionDTO{" +
                "idDonacion=" + idDonacion +
                ", fecha=" + fecha +
                ", nombreDonante='" + nombreDonante + '\'' +
                ", nombreReceptor='" + nombreReceptor + '\'' +
                ", estado='" + estado + '\'' +
                '}';
    }
}
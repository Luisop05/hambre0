package com.hambrecero.dto;

public class DonanteDTO {
    private int idDonante;
    private String nombre;
    private String tipoDocumento;
    private String numeroDocumento;
    private String email;
    private String telefono;
    private int totalDonaciones;
    private double totalCaloriasDonadas;

    // Constructores
    public DonanteDTO() {}

    public DonanteDTO(String nombre, String tipoDocumento, String numeroDocumento,
                      String email, String telefono) {
        this.nombre = nombre;
        this.tipoDocumento = tipoDocumento;
        this.numeroDocumento = numeroDocumento;
        this.email = email;
        this.telefono = telefono;
    }

    // Getters y Setters
    public int getIdDonante() { return idDonante; }
    public void setIdDonante(int idDonante) { this.idDonante = idDonante; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getTipoDocumento() { return tipoDocumento; }
    public void setTipoDocumento(String tipoDocumento) { this.tipoDocumento = tipoDocumento; }

    public String getNumeroDocumento() { return numeroDocumento; }
    public void setNumeroDocumento(String numeroDocumento) { this.numeroDocumento = numeroDocumento; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public int getTotalDonaciones() { return totalDonaciones; }
    public void setTotalDonaciones(int totalDonaciones) { this.totalDonaciones = totalDonaciones; }

    public double getTotalCaloriasDonadas() { return totalCaloriasDonadas; }
    public void setTotalCaloriasDonadas(double totalCaloriasDonadas) { this.totalCaloriasDonadas = totalCaloriasDonadas; }

    @Override
    public String toString() {
        return "DonanteDTO{" +
                "idDonante=" + idDonante +
                ", nombre='" + nombre + '\'' +
                ", numeroDocumento='" + numeroDocumento + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
package com.hambrecero.service;

import com.hambrecero.dao.DonacionDAO;
import com.hambrecero.dao.DonanteDAO;
import com.hambrecero.dto.DonacionDTO;
import com.hambrecero.dto.DonanteDTO;

import java.sql.Connection;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DonacionService {
    private DonacionDAO donacionDAO;
    private DonanteDAO donanteDAO;

    public DonacionService(Connection connection) {
        this.donacionDAO = new DonacionDAO(connection);
        this.donanteDAO = new DonanteDAO(connection);
    }

    // Crear una nueva donación
    public DonacionDTO crearDonacion(int idDonante, int idReceptor, String observaciones) {
        DonacionDTO nuevaDonacion = new DonacionDTO();
        nuevaDonacion.setFecha(LocalDate.now());
        nuevaDonacion.setIdDonante(idDonante);
        nuevaDonacion.setIdReceptor(idReceptor);
        nuevaDonacion.setEstado("Pendiente");
        nuevaDonacion.setObservaciones(observaciones);

        return donacionDAO.crear(nuevaDonacion);
    }

    // Actualizar estado de donación
    public boolean actualizarEstadoDonacion(int idDonacion, String nuevoEstado, String observaciones) {
        DonacionDTO donacion = donacionDAO.obtenerPorId(idDonacion);

        if (donacion != null) {
            donacion.setEstado(nuevoEstado);
            donacion.setObservaciones(observaciones);

            DonacionDTO actualizada = donacionDAO.actualizar(donacion);
            return actualizada != null;
        }

        return false;
    }

    // Obtener estadísticas generales
    public Map<String, Object> obtenerEstadisticas() {
        Map<String, Object> estadisticas = new HashMap<>();

        List<DonacionDTO> todasLasDonaciones = donacionDAO.obtenerTodos();

        int totalDonaciones = todasLasDonaciones.size();
        double totalCalorias = todasLasDonaciones.stream()
                .mapToDouble(DonacionDTO::getTotalCalorias)
                .sum();

        long donacionesEntregadas = todasLasDonaciones.stream()
                .filter(d -> "Entregado".equals(d.getEstado()))
                .count();

        long donacionesPendientes = todasLasDonaciones.stream()
                .filter(d -> "Pendiente".equals(d.getEstado()))
                .count();

        estadisticas.put("totalDonaciones", totalDonaciones);
        estadisticas.put("totalCalorias", totalCalorias);
        estadisticas.put("donacionesEntregadas", donacionesEntregadas);
        estadisticas.put("donacionesPendientes", donacionesPendientes);
        estadisticas.put("porcentajeEntregadas",
                totalDonaciones > 0 ? (donacionesEntregadas * 100.0 / totalDonaciones) : 0);

        return estadisticas;
    }

    // Obtener donaciones por estado
    public List<DonacionDTO> obtenerDonacionesPorEstado(String estado) {
        return donacionDAO.obtenerPorEstado(estado);
    }

    // Obtener todos los donantes
    public List<DonanteDTO> obtenerTodosLosDonantes() {
        return donanteDAO.obtenerTodos();
    }
}
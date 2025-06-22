package com.hambrecero.dao;

import com.hambrecero.dto.DonacionDTO;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DonacionDAO implements BaseDAO<DonacionDTO, Integer> {
    private Connection connection;

    public DonacionDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public DonacionDTO crear(DonacionDTO donacion) {
        String sql = "INSERT INTO donacion (fecha, id_donante, id_receptor, estado, observaciones) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setDate(1, Date.valueOf(donacion.getFecha()));
            pstmt.setInt(2, donacion.getIdDonante());
            pstmt.setInt(3, donacion.getIdReceptor());
            pstmt.setString(4, donacion.getEstado());
            pstmt.setString(5, donacion.getObservaciones());

            int filasAfectadas = pstmt.executeUpdate();

            if (filasAfectadas > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        donacion.setIdDonacion(generatedKeys.getInt(1));
                    }
                }
            }

            return donacion;
        } catch (SQLException e) {
            throw new RuntimeException("Error al crear donación: " + e.getMessage(), e);
        }
    }

    @Override
    public DonacionDTO obtenerPorId(Integer id) {
        String sql = "SELECT d.id_donacion, d.fecha, d.id_donante, d.id_receptor, d.estado, d.observaciones, " +
                "don.nombre as nombre_donante, rec.nombre as nombre_receptor, " +
                "ze.nombre_zona, COALESCE(SUM(dd.calorias_totales), 0) as total_calorias " +
                "FROM donacion d " +
                "INNER JOIN donante don ON d.id_donante = don.id_donante " +
                "INNER JOIN receptor rec ON d.id_receptor = rec.id_receptor " +
                "INNER JOIN zona_entrega ze ON rec.id_zona_entrega = ze.id_zona " +
                "LEFT JOIN detalle_donacion dd ON d.id_donacion = dd.id_donacion " +
                "WHERE d.id_donacion = ? " +
                "GROUP BY d.id_donacion";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToDonacionDTO(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener donación: " + e.getMessage(), e);
        }

        return null;
    }

    @Override
    public List<DonacionDTO> obtenerTodos() {
        String sql = "SELECT d.id_donacion, d.fecha, d.id_donante, d.id_receptor, d.estado, d.observaciones, " +
                "don.nombre as nombre_donante, rec.nombre as nombre_receptor, " +
                "ze.nombre_zona, COALESCE(SUM(dd.calorias_totales), 0) as total_calorias " +
                "FROM donacion d " +
                "INNER JOIN donante don ON d.id_donante = don.id_donante " +
                "INNER JOIN receptor rec ON d.id_receptor = rec.id_receptor " +
                "INNER JOIN zona_entrega ze ON rec.id_zona_entrega = ze.id_zona " +
                "LEFT JOIN detalle_donacion dd ON d.id_donacion = dd.id_donacion " +
                "GROUP BY d.id_donacion " +
                "ORDER BY d.fecha DESC";

        List<DonacionDTO> donaciones = new ArrayList<>();

        try (PreparedStatement pstmt = connection.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                donaciones.add(mapResultSetToDonacionDTO(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener donaciones: " + e.getMessage(), e);
        }

        return donaciones;
    }

    @Override
    public DonacionDTO actualizar(DonacionDTO donacion) {
        String sql = "UPDATE donacion SET fecha = ?, id_donante = ?, id_receptor = ?, estado = ?, observaciones = ? WHERE id_donacion = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setDate(1, Date.valueOf(donacion.getFecha()));
            pstmt.setInt(2, donacion.getIdDonante());
            pstmt.setInt(3, donacion.getIdReceptor());
            pstmt.setString(4, donacion.getEstado());
            pstmt.setString(5, donacion.getObservaciones());
            pstmt.setInt(6, donacion.getIdDonacion());

            int filasAfectadas = pstmt.executeUpdate();

            if (filasAfectadas > 0) {
                return obtenerPorId(donacion.getIdDonacion());
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar donación: " + e.getMessage(), e);
        }

        return null;
    }

    @Override
    public boolean eliminar(Integer id) {
        String sql = "DELETE FROM donacion WHERE id_donacion = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);

            int filasAfectadas = pstmt.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar donación: " + e.getMessage(), e);
        }
    }

    // Método específico para obtener donaciones por estado
    public List<DonacionDTO> obtenerPorEstado(String estado) {
        String sql = "SELECT d.id_donacion, d.fecha, d.id_donante, d.id_receptor, d.estado, d.observaciones, " +
                "don.nombre as nombre_donante, rec.nombre as nombre_receptor, " +
                "ze.nombre_zona, COALESCE(SUM(dd.calorias_totales), 0) as total_calorias " +
                "FROM donacion d " +
                "INNER JOIN donante don ON d.id_donante = don.id_donante " +
                "INNER JOIN receptor rec ON d.id_receptor = rec.id_receptor " +
                "INNER JOIN zona_entrega ze ON rec.id_zona_entrega = ze.id_zona " +
                "LEFT JOIN detalle_donacion dd ON d.id_donacion = dd.id_donacion " +
                "WHERE d.estado = ? " +
                "GROUP BY d.id_donacion " +
                "ORDER BY d.fecha DESC";

        List<DonacionDTO> donaciones = new ArrayList<>();

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, estado);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    donaciones.add(mapResultSetToDonacionDTO(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener donaciones por estado: " + e.getMessage(), e);
        }

        return donaciones;
    }

    // Método adicional para obtener donaciones por fechas
    public List<DonacionDTO> obtenerPorFechas(LocalDate fechaInicio, LocalDate fechaFin) {
        String sql = "SELECT d.id_donacion, d.fecha, d.id_donante, d.id_receptor, d.estado, d.observaciones, " +
                "don.nombre as nombre_donante, rec.nombre as nombre_receptor, " +
                "ze.nombre_zona, COALESCE(SUM(dd.calorias_totales), 0) as total_calorias " +
                "FROM donacion d " +
                "INNER JOIN donante don ON d.id_donante = don.id_donante " +
                "INNER JOIN receptor rec ON d.id_receptor = rec.id_receptor " +
                "INNER JOIN zona_entrega ze ON rec.id_zona_entrega = ze.id_zona " +
                "LEFT JOIN detalle_donacion dd ON d.id_donacion = dd.id_donacion " +
                "WHERE d.fecha BETWEEN ? AND ? " +
                "GROUP BY d.id_donacion " +
                "ORDER BY d.fecha DESC";

        List<DonacionDTO> donaciones = new ArrayList<>();

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setDate(1, Date.valueOf(fechaInicio));
            pstmt.setDate(2, Date.valueOf(fechaFin));

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    donaciones.add(mapResultSetToDonacionDTO(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener donaciones por fechas: " + e.getMessage(), e);
        }

        return donaciones;
    }

    private DonacionDTO mapResultSetToDonacionDTO(ResultSet rs) throws SQLException {
        DonacionDTO donacion = new DonacionDTO();
        donacion.setIdDonacion(rs.getInt("id_donacion"));
        donacion.setFecha(rs.getDate("fecha").toLocalDate());
        donacion.setIdDonante(rs.getInt("id_donante"));
        donacion.setIdReceptor(rs.getInt("id_receptor"));
        donacion.setEstado(rs.getString("estado"));
        donacion.setObservaciones(rs.getString("observaciones"));
        donacion.setNombreDonante(rs.getString("nombre_donante"));
        donacion.setNombreReceptor(rs.getString("nombre_receptor"));
        donacion.setZonaEntrega(rs.getString("nombre_zona"));
        donacion.setTotalCalorias(rs.getDouble("total_calorias"));
        return donacion;
    }
}
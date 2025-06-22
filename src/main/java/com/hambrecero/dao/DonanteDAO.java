package com.hambrecero.dao;

import com.hambrecero.dto.DonanteDTO;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DonanteDAO implements BaseDAO<DonanteDTO, Integer> {
    private Connection connection;

    public DonanteDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public DonanteDTO crear(DonanteDTO donante) {
        String sql = "INSERT INTO donante (nombre, tipo_documento, numero_documento, email, telefono) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, donante.getNombre());
            pstmt.setString(2, donante.getTipoDocumento());
            pstmt.setString(3, donante.getNumeroDocumento());
            pstmt.setString(4, donante.getEmail());
            pstmt.setString(5, donante.getTelefono());

            int filasAfectadas = pstmt.executeUpdate();

            if (filasAfectadas > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        donante.setIdDonante(generatedKeys.getInt(1));
                    }
                }
            }

            return donante;
        } catch (SQLException e) {
            throw new RuntimeException("Error al crear donante: " + e.getMessage(), e);
        }
    }

    @Override
    public DonanteDTO obtenerPorId(Integer id) {
        String sql = "SELECT d.id_donante, d.nombre, d.tipo_documento, d.numero_documento, d.email, d.telefono, " +
                "COUNT(don.id_donacion) as total_donaciones, " +
                "COALESCE(SUM(dd.calorias_totales), 0) as total_calorias_donadas " +
                "FROM donante d " +
                "LEFT JOIN donacion don ON d.id_donante = don.id_donante " +
                "LEFT JOIN detalle_donacion dd ON don.id_donacion = dd.id_donacion " +
                "WHERE d.id_donante = ? " +
                "GROUP BY d.id_donante";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToDonanteDTO(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener donante: " + e.getMessage(), e);
        }

        return null;
    }

    @Override
    public List<DonanteDTO> obtenerTodos() {
        String sql = "SELECT d.id_donante, d.nombre, d.tipo_documento, d.numero_documento, d.email, d.telefono, " +
                "COUNT(don.id_donacion) as total_donaciones, " +
                "COALESCE(SUM(dd.calorias_totales), 0) as total_calorias_donadas " +
                "FROM donante d " +
                "LEFT JOIN donacion don ON d.id_donante = don.id_donante " +
                "LEFT JOIN detalle_donacion dd ON don.id_donacion = dd.id_donacion " +
                "GROUP BY d.id_donante " +
                "ORDER BY d.nombre";

        List<DonanteDTO> donantes = new ArrayList<>();

        try (PreparedStatement pstmt = connection.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                donantes.add(mapResultSetToDonanteDTO(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener donantes: " + e.getMessage(), e);
        }

        return donantes;
    }

    @Override
    public DonanteDTO actualizar(DonanteDTO donante) {
        String sql = "UPDATE donante SET nombre = ?, tipo_documento = ?, numero_documento = ?, email = ?, telefono = ? WHERE id_donante = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, donante.getNombre());
            pstmt.setString(2, donante.getTipoDocumento());
            pstmt.setString(3, donante.getNumeroDocumento());
            pstmt.setString(4, donante.getEmail());
            pstmt.setString(5, donante.getTelefono());
            pstmt.setInt(6, donante.getIdDonante());

            int filasAfectadas = pstmt.executeUpdate();

            if (filasAfectadas > 0) {
                return obtenerPorId(donante.getIdDonante());
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar donante: " + e.getMessage(), e);
        }

        return null;
    }

    @Override
    public boolean eliminar(Integer id) {
        String sql = "DELETE FROM donante WHERE id_donante = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);

            int filasAfectadas = pstmt.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar donante: " + e.getMessage(), e);
        }
    }

    // Método específico para buscar por documento
    public DonanteDTO obtenerPorDocumento(String numeroDocumento) {
        String sql = "SELECT d.id_donante, d.nombre, d.tipo_documento, d.numero_documento, d.email, d.telefono, " +
                "COUNT(don.id_donacion) as total_donaciones, " +
                "COALESCE(SUM(dd.calorias_totales), 0) as total_calorias_donadas " +
                "FROM donante d " +
                "LEFT JOIN donacion don ON d.id_donante = don.id_donante " +
                "LEFT JOIN detalle_donacion dd ON don.id_donacion = dd.id_donacion " +
                "WHERE d.numero_documento = ? " +
                "GROUP BY d.id_donante";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, numeroDocumento);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToDonanteDTO(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener donante por documento: " + e.getMessage(), e);
        }

        return null;
    }

    private DonanteDTO mapResultSetToDonanteDTO(ResultSet rs) throws SQLException {
        DonanteDTO donante = new DonanteDTO();
        donante.setIdDonante(rs.getInt("id_donante"));
        donante.setNombre(rs.getString("nombre"));
        donante.setTipoDocumento(rs.getString("tipo_documento"));
        donante.setNumeroDocumento(rs.getString("numero_documento"));
        donante.setEmail(rs.getString("email"));
        donante.setTelefono(rs.getString("telefono"));
        donante.setTotalDonaciones(rs.getInt("total_donaciones"));
        donante.setTotalCaloriasDonadas(rs.getDouble("total_calorias_donadas"));
        return donante;
    }
}
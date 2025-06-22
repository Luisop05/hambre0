package com.hambrecero.util;

import com.hambrecero.config.DatabaseConfig;
import java.sql.*;

public class ConsultasSQL {

    public static void ejecutarConsultasDemo() {
        try (Connection connection = DatabaseConfig.getConnection()) {
            System.out.println("=== DEMOSTRANDO CONSULTAS SQL ===\n");

            // Consulta 1: INNER JOIN - Donaciones con información completa
            consulta1_InnerJoin(connection);

            // Consulta 2: UPDATE - Actualizar estados
            consulta2_Update(connection);

            // Consulta 3: UNION - Combinar resultados
            consulta3_Union(connection);

            // Consulta 4: Agregaciones y GROUP BY
            consulta4_Agregaciones(connection);

        } catch (SQLException e) {
            System.err.println("Error ejecutando consultas: " + e.getMessage());
        }
    }

    private static void consulta1_InnerJoin(Connection connection) throws SQLException {
        System.out.println("1️⃣ CONSULTA CON INNER JOIN - Donaciones completas:");
        System.out.println("================================================");

        String sql = "SELECT " +
                "d.id_donacion, " +
                "d.fecha, " +
                "don.nombre as donante, " +
                "rec.nombre as receptor, " +
                "ze.nombre_zona, " +
                "ze.ciudad, " +
                "d.estado " +
                "FROM donacion d " +
                "INNER JOIN donante don ON d.id_donante = don.id_donante " +
                "INNER JOIN receptor rec ON d.id_receptor = rec.id_receptor " +
                "INNER JOIN zona_entrega ze ON rec.id_zona_entrega = ze.id_zona " +
                "ORDER BY d.fecha DESC " +
                "LIMIT 5";

        try (PreparedStatement pstmt = connection.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                System.out.printf("ID: %d | Fecha: %s | Donante: %s | Receptor: %s | Zona: %s, %s | Estado: %s%n",
                        rs.getInt("id_donacion"),
                        rs.getDate("fecha"),
                        rs.getString("donante"),
                        rs.getString("receptor"),
                        rs.getString("nombre_zona"),
                        rs.getString("ciudad"),
                        rs.getString("estado")
                );
            }
        }
        System.out.println();
    }

    private static void consulta2_Update(Connection connection) throws SQLException {
        System.out.println("2️⃣ CONSULTA UPDATE - Actualizar observaciones:");
        System.out.println("=============================================");

        String sql = "UPDATE donacion " +
                "SET observaciones = CONCAT(COALESCE(observaciones, ''), ' - Actualizado desde IDE') " +
                "WHERE estado = 'Pendiente' " +
                "AND observaciones NOT LIKE '%Actualizado desde IDE%'";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            int filasAfectadas = pstmt.executeUpdate();
            System.out.println("Filas actualizadas: " + filasAfectadas);
        }
        System.out.println();
    }

    private static void consulta3_Union(Connection connection) throws SQLException {
        System.out.println("3️⃣ CONSULTA CON UNION - Participantes del sistema:");
        System.out.println("===============================================");

        String sql = "SELECT " +
                "'Donante' as tipo, " +
                "nombre, " +
                "numero_documento, " +
                "NULL as zona " +
                "FROM donante " +
                "UNION ALL " +
                "SELECT " +
                "'Receptor' as tipo, " +
                "r.nombre, " +
                "r.numero_documento, " +
                "ze.nombre_zona " +
                "FROM receptor r " +
                "INNER JOIN zona_entrega ze ON r.id_zona_entrega = ze.id_zona " +
                "ORDER BY tipo, nombre " +
                "LIMIT 10";

        try (PreparedStatement pstmt = connection.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                System.out.printf("Tipo: %s | Nombre: %s | Documento: %s | Zona: %s%n",
                        rs.getString("tipo"),
                        rs.getString("nombre"),
                        rs.getString("numero_documento"),
                        rs.getString("zona") != null ? rs.getString("zona") : "N/A"
                );
            }
        }
        System.out.println();
    }

    private static void consulta4_Agregaciones(Connection connection) throws SQLException {
        System.out.println("4️⃣ CONSULTA CON AGREGACIONES - Estadísticas por zona:");
        System.out.println("===================================================");

        String sql = "SELECT " +
                "ze.nombre_zona, " +
                "ze.ciudad, " +
                "COUNT(DISTINCT d.id_donacion) as total_donaciones, " +
                "COALESCE(SUM(dd.calorias_totales), 0) as total_calorias, " +
                "COUNT(DISTINCT r.id_receptor) as total_receptores " +
                "FROM zona_entrega ze " +
                "LEFT JOIN receptor r ON ze.id_zona = r.id_zona_entrega " +
                "LEFT JOIN donacion d ON r.id_receptor = d.id_receptor " +
                "LEFT JOIN detalle_donacion dd ON d.id_donacion = dd.id_donacion " +
                "GROUP BY ze.id_zona, ze.nombre_zona, ze.ciudad " +
                "ORDER BY total_calorias DESC";

        try (PreparedStatement pstmt = connection.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                System.out.printf("Zona: %s, %s | Donaciones: %d | Calorías: %.2f | Receptores: %d%n",
                        rs.getString("nombre_zona"),
                        rs.getString("ciudad"),
                        rs.getInt("total_donaciones"),
                        rs.getDouble("total_calorias"),
                        rs.getInt("total_receptores")
                );
            }
        }
        System.out.println();
    }

    // Consulta adicional 5: SELECT con COUNT y estadísticas generales
    private static void consulta5_Estadisticas(Connection connection) throws SQLException {
        System.out.println("5️⃣ CONSULTA DE ESTADÍSTICAS GENERALES:");
        System.out.println("====================================");

        String sql = "SELECT " +
                "COUNT(DISTINCT d.id_donacion) as total_donaciones, " +
                "COUNT(DISTINCT d.id_donante) as total_donantes, " +
                "COUNT(DISTINCT d.id_receptor) as total_receptores, " +
                "SUM(CASE WHEN d.estado = 'Entregado' THEN 1 ELSE 0 END) as donaciones_entregadas, " +
                "SUM(CASE WHEN d.estado = 'Pendiente' THEN 1 ELSE 0 END) as donaciones_pendientes, " +
                "COALESCE(SUM(dd.calorias_totales), 0) as total_calorias " +
                "FROM donacion d " +
                "LEFT JOIN detalle_donacion dd ON d.id_donacion = dd.id_donacion";

        try (PreparedStatement pstmt = connection.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            if (rs.next()) {
                System.out.printf("Total donaciones: %d%n", rs.getInt("total_donaciones"));
                System.out.printf("Total donantes: %d%n", rs.getInt("total_donantes"));
                System.out.printf("Total receptores: %d%n", rs.getInt("total_receptores"));
                System.out.printf("Donaciones entregadas: %d%n", rs.getInt("donaciones_entregadas"));
                System.out.printf("Donaciones pendientes: %d%n", rs.getInt("donaciones_pendientes"));
                System.out.printf("Total calorías donadas: %.2f%n", rs.getDouble("total_calorias"));
            }
        }
        System.out.println();
    }

    // Consulta adicional 6: DELETE con condiciones
    private static void consulta6_Delete(Connection connection) throws SQLException {
        System.out.println("6️⃣ CONSULTA DELETE - Limpiar datos de prueba:");
        System.out.println("============================================");

        // Primero verificamos qué vamos a eliminar
        String sqlSelect = "SELECT COUNT(*) as total " +
                "FROM donacion " +
                "WHERE observaciones LIKE '%prueba%' " +
                "AND estado = 'Pendiente'";

        try (PreparedStatement pstmt = connection.prepareStatement(sqlSelect);
             ResultSet rs = pstmt.executeQuery()) {

            if (rs.next()) {
                int total = rs.getInt("total");
                System.out.println("Donaciones de prueba encontradas: " + total);

                if (total > 0) {
                    // Comentamos la eliminación real para seguridad
                    System.out.println("(DELETE deshabilitado por seguridad - solo demostración)");
                    /*
                    String sqlDelete = "DELETE FROM donacion " +
                                      "WHERE observaciones LIKE '%prueba%' " +
                                      "AND estado = 'Pendiente'";

                    try (PreparedStatement pstmtDelete = connection.prepareStatement(sqlDelete)) {
                        int filasEliminadas = pstmtDelete.executeUpdate();
                        System.out.println("Filas eliminadas: " + filasEliminadas);
                    }
                    */
                }
            }
        }
        System.out.println();
    }

    // Método principal que ejecuta todas las consultas
    public static void ejecutarTodasLasConsultas() {
        try (Connection connection = DatabaseConfig.getConnection()) {
            System.out.println("=== DEMOSTRACIÓN COMPLETA DE CONSULTAS SQL ===\n");

            consulta1_InnerJoin(connection);
            consulta2_Update(connection);
            consulta3_Union(connection);
            consulta4_Agregaciones(connection);
            consulta5_Estadisticas(connection);
            consulta6_Delete(connection);

            System.out.println("=== DEMOSTRACIÓN COMPLETADA ===");

        } catch (SQLException e) {
            System.err.println("Error ejecutando consultas: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ejecutarTodasLasConsultas();
    }
}
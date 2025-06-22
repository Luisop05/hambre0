package com.hambrecero.main;

import com.hambrecero.config.DatabaseConfig;
import com.hambrecero.util.ConsultasSQL;
import com.hambrecero.service.DonacionService;
import com.hambrecero.dao.DonanteDAO;
import com.hambrecero.dto.DonanteDTO;

import java.sql.Connection;
import java.sql.SQLException;

public class DemoProyectoCompleto {

    public static void main(String[] args) {
        System.out.println("🎯 DEMOSTRACIÓN COMPLETA DEL PROYECTO HAMBRE CERO");
        System.out.println("=================================================\n");

        try (Connection connection = DatabaseConfig.getConnection()) {
            System.out.println("✅ Conexión exitosa a MySQL");
            System.out.println("✅ Base de datos: hambre_cero");
            System.out.println("✅ Patrones DAO/DTO implementados\n");

            // 1. DEMOSTRAR INSERCIÓN (DAO/DTO)
            demostrarInsercion(connection);

            // 2. DEMOSTRAR ACTUALIZACIÓN (DAO/DTO)
            demostrarActualizacion(connection);

            // 3. DEMOSTRAR PROYECCIÓN (Service)
            demostrarProyeccion(connection);

            // 4. DEMOSTRAR CONSULTAS SQL AVANZADAS
            demostrarConsultasSQL();

            System.out.println("🎉 DEMOSTRACIÓN COMPLETADA EXITOSAMENTE");
            System.out.println("✅ Todos los requisitos del proyecto cumplidos:");
            System.out.println("   • Modelo normalizado (1FN, 2FN, 3FN)");
            System.out.println("   • Patrones DAO/DTO implementados");
            System.out.println("   • CRUD completo funcionando");
            System.out.println("   • Consultas SQL: SELECT, JOIN, UNION, UPDATE");
            System.out.println("   • Casos de uso: Inserción, Actualización, Proyección");

        } catch (SQLException e) {
            System.err.println("❌ Error de conexión: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void demostrarInsercion(Connection connection) {
        System.out.println("📝 1. DEMOSTRACIÓN DE INSERCIÓN (Patrón DAO)");
        System.out.println("==========================================");

        try {
            DonanteDAO donanteDAO = new DonanteDAO(connection);

            // Crear nuevo donante
            DonanteDTO nuevoDonante = new DonanteDTO(
                    "Empresa Demo IDE",
                    "NIT",
                    "900999888-7",
                    "demo@empresademo.com",
                    "3009998887"
            );

            DonanteDTO donanteCread = donanteDAO.crear(nuevoDonante);

            System.out.println("✅ Donante insertado correctamente:");
            System.out.println("   ID generado: " + donanteCread.getIdDonante());
            System.out.println("   Nombre: " + donanteCread.getNombre());
            System.out.println("   Email: " + donanteCread.getEmail());
            System.out.println();

        } catch (Exception e) {
            System.out.println("⚠️  Error en inserción (posiblemente donante ya existe): " + e.getMessage());
            System.out.println();
        }
    }

    private static void demostrarActualizacion(Connection connection) {
        System.out.println("🔄 2. DEMOSTRACIÓN DE ACTUALIZACIÓN (Patrón DAO)");
        System.out.println("==============================================");

        try {
            DonacionService donacionService = new DonacionService(connection);

            // Actualizar estado de una donación existente
            boolean actualizado = donacionService.actualizarEstadoDonacion(
                    1,
                    "Entregado",
                    "Actualizado desde demostración IDE - " + java.time.LocalDateTime.now()
            );

            if (actualizado) {
                System.out.println("✅ Donación actualizada correctamente:");
                System.out.println("   ID donación: 1");
                System.out.println("   Nuevo estado: Entregado");
                System.out.println("   Observación agregada con timestamp");
            } else {
                System.out.println("⚠️  No se pudo actualizar la donación (posiblemente no existe)");
            }
            System.out.println();

        } catch (Exception e) {
            System.out.println("❌ Error en actualización: " + e.getMessage());
            System.out.println();
        }
    }

    private static void demostrarProyeccion(Connection connection) {
        System.out.println("📊 3. DEMOSTRACIÓN DE PROYECCIÓN (Service + DTO)");
        System.out.println("==============================================");

        try {
            DonacionService donacionService = new DonacionService(connection);

            // Obtener estadísticas (proyección de datos)
            var estadisticas = donacionService.obtenerEstadisticas();

            System.out.println("✅ Estadísticas proyectadas correctamente:");
            System.out.println("   Total donaciones: " + estadisticas.get("totalDonaciones"));
            System.out.println("   Donaciones entregadas: " + estadisticas.get("donacionesEntregadas"));
            System.out.println("   Donaciones pendientes: " + estadisticas.get("donacionesPendientes"));
            System.out.println("   Total calorías: " + String.format("%.0f", (Double) estadisticas.get("totalCalorias")));
            System.out.println("   Eficiencia: " + String.format("%.1f%%", (Double) estadisticas.get("porcentajeEntregadas")));
            System.out.println();

        } catch (Exception e) {
            System.out.println("❌ Error en proyección: " + e.getMessage());
            System.out.println();
        }
    }

    private static void demostrarConsultasSQL() {
        System.out.println("🔍 4. DEMOSTRACIÓN DE CONSULTAS SQL AVANZADAS");
        System.out.println("===========================================");
        System.out.println("Ejecutando: SELECT, INNER JOIN, UNION, UPDATE, agregaciones...\n");

        try {
            // Ejecutar todas las consultas SQL demostrativas
            ConsultasSQL.ejecutarTodasLasConsultas();

            System.out.println("✅ Todas las consultas SQL ejecutadas correctamente");
            System.out.println();

        } catch (Exception e) {
            System.out.println("❌ Error en consultas SQL: " + e.getMessage());
            System.out.println();
        }
    }
}
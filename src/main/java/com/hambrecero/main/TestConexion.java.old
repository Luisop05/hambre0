package com.hambrecero.main;

import com.hambrecero.config.DatabaseConfig;
import com.hambrecero.service.DonacionService;
import com.hambrecero.util.ConsultasSQL;
import com.hambrecero.dto.DonacionDTO;
import com.hambrecero.dto.DonanteDTO;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class TestConexion {
    private static DonacionService donacionService;
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("=== SISTEMA HAMBRE CERO - DEMOSTRACIÓN ===");

        try (Connection connection = DatabaseConfig.getConnection()) {
            System.out.println("✅ Conexión exitosa a la base de datos");

            donacionService = new DonacionService(connection);

            // Mostrar menú principal
            mostrarMenu();

        } catch (SQLException e) {
            System.err.println("❌ Error de conexión: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void mostrarMenu() {
        while (true) {
            System.out.println("\n=== MENÚ PRINCIPAL ===");
            System.out.println("1. Ver estadísticas generales");
            System.out.println("2. Listar todos los donantes");
            System.out.println("3. Ver donaciones por estado");
            System.out.println("4. Crear nueva donación");
            System.out.println("5. Actualizar estado de donación");
            System.out.println("6. 🔥 EJECUTAR CONSULTAS SQL AVANZADAS 🔥");
            System.out.println("7. Salir");
            System.out.print("Seleccione una opción: ");

            int opcion = scanner.nextInt();
            scanner.nextLine(); // Consumir el salto de línea

            switch (opcion) {
                case 1:
                    mostrarEstadisticas();
                    break;
                case 2:
                    listarDonantes();
                    break;
                case 3:
                    verDonacionesPorEstado();
                    break;
                case 4:
                    crearNuevaDonacion();
                    break;
                case 5:
                    actualizarEstadoDonacion();
                    break;
                case 6:
                    ejecutarConsultasSQL();
                    break;
                case 7:
                    System.out.println("👋 ¡Hasta luego!");
                    return;
                default:
                    System.out.println("❌ Opción inválida");
            }
        }
    }

    private static void ejecutarConsultasSQL() {
        System.out.println("\n🔥 EJECUTANDO CONSULTAS SQL AVANZADAS 🔥");
        System.out.println("========================================");

        // Aquí es donde llamamos las consultas SQL
        ConsultasSQL.ejecutarTodasLasConsultas();

        System.out.println("========================================");
        System.out.println("✅ Consultas SQL completadas");
        System.out.println("Presiona Enter para continuar...");
        scanner.nextLine();
    }

    private static void mostrarEstadisticas() {
        System.out.println("\n📊 ESTADÍSTICAS GENERALES");
        System.out.println("========================");

        Map<String, Object> estadisticas = donacionService.obtenerEstadisticas();

        System.out.println("Total de donaciones: " + estadisticas.get("totalDonaciones"));
        System.out.println("Total de calorías donadas: " + String.format("%.2f", (Double) estadisticas.get("totalCalorias")));
        System.out.println("Donaciones entregadas: " + estadisticas.get("donacionesEntregadas"));
        System.out.println("Donaciones pendientes: " + estadisticas.get("donacionesPendientes"));
        System.out.println("Porcentaje entregadas: " + String.format("%.2f%%", (Double) estadisticas.get("porcentajeEntregadas")));
    }

    private static void listarDonantes() {
        System.out.println("\n👥 LISTA DE DONANTES");
        System.out.println("==================");

        List<DonanteDTO> donantes = donacionService.obtenerTodosLosDonantes();

        for (DonanteDTO donante : donantes) {
            System.out.println("ID: " + donante.getIdDonante() +
                    " | Nombre: " + donante.getNombre() +
                    " | Email: " + donante.getEmail() +
                    " | Total donaciones: " + donante.getTotalDonaciones());
        }
    }

    private static void verDonacionesPorEstado() {
        System.out.print("Ingrese el estado (Pendiente/En proceso/Entregado): ");
        String estado = scanner.nextLine();

        System.out.println("\n📋 DONACIONES CON ESTADO: " + estado);
        System.out.println("===============================");

        List<DonacionDTO> donaciones = donacionService.obtenerDonacionesPorEstado(estado);

        if (donaciones.isEmpty()) {
            System.out.println("No se encontraron donaciones con ese estado.");
        } else {
            for (DonacionDTO donacion : donaciones) {
                System.out.println("ID: " + donacion.getIdDonacion() +
                        " | Fecha: " + donacion.getFecha() +
                        " | Donante: " + donacion.getNombreDonante() +
                        " | Receptor: " + donacion.getNombreReceptor() +
                        " | Zona: " + donacion.getZonaEntrega());
            }
        }
    }

    private static void crearNuevaDonacion() {
        System.out.println("\n➕ CREAR NUEVA DONACIÓN");
        System.out.println("=====================");

        System.out.print("ID del donante: ");
        int idDonante = scanner.nextInt();
        scanner.nextLine();

        System.out.print("ID del receptor: ");
        int idReceptor = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Observaciones: ");
        String observaciones = scanner.nextLine();

        try {
            DonacionDTO nuevaDonacion = donacionService.crearDonacion(idDonante, idReceptor, observaciones);
            System.out.println("✅ Donación creada exitosamente con ID: " + nuevaDonacion.getIdDonacion());
        } catch (Exception e) {
            System.out.println("❌ Error al crear donación: " + e.getMessage());
        }
    }

    private static void actualizarEstadoDonacion() {
        System.out.println("\n🔄 ACTUALIZAR ESTADO DE DONACIÓN");
        System.out.println("==============================");

        System.out.print("ID de la donación: ");
        int idDonacion = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Nuevo estado (Pendiente/En proceso/Entregado): ");
        String nuevoEstado = scanner.nextLine();

        System.out.print("Nuevas observaciones: ");
        String observaciones = scanner.nextLine();

        boolean actualizado = donacionService.actualizarEstadoDonacion(idDonacion, nuevoEstado, observaciones);

        if (actualizado) {
            System.out.println("✅ Estado actualizado correctamente");
        } else {
            System.out.println("❌ No se pudo actualizar el estado");
        }
    }
}
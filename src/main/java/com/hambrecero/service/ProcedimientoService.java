package com.hambrecero.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.Types;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class ProcedimientoService {
    
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcCall registrarDonacionCall;
    private final SimpleJdbcCall reporteDonacionesZonaCall;
    
    @Autowired
    public ProcedimientoService(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        
        // Configurar llamada al procedimiento de registrar donación
        this.registrarDonacionCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("sp_registrar_donacion_completa")
                .declareParameters(
                    new SqlParameter("p_id_donante", Types.INTEGER),
                    new SqlParameter("p_id_receptor", Types.INTEGER),
                    new SqlParameter("p_observaciones", Types.VARCHAR),
                    new SqlParameter("p_nombre_alimento", Types.VARCHAR),
                    new SqlParameter("p_cantidad", Types.DECIMAL),
                    new SqlParameter("p_unidad_medida", Types.VARCHAR),
                    new SqlParameter("p_calorias", Types.DECIMAL),
                    new SqlOutParameter("p_id_donacion", Types.INTEGER),
                    new SqlOutParameter("p_mensaje", Types.VARCHAR)
                );
        
        // Configurar llamada al procedimiento de reporte
        this.reporteDonacionesZonaCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("sp_reporte_donaciones_zona");
    }
    
    public Map<String, Object> registrarDonacionConProcedimiento(
            Integer idDonante, Integer idReceptor, String observaciones,
            String nombreAlimento, Double cantidad, String unidadMedida, Double calorias) {
        
        Map<String, Object> inParams = new HashMap<>();
        inParams.put("p_id_donante", idDonante);
        inParams.put("p_id_receptor", idReceptor);
        inParams.put("p_observaciones", observaciones);
        inParams.put("p_nombre_alimento", nombreAlimento);
        inParams.put("p_cantidad", cantidad);
        inParams.put("p_unidad_medida", unidadMedida);
        inParams.put("p_calorias", calorias);
        
        Map<String, Object> outParams = registrarDonacionCall.execute(inParams);
        
        Map<String, Object> resultado = new HashMap<>();
        resultado.put("idDonacion", outParams.get("p_id_donacion"));
        resultado.put("mensaje", outParams.get("p_mensaje"));
        resultado.put("exito", (Integer) outParams.get("p_id_donacion") > 0);
        
        return resultado;
    }
    
    public List<Map<String, Object>> generarReporteDonacionesPorZona(LocalDate fechaInicio, LocalDate fechaFin) {
        Map<String, Object> params = new HashMap<>();
        params.put("p_fecha_inicio", java.sql.Date.valueOf(fechaInicio));
        params.put("p_fecha_fin", java.sql.Date.valueOf(fechaFin));
        
        return jdbcTemplate.queryForList(
            "CALL sp_reporte_donaciones_zona(?, ?)",
            params.get("p_fecha_inicio"),
            params.get("p_fecha_fin")
        );
    }
    
    // Método adicional para ejecutar consultas directas si se necesitan
    public Map<String, Object> obtenerEstadisticasAvanzadas() {
        Map<String, Object> stats = new HashMap<>();
        
        // Total de donaciones por mes actual
        String sqlDonacionesMesActual = 
            "SELECT COUNT(*) as total FROM donacion WHERE MONTH(fecha) = MONTH(CURRENT_DATE()) AND YEAR(fecha) = YEAR(CURRENT_DATE())";
        Integer donacionesMesActual = jdbcTemplate.queryForObject(sqlDonacionesMesActual, Integer.class);
        stats.put("donacionesMesActual", donacionesMesActual);
        
        // Top 5 donantes por calorías
        String sqlTopDonantes = 
            "SELECT d.nombre, d.total_calorias_donadas FROM donante d ORDER BY d.total_calorias_donadas DESC LIMIT 5";
        List<Map<String, Object>> topDonantes = jdbcTemplate.queryForList(sqlTopDonantes);
        stats.put("topDonantes", topDonantes);
        
        // Zonas con más necesidad (menos donaciones)
        String sqlZonasNecesidad = 
            "SELECT ze.nombre_zona, ze.ciudad, COUNT(d.id_donacion) as total_donaciones " +
            "FROM zona_entrega ze " +
            "LEFT JOIN receptor r ON ze.id_zona = r.id_zona_entrega " +
            "LEFT JOIN donacion d ON r.id_receptor = d.id_receptor " +
            "GROUP BY ze.id_zona " +
            "ORDER BY total_donaciones ASC LIMIT 5";
        List<Map<String, Object>> zonasNecesidad = jdbcTemplate.queryForList(sqlZonasNecesidad);
        stats.put("zonasConMasNecesidad", zonasNecesidad);
        
        return stats;
    }
} 
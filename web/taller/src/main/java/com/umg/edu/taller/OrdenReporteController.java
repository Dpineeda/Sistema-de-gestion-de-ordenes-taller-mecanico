
package com.umg.edu.taller;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
public class OrdenReporteController {

    private final JdbcTemplate jdbc;

    public OrdenReporteController(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @GetMapping("/api/ordenes")
    public List<Map<String, Object>> obtenerOrdenes() {

        String sql = """
            SELECT
                o.id_orden AS numero,
                v.placa,
                c.nombre AS cliente,
                m.nombre AS mecanico,
                o.descripcion_problema AS problema,
                o.estado
            FROM orden o
            INNER JOIN vehiculo v
                ON o.id_vehiculo = v.id_vehiculo
            INNER JOIN cliente c
                ON v.id_cliente = c.id_cliente
            INNER JOIN mecanico m
                ON o.id_mecanico = m.id_mecanico
            ORDER BY o.id_orden
            """;

        return jdbc.queryForList(sql);
    }
}

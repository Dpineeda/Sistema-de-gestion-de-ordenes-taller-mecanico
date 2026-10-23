package com.umg.edu.taller.dao;

import com.umg.edu.taller.config.conexion_db;
import com.umg.edu.taller.modelo.comprobante;
import com.umg.edu.taller.modelo.orden;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class comprobanteDao implements CrudDao<comprobante, Integer> {

    @Override
    public boolean insertar(comprobante objeto) {
        if (objeto == null || objeto.getorden() == null || objeto.getFecha_emision() == null) {
            return false;
        }
        String sql = "INSERT INTO comprobante (id_comprobante, id_orden, fecha_emision, total_repuesto, total) "
                + "SELECT ?, o.id_orden, ?, "
                + "COALESCE((SELECT SUM(orj.cantidad * orj.precio_unidad) FROM orden_repuesto orj "
                + "WHERE orj.id_orden = o.id_orden), 0), o.precio_total "
                + "FROM orden o WHERE o.id_orden = ? AND o.estado = ? "
                + "AND NOT EXISTS (SELECT 1 FROM comprobante c WHERE c.id_orden = o.id_orden)";

        try (Connection con = conexion_db.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, objeto.getId_comprobante());
            ps.setTimestamp(2, Timestamp.valueOf(objeto.getFecha_emision()));
            ps.setInt(3, objeto.getorden().getid_orden());
            ps.setString(4, com.umg.edu.taller.modelo.orden.FINALIZADA);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al insertar el comprobante");
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean actualizar(comprobante objeto) {
        System.err.println("Los comprobantes emitidos son inmutables");
        return false;
    }

    @Override
    public boolean eliminar(Integer id) {
        System.err.println("Los comprobantes emitidos no se pueden eliminar");
        return false;
    }

    @Override
    public comprobante buscarporid(Integer id) {
        String sql = "SELECT id_comprobante, id_orden, fecha_emision, total_repuesto, total FROM comprobante WHERE id_comprobante = ?";

        try (Connection con = conexion_db.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapearcomprobante(rs) : null;
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar el comprobante por ID");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<comprobante> listartodos() {
        String sql = "SELECT id_comprobante, id_orden, fecha_emision, total_repuesto, total FROM comprobante";
        List<comprobante> comprobantes = new ArrayList<>();

        try (Connection con = conexion_db.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                comprobantes.add(mapearcomprobante(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al listar los comprobantes");
            e.printStackTrace();
        }
        return comprobantes;
    }

    public BigDecimal ingresosEntre(LocalDateTime inicio, LocalDateTime fin) throws SQLException {
        if (inicio == null || fin == null || !inicio.isBefore(fin)) {
            throw new IllegalArgumentException("El inicio debe ser anterior al fin del rango");
        }
        String sql = "SELECT COALESCE(SUM(total), 0) AS ingresos FROM comprobante "
                + "WHERE fecha_emision >= ? AND fecha_emision < ?";
        try (Connection con = conexion_db.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setTimestamp(1, Timestamp.valueOf(inicio));
            ps.setTimestamp(2, Timestamp.valueOf(fin));
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getBigDecimal("ingresos");
            }
        }
    }

    private comprobante mapearcomprobante(ResultSet rs) throws SQLException {
        orden ordenComprobante = new orden(
                rs.getInt("id_orden"),
                null,
                null,
                null,
                null
        );
        Timestamp fechaEmision = rs.getTimestamp("fecha_emision");

        return new comprobante(
                rs.getInt("id_comprobante"),
                fechaEmision == null ? null : fechaEmision.toLocalDateTime(),
                ordenComprobante,
                rs.getBigDecimal("total_repuesto"),
                rs.getBigDecimal("total")
        );
}
}

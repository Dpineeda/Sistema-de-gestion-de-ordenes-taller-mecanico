package com.umg.edu.taller.dao;

import com.umg.edu.taller.config.conexion_db;
import com.umg.edu.taller.modelo.comprobante;
import com.umg.edu.taller.modelo.orden;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class comprobanteDao implements CrudDao<comprobante> {

    @Override
    public boolean insertar(comprobante objeto) {
        String sql = "INSERT INTO comprobante (id_comprobante, id_orden, fecha_emision, total_repuesto, total) VALUES (?, ?, ?, ?, ?)";

        try (Connection con = conexion_db.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, objeto.getId_comprobante());
            ps.setInt(2, objeto.getorden().getid_orden());
            ps.setTimestamp(3, Timestamp.valueOf(objeto.getFecha_emision()));
            ps.setBigDecimal(4, objeto.getTotal_repuesto());
            ps.setBigDecimal(5, objeto.getTotal());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al insertar el comprobante");
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean actualizar(comprobante objeto) {
        String sql = "UPDATE comprobante SET id_orden = ?, fecha_emision = ?, "
                + "total_repuesto = ?, total = ? WHERE id_comprobante = ?";

        try (Connection con = conexion_db.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, objeto.getorden().getid_orden());
            ps.setTimestamp(2, Timestamp.valueOf(objeto.getFecha_emision()));
            ps.setBigDecimal(3, objeto.getTotal_repuesto());
            ps.setBigDecimal(4, objeto.getTotal());
            ps.setInt(5, objeto.getId_comprobante());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar el comprobante");
            e.printStackTrace();
            return false;
     }
    }

    @Override
    public boolean eliminar(int id) {
        String sql = "DELETE FROM comprobante WHERE id_comprobante = ?";

        try (Connection con = conexion_db.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar el comprobante");
            e.printStackTrace();
            return false;
    }
    }

    @Override
    public comprobante buscarporid(int id) {
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

package com.umg.edu.taller.dao;

import com.umg.edu.taller.config.conexion_db;
import com.umg.edu.taller.modelo.orden;
import com.umg.edu.taller.modelo.orden_repuesto;
import com.umg.edu.taller.modelo.repuesto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class orden_repuestoDao implements CrudDao<orden_repuesto> {

    @Override
    public boolean insertar(orden_repuesto objeto) {
        String sql = "INSERT INTO orden_repuesto(id_orden_repuesto, id_orden, id_repuesto, cantidad, precio_unidad) VALUES (?, ?, ?, ?, ?)";

        try (Connection con = conexion_db.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, objeto.getId_orden_repuesto());
            ps.setInt(2, objeto.getOrden().getid_orden());
            ps.setInt(3, objeto.getRepuesto().getId_repuesto());
            ps.setInt(4, objeto.getCantidad());
            ps.setBigDecimal(5, objeto.getPrecio_unidad());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al insertar el repuesto de la orden");
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean actualizar(orden_repuesto objeto) {
        String sql = "UPDATE orden_repuesto SET id_orden = ?, id_repuesto = ?, cantidad = ?, precio_unidad = ? WHERE id_orden_repuesto = ?";

        try (Connection con = conexion_db.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, objeto.getOrden().getid_orden());
            ps.setInt(2, objeto.getRepuesto().getId_repuesto());
            ps.setInt(3, objeto.getCantidad());
            ps.setBigDecimal(4, objeto.getPrecio_unidad());
            ps.setInt(5, objeto.getId_orden_repuesto());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar el repuesto de la orden");
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean eliminar(int id) {
        String sql = "DELETE FROM orden_repuesto WHERE id_orden_repuesto = ?";

        try (Connection con = conexion_db.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar el repuesto de la orden");
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public orden_repuesto buscarporid(int id) {
        String sql = "SELECT id_orden_repuesto, id_orden, id_repuesto, cantidad, precio_unidad FROM orden_repuesto WHERE id_orden_repuesto = ?";

        try (Connection con = conexion_db.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapearOrdenRepuesto(rs) : null;
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar el repuesto de la orden por ID");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<orden_repuesto> listartodos() {
        String sql = "SELECT id_orden_repuesto, id_orden, id_repuesto, cantidad, precio_unidad FROM orden_repuesto";
        List<orden_repuesto> ordenesRepuestos = new ArrayList<>();

        try (Connection con = conexion_db.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                ordenesRepuestos.add(mapearOrdenRepuesto(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al listar los repuestos de las órdenes");
            e.printStackTrace();
        }
        return ordenesRepuestos;
    }

    private orden_repuesto mapearOrdenRepuesto(ResultSet rs) throws SQLException {
        orden ordenRelacionada = new orden(
                rs.getInt("id_orden"),
                null,
                null,
                null,
                null
        );
        repuesto repuestoRelacionado = new repuesto(
                rs.getInt("id_repuesto"),
                null,
                null,
                null,
                0,
                0
        );

        return new orden_repuesto(
                rs.getInt("id_orden_repuesto"),
                ordenRelacionada,
                repuestoRelacionado,
                rs.getInt("cantidad"),
                rs.getBigDecimal("precio_unidad")
        );
    }
}

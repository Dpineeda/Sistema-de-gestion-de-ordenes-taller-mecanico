package com.umg.edu.taller.dao;

import com.umg.edu.taller.config.conexion_db;
import com.umg.edu.taller.modelo.orden;
import com.umg.edu.taller.modelo.orden_servicio;
import com.umg.edu.taller.modelo.servicio;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class oden_servicioDao implements CrudDao<orden_servicio> {

    @Override
    public boolean insertar(orden_servicio objeto) {
        String sql = "INSERT INTO orden_servicio(id_orden_servicio, id_orden, id_servicio, costo) VALUES (?, ?, ?, ?)";

        try (Connection con = conexion_db.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, objeto.getId_orden_servicio());
            ps.setInt(2, objeto.getOrden().getid_orden());
            ps.setInt(3, objeto.getServicio().getid_servicio());
            ps.setBigDecimal(4, objeto.getCosto());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al insertar el servicio de la orden");
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean actualizar(orden_servicio objeto) {
        String sql = "UPDATE orden_servicio SET id_orden = ?, id_servicio = ?, costo = ? WHERE id_orden_servicio = ?";

        try (Connection con = conexion_db.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, objeto.getOrden().getid_orden());
            ps.setInt(2, objeto.getServicio().getid_servicio());
            ps.setBigDecimal(3, objeto.getCosto());
            ps.setInt(4, objeto.getId_orden_servicio());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar el servicio de la orden");
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean eliminar(int id) {
        String sql = "DELETE FROM orden_servicio WHERE id_orden_servicio = ?";

        try (Connection con = conexion_db.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar el servicio de la orden");
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public orden_servicio buscarporid(int id) {
        String sql = "SELECT id_orden_servicio, id_orden, id_servicio, costo FROM orden_servicio WHERE id_orden_servicio = ?";

        try (Connection con = conexion_db.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapearOrdenServicio(rs) : null;
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar el servicio de la orden por ID");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<orden_servicio> listartodos() {
        String sql = "SELECT id_orden_servicio, id_orden, id_servicio, costo FROM orden_servicio";
        List<orden_servicio> ordenesServicios = new ArrayList<>();

        try (Connection con = conexion_db.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                ordenesServicios.add(mapearOrdenServicio(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al listar los servicios de las órdenes");
            e.printStackTrace();
        }
        return ordenesServicios;
    }

    private orden_servicio mapearOrdenServicio(ResultSet rs) throws SQLException {
        orden ordenRelacionada = new orden(
                rs.getInt("id_orden"),
                null,
                null,
                null,
                null
        );
        servicio servicioRelacionado = new servicio(
                rs.getInt("id_servicio"),
                null,
                null
        );

        return new orden_servicio(
                rs.getInt("id_orden_servicio"),
                ordenRelacionada,
                servicioRelacionado,
                rs.getBigDecimal("costo")
        );
    }
}

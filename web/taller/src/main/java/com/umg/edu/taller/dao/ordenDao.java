package com.umg.edu.taller.dao;

import com.umg.edu.taller.config.conexion_db;
import com.umg.edu.taller.modelo.mecanico;
import com.umg.edu.taller.modelo.orden;
import com.umg.edu.taller.modelo.vehiculo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ordenDao implements CrudDao<orden> {

    @Override
    public boolean insertar(orden objeto) {
        String sql = "INSERT INTO orden(id_orden, id_vehiculo, id_mecanico, descripcion_problema, precio_total) VALUES (?, ?, ?, ?, ?)";

        try (Connection con = conexion_db.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, objeto.getid_orden());
            ps.setInt(2, objeto.getvehiculo().getId_vehiculo());
            ps.setInt(3, objeto.getmecanico().getid_mecanico());
            ps.setString(4, objeto.getdescripcion_problema());
            ps.setBigDecimal(5, objeto.precio_total());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
    }
    }

    @Override
    public boolean actualizar(orden objeto) {
        String sql = "UPDATE orden SET id_vehiculo = ?, id_mecanico = ?, descripcion_problema = ?, precio_total = ? WHERE id_orden = ?";

        try (Connection con = conexion_db.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, objeto.getvehiculo().getId_vehiculo());
            ps.setInt(2, objeto.getmecanico().getid_mecanico());
            ps.setString(3, objeto.getdescripcion_problema());
            ps.setBigDecimal(4, objeto.precio_total());
            ps.setInt(5, objeto.getid_orden());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar la orden");
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean eliminar(int id) {
        String sql = "DELETE FROM orden WHERE id_orden = ?";

        try (Connection con = conexion_db.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar la orden");
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public orden buscarporid(int id) {
        String sql = "SELECT id_orden, id_vehiculo, id_mecanico, descripcion_problema, precio_total FROM orden WHERE id_orden = ?";

        try (Connection con = conexion_db.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapearOrden(rs) : null;
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar la orden por ID");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<orden> listartodos() {
        String sql = "SELECT id_orden, id_vehiculo, id_mecanico, descripcion_problema, precio_total FROM orden";
        List<orden> ordenes = new ArrayList<>();

        try (Connection con = conexion_db.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                ordenes.add(mapearOrden(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al listar las órdenes");
            e.printStackTrace();
        }
        return ordenes;
    }

    private orden mapearOrden(ResultSet rs) throws SQLException {
        vehiculo vehiculoOrden = new vehiculo(
                rs.getInt("id_vehiculo"),
                null,
                null,
                null,
                null,
                null,
                null
        );
        mecanico mecanicoOrden = new mecanico(
                rs.getInt("id_mecanico"),
                null,
                null
        );

        return new orden(
                rs.getInt("id_orden"),
                vehiculoOrden,
                mecanicoOrden,
                rs.getString("descripcion_problema"),
                rs.getBigDecimal("precio_total")
        );
    }
}

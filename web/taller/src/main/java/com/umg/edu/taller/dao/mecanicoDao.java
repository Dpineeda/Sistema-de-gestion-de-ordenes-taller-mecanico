package com.umg.edu.taller.dao;

import com.umg.edu.taller.config.conexion_db;
import com.umg.edu.taller.modelo.mecanico;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class mecanicoDao implements CrudDao<mecanico, Integer> {

    @Override
    public boolean insertar(mecanico objeto) {
        String sql = "INSERT INTO mecanico(id_mecanico, nombre, telefono) VALUES (?, ?, ?)";
        try (Connection con = conexion_db.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, objeto.getid_mecanico());
            ps.setString(2, objeto.getnombre());
            ps.setString(3, objeto.gettelefono());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al insertar el mecánico");
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean actualizar(mecanico objeto) {
        String sql = "UPDATE mecanico SET nombre = ?, telefono = ? WHERE id_mecanico = ?";

        try (Connection con = conexion_db.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, objeto.getnombre());
            ps.setString(2, objeto.gettelefono());
            ps.setInt(3, objeto.getid_mecanico());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar el mecánico");
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean eliminar(Integer id) {
        String sql = "DELETE FROM mecanico WHERE id_mecanico = ? "
                + "AND NOT EXISTS (SELECT 1 FROM orden WHERE orden.id_mecanico = mecanico.id_mecanico)";

        try (Connection con = conexion_db.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar el mecánico");
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public mecanico buscarporid(Integer id) {
        String sql = "SELECT id_mecanico, nombre, telefono FROM mecanico WHERE id_mecanico = ?";

        try (Connection con = conexion_db.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapearMecanico(rs) : null;
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar el mecánico por ID");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<mecanico> listartodos() {
        String sql = "SELECT id_mecanico, nombre, telefono FROM mecanico";
        List<mecanico> mecanicos = new ArrayList<>();

        try (Connection con = conexion_db.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                mecanicos.add(mapearMecanico(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al listar los mecánicos");
            e.printStackTrace();
        }
        return mecanicos;
    }

    private mecanico mapearMecanico(ResultSet rs) throws SQLException {
        return new mecanico(
                rs.getInt("id_mecanico"),
                rs.getString("nombre"),
                rs.getString("telefono")
        );
    }
}

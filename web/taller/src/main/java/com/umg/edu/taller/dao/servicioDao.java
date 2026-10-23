package com.umg.edu.taller.dao;

import com.umg.edu.taller.config.conexion_db;
import com.umg.edu.taller.modelo.servicio;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class servicioDao implements CrudDao<servicio, Integer> {

    @Override
    public boolean insertar(servicio objeto) {
        if (objeto == null || objeto.getdescripcion() == null || objeto.getdescripcion().isBlank()
                || objeto.getcosto_manodeobra() == null || objeto.getcosto_manodeobra().signum() < 0) {
            return false;
        }
        String sql = "INSERT INTO servicio(id_servicio, descripcion, costo_manodeobra) VALUES (?, ?, ?)";

        try (Connection con = conexion_db.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, objeto.getid_servicio());
            ps.setString(2, objeto.getdescripcion());
            ps.setBigDecimal(3, objeto.getcosto_manodeobra());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al insertar el servicio");
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean actualizar(servicio objeto) {
        if (objeto == null || objeto.getdescripcion() == null || objeto.getdescripcion().isBlank()
                || objeto.getcosto_manodeobra() == null || objeto.getcosto_manodeobra().signum() < 0) {
            return false;
        }
        String sql = "UPDATE servicio SET descripcion = ?, costo_manodeobra = ? WHERE id_servicio = ?";

        try (Connection con = conexion_db.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, objeto.getdescripcion());
            ps.setBigDecimal(2, objeto.getcosto_manodeobra());
            ps.setInt(3, objeto.getid_servicio());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar el servicio");
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean eliminar(Integer id) {
        String sql = "DELETE FROM servicio WHERE id_servicio = ?";

        try (Connection con = conexion_db.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar el servicio");
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public servicio buscarporid(Integer id) {
        String sql = "SELECT id_servicio, descripcion, costo_manodeobra FROM servicio WHERE id_servicio = ?";

        try (Connection con = conexion_db.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapearServicio(rs) : null;
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar el servicio por ID");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<servicio> listartodos() {
        String sql = "SELECT id_servicio, descripcion, costo_manodeobra FROM servicio";
        List<servicio> servicios = new ArrayList<>();

        try (Connection con = conexion_db.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                servicios.add(mapearServicio(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al listar los servicios");
            e.printStackTrace();
        }
        return servicios;
    }

    private servicio mapearServicio(ResultSet rs) throws SQLException {
        return new servicio(
                rs.getInt("id_servicio"),
                rs.getString("descripcion"),
                rs.getBigDecimal("costo_manodeobra")
        );
    }
}

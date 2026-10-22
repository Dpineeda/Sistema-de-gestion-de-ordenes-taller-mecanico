package com.umg.edu.taller.dao;

import com.umg.edu.taller.config.conexion_db;
import com.umg.edu.taller.modelo.repuesto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class repuestoDao implements CrudDao<repuesto> {

    @Override
    public boolean insertar(repuesto objeto) {
        String sql = "INSERT INTO repuesto(id_repuesto, nombre, descripcion, precio, existencia, minimo_en_stock) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = conexion_db.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, objeto.getId_repuesto());
            ps.setString(2, objeto.getNombre());
            ps.setString(3, objeto.getDescripcion());
            ps.setBigDecimal(4, objeto.getPrecio());
            ps.setInt(5, objeto.getExistencia());
            ps.setInt(6, objeto.getMinimo_en_stock());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al insertar el repuesto");
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean actualizar(repuesto objeto) {
        String sql = "UPDATE repuesto SET nombre = ?, descripcion = ?, precio = ?, existencia = ?, minimo_en_stock = ? WHERE id_repuesto = ?";

        try (Connection con = conexion_db.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, objeto.getNombre());
            ps.setString(2, objeto.getDescripcion());
            ps.setBigDecimal(3, objeto.getPrecio());
            ps.setInt(4, objeto.getExistencia());
            ps.setInt(5, objeto.getMinimo_en_stock());
            ps.setInt(6, objeto.getId_repuesto());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar el repuesto");
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean eliminar(int id) {
        String sql = "DELETE FROM repuesto WHERE id_repuesto = ?";

        try (Connection con = conexion_db.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar el repuesto");
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public repuesto buscarporid(int id) {
        String sql = "SELECT id_repuesto, nombre, descripcion, precio, existencia, minimo_en_stock FROM repuesto WHERE id_repuesto = ?";

        try (Connection con = conexion_db.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapearRepuesto(rs) : null;
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar el repuesto por ID");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<repuesto> listartodos() {
        String sql = "SELECT id_repuesto, nombre, descripcion, precio, existencia, minimo_en_stock FROM repuesto";
        List<repuesto> repuestos = new ArrayList<>();

        try (Connection con = conexion_db.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                repuestos.add(mapearRepuesto(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al listar los repuestos");
            e.printStackTrace();
        }
        return repuestos;
    }

    private repuesto mapearRepuesto(ResultSet rs) throws SQLException {
        return new repuesto(
                rs.getInt("id_repuesto"),
                rs.getString("nombre"),
                rs.getString("descripcion"),
                rs.getBigDecimal("precio"),
                rs.getInt("existencia"),
                rs.getInt("minimo_en_stock")
        );
    }
}

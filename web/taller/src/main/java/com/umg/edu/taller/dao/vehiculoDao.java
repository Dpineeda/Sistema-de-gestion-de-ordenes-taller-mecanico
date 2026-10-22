package com.umg.edu.taller.dao;

import com.umg.edu.taller.config.conexion_db;
import com.umg.edu.taller.modelo.cliente;
import com.umg.edu.taller.modelo.vehiculo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class vehiculoDao implements CrudDao<vehiculo> {

    @Override
    public boolean insertar(vehiculo objeto) {
        String sql = "INSERT INTO vehiculo(id_vehiculo, placa, marca, modelo, anio, color, id_cliente) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = conexion_db.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, objeto.getId_vehiculo());
            ps.setString(2, objeto.getPlaca());
            ps.setString(3, objeto.getMarca());
            ps.setString(4, objeto.getModelo());
            if (objeto.getAnio() == null) {
                ps.setNull(5, Types.SMALLINT);
            } else {
                ps.setShort(5, objeto.getAnio());
            }
            ps.setString(6, objeto.getColor());
            ps.setString(7, objeto.getCliente().getid_cliente());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al insertar el vehículo");
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean actualizar(vehiculo objeto) {
        String sql = "UPDATE vehiculo SET placa = ?, marca = ?, modelo = ?, anio = ?, color = ?, id_cliente = ? WHERE id_vehiculo = ?";

        try (Connection con = conexion_db.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, objeto.getPlaca());
            ps.setString(2, objeto.getMarca());
            ps.setString(3, objeto.getModelo());
            if (objeto.getAnio() == null) {
                ps.setNull(4, Types.SMALLINT);
            } else {
                ps.setShort(4, objeto.getAnio());
            }
            ps.setString(5, objeto.getColor());
            ps.setString(6, objeto.getCliente().getid_cliente());
            ps.setInt(7, objeto.getId_vehiculo());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar el vehículo");
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean eliminar(int id) {
        String sql = "DELETE FROM vehiculo WHERE id_vehiculo = ?";

        try (Connection con = conexion_db.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar el vehículo");
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public vehiculo buscarporid(int id) {
        String sql = "SELECT id_vehiculo, placa, marca, modelo, anio, color, id_cliente FROM vehiculo WHERE id_vehiculo = ?";

        try (Connection con = conexion_db.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapearVehiculo(rs) : null;
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar el vehículo por ID");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<vehiculo> listartodos() {
        String sql = "SELECT id_vehiculo, placa, marca, modelo, anio, color, id_cliente FROM vehiculo";
        List<vehiculo> vehiculos = new ArrayList<>();

        try (Connection con = conexion_db.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                vehiculos.add(mapearVehiculo(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al listar los vehículos");
            e.printStackTrace();
        }
        return vehiculos;
    }

    private vehiculo mapearVehiculo(ResultSet rs) throws SQLException {
        Short anio = rs.getShort("anio");
        if (rs.wasNull()) {
            anio = null;
        }

        cliente clienteRelacionado = new cliente(
                rs.getString("id_cliente"),
                null,
                null,
                null
        );

        return new vehiculo(
                rs.getInt("id_vehiculo"),
                rs.getString("placa"),
                rs.getString("marca"),
                rs.getString("modelo"),
                anio,
                rs.getString("color"),
                clienteRelacionado
        );
    }
}

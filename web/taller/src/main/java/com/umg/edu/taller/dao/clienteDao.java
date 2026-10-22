
package com.umg.edu.taller.dao;
import com.umg.edu.taller.config.conexion_db;
import com.umg.edu.taller.modelo.cliente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class clienteDao implements CrudDao<cliente> {

    @Override
    public boolean insertar(cliente cliente) {
        String sql = "INSERT INTO cliente(id_cliente, nombre, telefono, correo) VALUES (?, ?, ?, ?)";

        try (Connection con = conexion_db.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, cliente.getid_cliente());
            ps.setString(2, cliente.getnombre());
            ps.setString(3, cliente.gettelefono());
            ps.setString(4, cliente.getcorreo());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean actualizar(cliente objeto) {
        String sql = "UPDATE cliente SET nombre = ?, telefono = ?, correo = ? WHERE id_cliente = ?";

        try (Connection con = conexion_db.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, objeto.getnombre());
            ps.setString(2, objeto.gettelefono());
            ps.setString(3, objeto.getcorreo());
            ps.setString(4, objeto.getid_cliente());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar el cliente");
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean eliminar(int id) {
        String sql = "DELETE FROM cliente WHERE id_cliente = ?";

        try (Connection con = conexion_db.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar el cliente");
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public cliente buscarporid(int id) {
        String sql = "SELECT id_cliente, nombre, telefono, correo FROM cliente WHERE id_cliente = ?";

        try (Connection con = conexion_db.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapearcliente(rs) : null;
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar el cliente por ID");
            System.err.println();
        }
        return null;
    }

    @Override
    public List<cliente> listartodos() {
        String sql = "SELECT id_cliente, nombre, telefono, correo FROM cliente";
        List<cliente> clientes = new ArrayList<>();

        try (Connection con = conexion_db.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                clientes.add(mapearcliente(rs));
            }
           
        } catch (SQLException e) {
            System.err.println("Error al listar los clientes");
            e.printStackTrace();
        }
        return clientes;
    }

    private cliente mapearcliente(ResultSet rs) throws SQLException {
        return new cliente(
                rs.getString("id_cliente"),
                rs.getString("nombre"),
                rs.getString("telefono"),
                rs.getString("correo")
        );
    }
}

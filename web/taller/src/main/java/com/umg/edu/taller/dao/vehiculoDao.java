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

public class vehiculoDao implements CrudDao<vehiculo, Integer> {

    @Override
    public boolean insertar(vehiculo objeto) {
        if (objeto == null || objeto.getCliente() == null || objeto.getPlaca() == null
                || objeto.getPlaca().isBlank()) {
            return false;
        }
        String sql = "INSERT INTO vehiculo(id_vehiculo, placa, marca, modelo, anio, color, id_cliente) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = conexion_db.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            if (objeto.getCliente() == null
                    || objeto.getCliente().getid_cliente() == null
                    || placaExiste(con, objeto.getPlaca(), null)
                    || !clienteExiste(con, objeto.getCliente().getid_cliente())) {
                return false;
            }
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
        if (objeto == null || objeto.getCliente() == null || objeto.getPlaca() == null
                || objeto.getPlaca().isBlank()) {
            return false;
        }
        String sql = "UPDATE vehiculo SET placa = ?, marca = ?, modelo = ?, anio = ?, color = ?, id_cliente = ? WHERE id_vehiculo = ?";

        try (Connection con = conexion_db.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            if (objeto.getCliente() == null
                    || objeto.getCliente().getid_cliente() == null
                    || placaExiste(con, objeto.getPlaca(), objeto.getId_vehiculo())
                    || !clienteExiste(con, objeto.getCliente().getid_cliente())) {
                return false;
            }
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
    public boolean eliminar(Integer id) {
        String sql = "DELETE FROM vehiculo WHERE id_vehiculo = ? "
                + "AND NOT EXISTS (SELECT 1 FROM orden WHERE orden.id_vehiculo = vehiculo.id_vehiculo)";

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
    public vehiculo buscarporid(Integer id) {
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

    public List<com.umg.edu.taller.modelo.orden> listarHistorialOrdenes(int idVehiculo) {
        return new ordenDao().listarPorVehiculo(idVehiculo);
    }

    public List<com.umg.edu.taller.modelo.orden> listarHistorialOrdenes(String placa) {
        if (placa == null || placa.isBlank()) {
            throw new IllegalArgumentException("La placa es obligatoria");
        }
        return new ordenDao().listarPorPlaca(placa);
    }

    private boolean placaExiste(Connection con, String placa, Integer idExcluido) throws SQLException {
        String sql = idExcluido == null
                ? "SELECT 1 FROM vehiculo WHERE placa = ? LIMIT 1"
                : "SELECT 1 FROM vehiculo WHERE placa = ? AND id_vehiculo <> ? LIMIT 1";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, placa);
            if (idExcluido != null) {
                ps.setInt(2, idExcluido);
            }
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    private boolean clienteExiste(Connection con, String idCliente) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement("SELECT 1 FROM cliente WHERE id_cliente = ?")) {
            ps.setString(1, idCliente);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
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

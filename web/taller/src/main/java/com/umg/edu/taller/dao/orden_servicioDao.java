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

public class orden_servicioDao implements CrudDao<orden_servicio, Integer> {

    @Override
    public boolean insertar(orden_servicio objeto) {
        if (objeto == null || objeto.getOrden() == null || objeto.getServicio() == null
                || objeto.getCosto() == null || objeto.getCosto().signum() < 0) {
            return false;
        }
        String sql = "INSERT INTO orden_servicio(id_orden_servicio, id_orden, id_servicio, costo) VALUES (?, ?, ?, ?)";

        try (Connection con = conexion_db.conectar()) {
            con.setAutoCommit(false);
            try {
                if (!ordenEnProceso(con, objeto.getOrden().getid_orden())) {
                    con.rollback();
                    return false;
                }
                try (PreparedStatement ps = con.prepareStatement(sql)) {
                    ps.setInt(1, objeto.getId_orden_servicio());
                    ps.setInt(2, objeto.getOrden().getid_orden());
                    ps.setInt(3, objeto.getServicio().getid_servicio());
                    ps.setBigDecimal(4, objeto.getCosto());
                    boolean insertado = ps.executeUpdate() > 0;
                    con.commit();
                    return insertado;
                }
            } catch (SQLException e) {
                con.rollback();
                throw e;
            } finally {
                con.setAutoCommit(true);
            }
        } catch (SQLException e) {
            System.err.println("Error al asignar el servicio a la orden");
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean actualizar(orden_servicio objeto) {
        if (objeto == null || objeto.getOrden() == null || objeto.getServicio() == null
                || objeto.getCosto() == null || objeto.getCosto().signum() < 0) {
            return false;
        }
        String sql = "UPDATE orden_servicio SET id_servicio = ?, costo = ? WHERE id_orden_servicio = ? AND id_orden = ?";

        try (Connection con = conexion_db.conectar()) {
            con.setAutoCommit(false);
            try {
                if (!ordenEnProceso(con, objeto.getOrden().getid_orden())) {
                    con.rollback();
                    return false;
                }
                try (PreparedStatement ps = con.prepareStatement(sql)) {
                    ps.setInt(1, objeto.getServicio().getid_servicio());
                    ps.setBigDecimal(2, objeto.getCosto());
                    ps.setInt(3, objeto.getId_orden_servicio());
                    ps.setInt(4, objeto.getOrden().getid_orden());
                    boolean actualizado = ps.executeUpdate() > 0;
                    con.commit();
                    return actualizado;
                }
            } catch (SQLException e) {
                con.rollback();
                throw e;
            } finally {
                con.setAutoCommit(true);
            }
        } catch (SQLException e) {
            System.err.println("Error al actualizar el servicio de la orden");
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean eliminar(Integer id) {
        String selectSql = "SELECT id_orden FROM orden_servicio WHERE id_orden_servicio = ?";
        String deleteSql = "DELETE FROM orden_servicio WHERE id_orden_servicio = ?";

        try (Connection con = conexion_db.conectar()) {
            con.setAutoCommit(false);
            try {
                Integer idOrden = null;
                try (PreparedStatement ps = con.prepareStatement(selectSql)) {
                    ps.setInt(1, id);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            idOrden = rs.getInt("id_orden");
                        }
                    }
                }
                if (idOrden == null || !ordenEnProceso(con, idOrden)) {
                    con.rollback();
                    return false;
                }
                try (PreparedStatement ps = con.prepareStatement(deleteSql)) {
                    ps.setInt(1, id);
                    boolean eliminado = ps.executeUpdate() > 0;
                    con.commit();
                    return eliminado;
                }
            } catch (SQLException e) {
                con.rollback();
                throw e;
            } finally {
                con.setAutoCommit(true);
            }
        } catch (SQLException e) {
            System.err.println("Error al eliminar el servicio asignado");
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public orden_servicio buscarporid(Integer id) {
        String sql = "SELECT id_orden_servicio, id_orden, id_servicio, costo FROM orden_servicio WHERE id_orden_servicio = ?";

        try (Connection con = conexion_db.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapearOrdenServicio(rs) : null;
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar el servicio asignado por ID");
            e.printStackTrace();
            return null;
        }
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
            System.err.println("Error al listar los servicios asignados");
            e.printStackTrace();
        }
        return ordenesServicios;
    }

    private boolean ordenEnProceso(Connection con, int idOrden) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement(
                "SELECT estado FROM orden WHERE id_orden = ? FOR UPDATE")) {
            ps.setInt(1, idOrden);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && orden.EN_PROCESO.equals(rs.getString("estado"));
            }
        }
    }

    private orden_servicio mapearOrdenServicio(ResultSet rs) throws SQLException {
        orden ordenRelacionada = new orden(rs.getInt("id_orden"), null, null, null, null);
        servicio servicioRelacionado = new servicio(rs.getInt("id_servicio"), null, null);
        return new orden_servicio(
                rs.getInt("id_orden_servicio"),
                ordenRelacionada,
                servicioRelacionado,
                rs.getBigDecimal("costo")
        );
    }
}

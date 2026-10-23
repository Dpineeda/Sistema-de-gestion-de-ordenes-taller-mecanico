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

public class orden_repuestoDao implements CrudDao<orden_repuesto, Integer> {

    @Override
    public boolean insertar(orden_repuesto objeto) {
        if (!datosValidos(objeto)) {
            return false;
        }
        String sql = "INSERT INTO orden_repuesto(id_orden_repuesto, id_orden, id_repuesto, cantidad, precio_unidad) "
                + "VALUES (?, ?, ?, ?, ?)";

        try (Connection con = conexion_db.conectar()) {
            con.setAutoCommit(false);
            try {
                if (!ordenEnProceso(con, objeto.getOrden().getid_orden())
                        || !bloquearRepuestos(con, objeto.getRepuesto().getId_repuesto(), null)
                        || !descontarExistencia(con, objeto.getRepuesto().getId_repuesto(), objeto.getCantidad())) {
                    con.rollback();
                    return false;
                }
                try (PreparedStatement ps = con.prepareStatement(sql)) {
                    ps.setInt(1, objeto.getId_orden_repuesto());
                    ps.setInt(2, objeto.getOrden().getid_orden());
                    ps.setInt(3, objeto.getRepuesto().getId_repuesto());
                    ps.setInt(4, objeto.getCantidad());
                    ps.setBigDecimal(5, objeto.getPrecio_unidad());
                    boolean insertado = ps.executeUpdate() > 0;
                    if (!insertado) {
                        con.rollback();
                        return false;
                    }
                }
                con.commit();
                return true;
            } catch (SQLException e) {
                con.rollback();
                throw e;
            } finally {
                con.setAutoCommit(true);
            }
        } catch (SQLException e) {
            System.err.println("Error al asignar el repuesto y descontar el inventario");
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean actualizar(orden_repuesto objeto) {
        if (!datosValidos(objeto) || objeto.getOrden() == null || objeto.getRepuesto() == null) {
            return false;
        }
        String selectSql = "SELECT id_orden, id_repuesto, cantidad FROM orden_repuesto "
                + "WHERE id_orden_repuesto = ? FOR UPDATE";
        String updateSql = "UPDATE orden_repuesto SET id_repuesto = ?, cantidad = ?, precio_unidad = ? "
                + "WHERE id_orden_repuesto = ?";

        try (Connection con = conexion_db.conectar()) {
            con.setAutoCommit(false);
            try {
                int idOrden = objeto.getOrden().getid_orden();
                if (!ordenEnProceso(con, idOrden)) {
                    con.rollback();
                    return false;
                }

                int idRepuestoAnterior;
                int cantidadAnterior;
                try (PreparedStatement ps = con.prepareStatement(selectSql)) {
                    ps.setInt(1, objeto.getId_orden_repuesto());
                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next() || rs.getInt("id_orden") != idOrden) {
                            con.rollback();
                            return false;
                        }
                        idRepuestoAnterior = rs.getInt("id_repuesto");
                        cantidadAnterior = rs.getInt("cantidad");
                    }
                }

                int idRepuestoNuevo = objeto.getRepuesto().getId_repuesto();
                if (!bloquearRepuestos(con, idRepuestoAnterior, idRepuestoNuevo)) {
                    con.rollback();
                    return false;
                }
                if (idRepuestoAnterior == idRepuestoNuevo) {
                    int diferencia = objeto.getCantidad() - cantidadAnterior;
                    if (diferencia > 0 && !descontarExistencia(con, idRepuestoNuevo, diferencia)) {
                        con.rollback();
                        return false;
                    }
                    if (diferencia < 0) {
                        aumentarExistencia(con, idRepuestoAnterior, -diferencia);
                    }
                } else {
                    aumentarExistencia(con, idRepuestoAnterior, cantidadAnterior);
                    if (!descontarExistencia(con, idRepuestoNuevo, objeto.getCantidad())) {
                        con.rollback();
                        return false;
                    }
                }

                try (PreparedStatement ps = con.prepareStatement(updateSql)) {
                    ps.setInt(1, idRepuestoNuevo);
                    ps.setInt(2, objeto.getCantidad());
                    ps.setBigDecimal(3, objeto.getPrecio_unidad());
                    ps.setInt(4, objeto.getId_orden_repuesto());
                    boolean actualizado = ps.executeUpdate() > 0;
                    if (!actualizado) {
                        con.rollback();
                        return false;
                    }
                }
                con.commit();
                return true;
            } catch (SQLException e) {
                con.rollback();
                throw e;
            } finally {
                con.setAutoCommit(true);
            }
        } catch (SQLException e) {
            System.err.println("Error al actualizar el repuesto asignado y el inventario");
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean eliminar(Integer id) {
        String orderSql = "SELECT id_orden FROM orden_repuesto WHERE id_orden_repuesto = ?";
        String allocationSql = "SELECT id_repuesto, cantidad FROM orden_repuesto "
                + "WHERE id_orden_repuesto = ? FOR UPDATE";
        String deleteSql = "DELETE FROM orden_repuesto WHERE id_orden_repuesto = ?";

        try (Connection con = conexion_db.conectar()) {
            con.setAutoCommit(false);
            try {
                int idOrden;
                try (PreparedStatement ps = con.prepareStatement(orderSql)) {
                    ps.setInt(1, id);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) {
                            con.rollback();
                            return false;
                        }
                        idOrden = rs.getInt("id_orden");
                    }
                }
                if (!ordenEnProceso(con, idOrden)) {
                    con.rollback();
                    return false;
                }
                int idRepuesto;
                int cantidad;
                try (PreparedStatement ps = con.prepareStatement(allocationSql)) {
                    ps.setInt(1, id);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) {
                            con.rollback();
                            return false;
                        }
                        idRepuesto = rs.getInt("id_repuesto");
                        cantidad = rs.getInt("cantidad");
                    }
                }
                if (!bloquearRepuestos(con, idRepuesto, null)) {
                    con.rollback();
                    return false;
                }
                try (PreparedStatement ps = con.prepareStatement(deleteSql)) {
                    ps.setInt(1, id);
                    if (ps.executeUpdate() != 1) {
                        con.rollback();
                        return false;
                    }
                }
                aumentarExistencia(con, idRepuesto, cantidad);
                con.commit();
                return true;
            } catch (SQLException e) {
                con.rollback();
                throw e;
            } finally {
                con.setAutoCommit(true);
            }
        } catch (SQLException e) {
            System.err.println("Error al retirar el repuesto y devolverlo al inventario");
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public orden_repuesto buscarporid(Integer id) {
        String sql = "SELECT id_orden_repuesto, id_orden, id_repuesto, cantidad, precio_unidad "
                + "FROM orden_repuesto WHERE id_orden_repuesto = ?";
        try (Connection con = conexion_db.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapearOrdenRepuesto(rs) : null;
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar el repuesto de la orden por ID");
            e.printStackTrace();
            return null;
        }
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
            System.err.println("Error al listar los repuestos asignados a órdenes");
            e.printStackTrace();
        }
        return ordenesRepuestos;
    }

    private boolean datosValidos(orden_repuesto objeto) {
        return objeto != null
                && objeto.getOrden() != null
                && objeto.getRepuesto() != null
                && objeto.getCantidad() > 0
                && objeto.getPrecio_unidad() != null
                && objeto.getPrecio_unidad().signum() >= 0;
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

    private boolean bloquearRepuestos(Connection con, int idPrimero, Integer idSegundo) throws SQLException {
        int menor = idSegundo == null ? idPrimero : Math.min(idPrimero, idSegundo);
        int mayor = idSegundo == null ? idPrimero : Math.max(idPrimero, idSegundo);
        String sql = idSegundo == null
                ? "SELECT id_repuesto FROM repuesto WHERE id_repuesto = ? FOR UPDATE"
                : "SELECT id_repuesto FROM repuesto WHERE id_repuesto IN (?, ?) ORDER BY id_repuesto FOR UPDATE";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, menor);
            if (idSegundo != null) {
                ps.setInt(2, mayor);
            }
            try (ResultSet rs = ps.executeQuery()) {
                int encontrados = 0;
                while (rs.next()) {
                    encontrados++;
                }
                return encontrados == (idSegundo == null || menor == mayor ? 1 : 2);
            }
        }
    }

    private boolean descontarExistencia(Connection con, int idRepuesto, int cantidad) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement(
                "UPDATE repuesto SET existencia = existencia - ? WHERE id_repuesto = ? AND existencia >= ?")) {
            ps.setInt(1, cantidad);
            ps.setInt(2, idRepuesto);
            ps.setInt(3, cantidad);
            return ps.executeUpdate() == 1;
        }
    }

    private void aumentarExistencia(Connection con, int idRepuesto, int cantidad) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement(
                "UPDATE repuesto SET existencia = existencia + ? WHERE id_repuesto = ?")) {
            ps.setInt(1, cantidad);
            ps.setInt(2, idRepuesto);
            if (ps.executeUpdate() != 1) {
                throw new SQLException("No se encontró el repuesto para reintegrar inventario");
            }
        }
    }

    private orden_repuesto mapearOrdenRepuesto(ResultSet rs) throws SQLException {
        orden ordenRelacionada = new orden(rs.getInt("id_orden"), null, null, null, null);
        repuesto repuestoRelacionado = new repuesto(rs.getInt("id_repuesto"), null, null, null, 0, 0);
        return new orden_repuesto(
                rs.getInt("id_orden_repuesto"),
                ordenRelacionada,
                repuestoRelacionado,
                rs.getInt("cantidad"),
                rs.getBigDecimal("precio_unidad")
        );
    }
}

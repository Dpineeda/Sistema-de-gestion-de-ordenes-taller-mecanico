package com.umg.edu.taller.dao;

import com.umg.edu.taller.config.conexion_db;
import com.umg.edu.taller.modelo.mecanico;
import com.umg.edu.taller.modelo.orden;
import com.umg.edu.taller.modelo.vehiculo;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ordenDao implements CrudDao<orden, Integer> {

    @Override
    public boolean insertar(orden objeto) {
        if (objeto == null || objeto.getvehiculo() == null || objeto.getmecanico() == null
                || objeto.getdescripcion_problema() == null
                || objeto.getdescripcion_problema().isBlank()) {
            return false;
        }
        String sql = "INSERT INTO orden(id_orden, id_vehiculo, id_mecanico, descripcion_problema, precio_total, estado) "
                + "VALUES (?, ?, ?, ?, 0, ?)";

        try (Connection con = conexion_db.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, objeto.getid_orden());
            ps.setInt(2, objeto.getvehiculo().getId_vehiculo());
            ps.setInt(3, objeto.getmecanico().getid_mecanico());
            ps.setString(4, objeto.getdescripcion_problema());
            ps.setString(5, orden.PENDIENTE);

            boolean insertada = ps.executeUpdate() > 0;
            if (insertada) {
                objeto.setEstado(orden.PENDIENTE);
                objeto.setprecio_total(BigDecimal.ZERO);
            }
            return insertada;
        } catch (SQLException e) {
            System.err.println("Error al insertar la orden");
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean actualizar(orden objeto) {
        if (objeto == null || objeto.getvehiculo() == null || objeto.getmecanico() == null
                || objeto.getdescripcion_problema() == null
                || objeto.getdescripcion_problema().isBlank()) {
            return false;
        }
        String sql = "UPDATE orden SET id_vehiculo = ?, id_mecanico = ?, descripcion_problema = ? "
                + "WHERE id_orden = ? AND estado IN (?, ?)";

        try (Connection con = conexion_db.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, objeto.getvehiculo().getId_vehiculo());
            ps.setInt(2, objeto.getmecanico().getid_mecanico());
            ps.setString(3, objeto.getdescripcion_problema());
            ps.setInt(4, objeto.getid_orden());
            ps.setString(5, orden.PENDIENTE);
            ps.setString(6, orden.EN_PROCESO);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar la orden");
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean eliminar(Integer id) {
        String sql = "DELETE FROM orden WHERE id_orden = ? AND estado = ? "
                + "AND NOT EXISTS (SELECT 1 FROM orden_repuesto WHERE id_orden = ?) "
                + "AND NOT EXISTS (SELECT 1 FROM orden_servicio WHERE id_orden = ?) "
                + "AND NOT EXISTS (SELECT 1 FROM comprobante WHERE id_orden = ?)";

        try (Connection con = conexion_db.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.setString(2, orden.PENDIENTE);
            ps.setInt(3, id);
            ps.setInt(4, id);
            ps.setInt(5, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar la orden");
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public orden buscarporid(Integer id) {
        String sql = "SELECT id_orden, id_vehiculo, id_mecanico, descripcion_problema, precio_total, estado "
                + "FROM orden WHERE id_orden = ?";

        try (Connection con = conexion_db.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapearOrden(rs) : null;
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar la orden por ID");
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<orden> listartodos() {
        String sql = "SELECT id_orden, id_vehiculo, id_mecanico, descripcion_problema, precio_total, estado FROM orden";
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

    public List<orden> listarPorVehiculo(int idVehiculo) {
        String sql = "SELECT id_orden, id_vehiculo, id_mecanico, descripcion_problema, precio_total, estado "
                + "FROM orden WHERE id_vehiculo = ? ORDER BY id_orden DESC";
        List<orden> ordenes = new ArrayList<>();

        try (Connection con = conexion_db.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idVehiculo);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ordenes.add(mapearOrden(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al consultar el historial del vehículo");
            e.printStackTrace();
        }
        return ordenes;
    }

    public List<orden> listarPorEstado(String estado) {
        if (!orden.PENDIENTE.equals(estado) && !orden.EN_PROCESO.equals(estado)
                && !orden.FINALIZADA.equals(estado) && !orden.CANCELADA.equals(estado)) {
            throw new IllegalArgumentException("Estado de orden no válido");
        }
        String sql = "SELECT id_orden, id_vehiculo, id_mecanico, descripcion_problema, precio_total, estado "
                + "FROM orden WHERE estado = ?";
        List<orden> ordenes = new ArrayList<>();
        try (Connection con = conexion_db.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, estado);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ordenes.add(mapearOrden(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al consultar órdenes por estado");
            e.printStackTrace();
        }
        return ordenes;
    }

    public List<orden> listarPorMecanico(int idMecanico) {
        String sql = "SELECT id_orden, id_vehiculo, id_mecanico, descripcion_problema, precio_total, estado "
                + "FROM orden WHERE id_mecanico = ?";
        List<orden> ordenes = new ArrayList<>();
        try (Connection con = conexion_db.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idMecanico);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ordenes.add(mapearOrden(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al consultar órdenes por mecánico");
            e.printStackTrace();
        }
        return ordenes;
    }

    public List<orden> listarPorFecha(LocalDateTime inicio, LocalDateTime fin) {
        if (inicio == null || fin == null || !inicio.isBefore(fin)) {
            throw new IllegalArgumentException("El inicio debe ser anterior al fin del rango");
        }
        String sql = "SELECT o.id_orden, o.id_vehiculo, o.id_mecanico, o.descripcion_problema, o.precio_total, o.estado "
                + "FROM orden o JOIN comprobante c ON c.id_orden = o.id_orden "
                + "WHERE c.fecha_emision >= ? AND c.fecha_emision < ?";
        List<orden> ordenes = new ArrayList<>();
        try (Connection con = conexion_db.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setTimestamp(1, Timestamp.valueOf(inicio));
            ps.setTimestamp(2, Timestamp.valueOf(fin));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ordenes.add(mapearOrden(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al consultar órdenes dentro del rango de fechas");
            e.printStackTrace();
        }
        return ordenes;
    }

    public List<orden> listarPorPlaca(String placa) {
        String sql = "SELECT o.id_orden, o.id_vehiculo, o.id_mecanico, o.descripcion_problema, "
                + "o.precio_total, o.estado FROM orden o JOIN vehiculo v ON v.id_vehiculo = o.id_vehiculo "
                + "WHERE v.placa = ? ORDER BY o.id_orden DESC";
        List<orden> ordenes = new ArrayList<>();
        try (Connection con = conexion_db.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, placa);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ordenes.add(mapearOrden(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al consultar historial de órdenes por placa");
            e.printStackTrace();
        }
        return ordenes;
    }

    public boolean iniciarReparacion(Integer id) {
        String sql = "UPDATE orden SET estado = ? WHERE id_orden = ? AND estado = ?";

        try (Connection con = conexion_db.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, orden.EN_PROCESO);
            ps.setInt(2, id);
            ps.setString(3, orden.PENDIENTE);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al iniciar la reparación");
            e.printStackTrace();
            return false;
        }
    }

    public boolean finalizarOrden(Integer id) {
        String lockSql = "SELECT estado FROM orden WHERE id_orden = ? FOR UPDATE";
        String serviceSql = "SELECT COUNT(*) FROM orden_servicio WHERE id_orden = ?";
        String totalsSql = "SELECT "
                + "COALESCE((SELECT SUM(costo) FROM orden_servicio WHERE id_orden = ?), 0) "
                + "+ COALESCE((SELECT SUM(cantidad * precio_unidad) FROM orden_repuesto WHERE id_orden = ?), 0) AS total, "
                + "COALESCE((SELECT SUM(cantidad * precio_unidad) FROM orden_repuesto WHERE id_orden = ?), 0) AS total_repuesto";
        String updateSql = "UPDATE orden SET estado = ?, precio_total = ? WHERE id_orden = ?";
        String receiptSql = "INSERT INTO comprobante(id_orden, fecha_emision, total_repuesto, total) VALUES (?, ?, ?, ?)";

        try (Connection con = conexion_db.conectar()) {
            con.setAutoCommit(false);
            try {
                if (!ordenEstaEnProceso(con, lockSql, id) || !tieneServicios(con, serviceSql, id)) {
                    con.rollback();
                    return false;
                }

                BigDecimal total;
                BigDecimal totalRepuesto;
                try (PreparedStatement ps = con.prepareStatement(totalsSql)) {
                    ps.setInt(1, id);
                    ps.setInt(2, id);
                    ps.setInt(3, id);
                    try (ResultSet rs = ps.executeQuery()) {
                        rs.next();
                        total = rs.getBigDecimal("total");
                        totalRepuesto = rs.getBigDecimal("total_repuesto");
                    }
                }

                try (PreparedStatement ps = con.prepareStatement(updateSql)) {
                    ps.setString(1, orden.FINALIZADA);
                    ps.setBigDecimal(2, total);
                    ps.setInt(3, id);
                    if (ps.executeUpdate() != 1) {
                        con.rollback();
                        return false;
                    }
                }

                try (PreparedStatement ps = con.prepareStatement(receiptSql, Statement.RETURN_GENERATED_KEYS)) {
                    ps.setInt(1, id);
                    ps.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
                    ps.setBigDecimal(3, totalRepuesto);
                    ps.setBigDecimal(4, total);
                    ps.executeUpdate();
                    try (ResultSet keys = ps.getGeneratedKeys()) {
                        if (!keys.next()) {
                            throw new SQLException("No se generó el identificador del comprobante");
                        }
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
            System.err.println("Error al finalizar la orden y generar el comprobante");
            e.printStackTrace();
            return false;
        }
    }

    public boolean cancelarOrden(Integer id) {
        String lockSql = "SELECT estado FROM orden WHERE id_orden = ? FOR UPDATE";
        String restoreSql = "UPDATE repuesto r JOIN "
                + "(SELECT id_repuesto, SUM(cantidad) AS cantidad FROM orden_repuesto WHERE id_orden = ? GROUP BY id_repuesto) x "
                + "ON r.id_repuesto = x.id_repuesto SET r.existencia = r.existencia + x.cantidad";
        String deletePartsSql = "DELETE FROM orden_repuesto WHERE id_orden = ?";
        String updateOrderSql = "UPDATE orden SET estado = ? WHERE id_orden = ? AND estado IN (?, ?)";

        try (Connection con = conexion_db.conectar()) {
            con.setAutoCommit(false);
            try {
                if (!ordenPuedeCancelarse(con, lockSql, id)) {
                    con.rollback();
                    return false;
                }
                try (PreparedStatement ps = con.prepareStatement(restoreSql)) {
                    ps.setInt(1, id);
                    ps.executeUpdate();
                }
                try (PreparedStatement ps = con.prepareStatement(deletePartsSql)) {
                    ps.setInt(1, id);
                    ps.executeUpdate();
                }
                try (PreparedStatement ps = con.prepareStatement(updateOrderSql)) {
                    ps.setString(1, orden.CANCELADA);
                    ps.setInt(2, id);
                    ps.setString(3, orden.PENDIENTE);
                    ps.setString(4, orden.EN_PROCESO);
                    if (ps.executeUpdate() != 1) {
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
            System.err.println("Error al cancelar la orden y reintegrar los repuestos");
            e.printStackTrace();
            return false;
        }
    }

    private boolean ordenEstaEnProceso(Connection con, String sql, int id) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && orden.EN_PROCESO.equals(rs.getString("estado"));
            }
        }
    }

    private boolean ordenPuedeCancelarse(Connection con, String sql, int id) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return false;
                }
                String estado = rs.getString("estado");
                return orden.PENDIENTE.equals(estado) || orden.EN_PROCESO.equals(estado);
            }
        }
    }

    private boolean tieneServicios(Connection con, String sql, int id) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }

    private orden mapearOrden(ResultSet rs) throws SQLException {
        vehiculo vehiculoOrden = new vehiculo(
                rs.getInt("id_vehiculo"), null, null, null, null, null, null);
        mecanico mecanicoOrden = new mecanico(
                rs.getInt("id_mecanico"), null, null);
        return new orden(
                rs.getInt("id_orden"),
                vehiculoOrden,
                mecanicoOrden,
                rs.getString("descripcion_problema"),
                rs.getBigDecimal("precio_total"),
                rs.getString("estado")
        );
    }
}

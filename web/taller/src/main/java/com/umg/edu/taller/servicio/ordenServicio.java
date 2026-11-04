package com.umg.edu.taller.servicio;

import com.umg.edu.taller.dao.ordenDao;
import com.umg.edu.taller.dao.orden_repuestoDao;
import com.umg.edu.taller.dao.orden_servicioDao;
import com.umg.edu.taller.modelo.orden;
import com.umg.edu.taller.modelo.orden_repuesto;
import com.umg.edu.taller.modelo.orden_servicio;
import java.util.List;
import java.util.stream.Collectors;

public class ordenServicio {
    private final ordenDao ordenDao = new ordenDao();
    private final orden_servicioDao ordenServicioDao = new orden_servicioDao();
    private final orden_repuestoDao ordenRepuestoDao = new orden_repuestoDao();

    public List<orden> listarOrdenes() {
        return ordenDao.listartodos();
    }

    public orden buscarOrden(int id) {
        return ordenDao.buscarporid(id);
    }

    public boolean guardarOrden(orden orden) {
        if (orden == null || orden.getvehiculo() == null || orden.getmecanico() == null
                || orden.getdescripcion_problema() == null
                || orden.getdescripcion_problema().isBlank()) {
            return false;
        }

        if (orden.getid_orden() <= 0) {
            return ordenDao.insertar(orden);
        }
        return ordenDao.actualizar(orden);
    }

    public boolean iniciarReparacion(int id) {
        return ordenDao.iniciarReparacion(id);
    }

    public boolean finalizarOrden(int id) {
        return ordenDao.finalizarOrden(id);
    }

    public boolean cancelarOrden(int id) {
        return ordenDao.cancelarOrden(id);
    }

    public boolean eliminarOrden(int id) {
        return ordenDao.eliminar(id);
    }

    public List<orden> listarPorEstado(String estado) {
        return ordenDao.listarPorEstado(estado);
    }

    public List<orden> listarPorMecanico(int idMecanico) {
        return ordenDao.listarPorMecanico(idMecanico);
    }

    public List<orden> listarPorFecha(java.time.LocalDateTime inicio, java.time.LocalDateTime fin) {
        return ordenDao.listarPorFecha(inicio, fin);
    }

    public List<orden_servicio> listarServiciosAsignados() {
        return ordenServicioDao.listartodos();
    }

    public List<orden_servicio> listarServiciosAsignados(int idOrden) {
        return listarServiciosAsignados().stream()
                .filter(asignacion -> asignacion.getOrden() != null
                        && asignacion.getOrden().getid_orden() == idOrden)
                .collect(Collectors.toList());
    }

    public orden_servicio buscarServicioAsignado(int id) {
        return ordenServicioDao.buscarporid(id);
    }

    public boolean guardarServicioEnOrden(orden_servicio asignacion) {
        if (asignacion == null) {
            return false;
        }

        if (asignacion.getId_orden_servicio() <= 0) {
            return ordenServicioDao.insertar(asignacion);
        }
        return ordenServicioDao.actualizar(asignacion);
    }

    public boolean retirarServicioDeOrden(int id) {
        return ordenServicioDao.eliminar(id);
    }

    public List<orden_repuesto> listarRepuestosAsignados() {
        return ordenRepuestoDao.listartodos();
    }

    public List<orden_repuesto> listarRepuestosAsignados(int idOrden) {
        return listarRepuestosAsignados().stream()
                .filter(asignacion -> asignacion.getOrden() != null
                        && asignacion.getOrden().getid_orden() == idOrden)
                .collect(Collectors.toList());
    }

    public orden_repuesto buscarRepuestoAsignado(int id) {
        return ordenRepuestoDao.buscarporid(id);
    }

    public boolean guardarRepuestoEnOrden(orden_repuesto asignacion) {
        if (asignacion == null) {
            return false;
        }

        if (asignacion.getId_orden_repuesto() <= 0) {
            return ordenRepuestoDao.insertar(asignacion);
        }
        return ordenRepuestoDao.actualizar(asignacion);
    }

    public boolean retirarRepuestoDeOrden(int id) {
        return ordenRepuestoDao.eliminar(id);
    }
}

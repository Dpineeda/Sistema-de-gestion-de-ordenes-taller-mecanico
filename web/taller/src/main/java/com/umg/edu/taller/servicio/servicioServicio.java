package com.umg.edu.taller.servicio;

import com.umg.edu.taller.dao.servicioDao;
import com.umg.edu.taller.modelo.servicio;
import java.util.List;

public class servicioServicio {
    private final servicioDao servicioDao = new servicioDao();

    public List<servicio> listarServicios() {
        return servicioDao.listartodos();
    }

    public servicio buscarServicio(int id) {
        return servicioDao.buscarporid(id);
    }

    public boolean guardarServicio(servicio servicio) {
        if (servicio == null || servicio.getdescripcion() == null
                || servicio.getdescripcion().isBlank()
                || servicio.getcosto_manodeobra() == null
                || servicio.getcosto_manodeobra().signum() < 0) {
            return false;
        }

        if (servicio.getid_servicio() <= 0) {
            return servicioDao.insertar(servicio);
        }
        return servicioDao.actualizar(servicio);
    }

    public boolean eliminarServicio(int id) {
        return id > 0 && servicioDao.eliminar(id);
    }
}

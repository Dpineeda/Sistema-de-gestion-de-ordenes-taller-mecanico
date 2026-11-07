package com.umg.edu.taller.servicio;

import com.umg.edu.taller.dao.repuestoDao;
import com.umg.edu.taller.modelo.repuesto;
import java.util.List;

public class repuestoServicio {
    private final repuestoDao repuestoDao = new repuestoDao();

    public List<repuesto> listarRepuestos() {
        return repuestoDao.listartodos();
    }

    public repuesto buscarRepuesto(int id) {
        return id <= 0 ? null : repuestoDao.buscarporid(id);
    }

    public boolean guardarRepuesto(repuesto repuesto) {
        if (repuesto == null || repuesto.getNombre() == null || repuesto.getNombre().isBlank()
                || repuesto.getDescripcion() == null || repuesto.getDescripcion().isBlank()
                || repuesto.getPrecio() == null || repuesto.getPrecio().signum() < 0
                || repuesto.getExistencia() < 0 || repuesto.getMinimo_en_stock() < 0) {
            return false;
        }

        if (repuesto.getId_repuesto() <= 0) {
            return repuestoDao.insertar(repuesto);
        }
        return repuestoDao.actualizar(repuesto);
    }

    public boolean abastecer(int idRepuesto, int unidades) {
        return idRepuesto > 0 && unidades > 0 && repuestoDao.abastecer(idRepuesto, unidades);
    }

    public List<repuesto> listarAgotados() {
        return repuestoDao.listarAgotados();
    }

    public List<repuesto> listarBajoInventario() {
        return repuestoDao.listarBajoInventario();
    }

    public List<repuesto> listarExistenciaHastaCinco() {
        return repuestoDao.listarExistenciaHastaCinco();
    }

    public boolean eliminarRepuesto(int id) {
        return id > 0 && repuestoDao.eliminar(id);
    }
}

package com.umg.edu.taller.servicio;

import com.umg.edu.taller.dao.vehiculoDao;
import com.umg.edu.taller.modelo.orden;
import com.umg.edu.taller.modelo.vehiculo;
import java.util.List;

public class vehiculoServicio {
    private final vehiculoDao vehiculoDao = new vehiculoDao();

    public List<vehiculo> listarVehiculos() {
        return vehiculoDao.listartodos();
    }

    public vehiculo buscarVehiculo(int id) {
        return id <= 0 ? null : vehiculoDao.buscarporid(id);
    }

    public boolean guardarVehiculo(vehiculo vehiculo) {
        if (vehiculo == null || vehiculo.getPlaca() == null || vehiculo.getPlaca().isBlank()
                || vehiculo.getMarca() == null || vehiculo.getMarca().isBlank()
                || vehiculo.getModelo() == null || vehiculo.getModelo().isBlank()
                || vehiculo.getAnio() == null || vehiculo.getCliente() == null
                || vehiculo.getCliente().getid_cliente() == null
                || vehiculo.getCliente().getid_cliente().isBlank()) {
            return false;
        }

        if (vehiculo.getId_vehiculo() <= 0) {
            return vehiculoDao.insertar(vehiculo);
        }
        return vehiculoDao.actualizar(vehiculo);
    }

    public boolean eliminarVehiculo(int id) {
        return id > 0 && vehiculoDao.eliminar(id);
    }

    public List<orden> listarHistorialOrdenes(int idVehiculo) {
        return vehiculoDao.listarHistorialOrdenes(idVehiculo);
    }

    public List<orden> listarHistorialOrdenes(String placa) {
        if (placa == null || placa.isBlank()) {
            throw new IllegalArgumentException("La placa es obligatoria");
        }
        return vehiculoDao.listarHistorialOrdenes(placa.trim());
    }
}

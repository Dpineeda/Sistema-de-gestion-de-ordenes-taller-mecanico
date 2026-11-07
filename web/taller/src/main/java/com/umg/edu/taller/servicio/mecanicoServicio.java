package com.umg.edu.taller.servicio;

import com.umg.edu.taller.dao.mecanicoDao;
import com.umg.edu.taller.modelo.mecanico;
import java.util.List;

public class mecanicoServicio {
    private final mecanicoDao mecanicoDao = new mecanicoDao();

    public List<mecanico> listarMecanicos() {
        return mecanicoDao.listartodos();
    }

    public mecanico buscarMecanico(int id) {
        return id <= 0 ? null : mecanicoDao.buscarporid(id);
    }

    public boolean guardarMecanico(mecanico mecanico) {
        if (mecanico == null || mecanico.getnombre() == null || mecanico.getnombre().isBlank()
                || mecanico.gettelefono() == null || mecanico.gettelefono().isBlank()) {
            return false;
        }

        if (mecanico.getid_mecanico() <= 0) {
            return mecanicoDao.insertar(mecanico);
        }
        return mecanicoDao.actualizar(mecanico);
    }

    public boolean eliminarMecanico(int id) {
        return id > 0 && mecanicoDao.eliminar(id);
    }
}
